package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.StringUtils;
import at.sheldor5.tr.api.utils.UuidUtils;
import at.sheldor5.tr.persistence.database.Database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseAuthentication implements AuthenticationPlugin {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = Logger.getLogger(DatabaseAuthentication.class.getName());

  private static final String NAME = "tr-db";

  private static final String INSERT_USER = "INSERT INTO USERS (UUID, USERNAME, PASSWORD, FORENAME, SURNAME) VALUES (?, ?, ?, ?, ?)";
  private static final String SELECT_USER = "SELECT PK_USER_ID, UUID, USERNAME, FORENAME, SURNAME FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";

  private DataSource dataSource;

  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void initialize() throws IllegalStateException {
    try (final Connection connection = dataSource.getConnection()) {
      final Database database = new Database(connection);
      final UsersTable usersTable = new UsersTable(connection);
      if (!usersTable.check()) {
        database.executeScript("/sql/h2/users.sql");
      }
      if (!usersTable.check()) {
        throw new IllegalStateException("Database table not found");
      }
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
      throw new IllegalStateException("Exception thrown during setup");
    }
  }

  /**
   * @param user
   * @param plainTextPassword
   */
  @Override
  public void addUser(final User user, final String plainTextPassword) {
    if (user == null) {
      LOGGER.warning("User is null");
      return;
    }
    if (plainTextPassword == null) {
      LOGGER.warning("Password is null");
      return;
    }
    if (plainTextPassword.length() < 5) {
      throw new IllegalArgumentException("Password too short, minimum 5 characters");
    }

    try (final Connection connection = dataSource.getConnection()) {
      final byte[] uuid_bytes = UuidUtils.getRandomUuidBytes();
      boolean success = false;
      int id = 0;

      try (final PreparedStatement statement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

        statement.setBytes(1, uuid_bytes);
        statement.setString(2, user.getUsername());
        statement.setString(3, StringUtils.getMD5(plainTextPassword));
        statement.setString(4, user.getForename());
        statement.setString(5, user.getSurname());

        try (final ResultSet resultSet = statement.getGeneratedKeys()) {
          resultSet.next();
          id = resultSet.getInt(1);
          user.setId(id);
        } catch (final SQLException sqle) {
          LOGGER.fine(sqle.getMessage());
        }


        if (statement.executeUpdate() == 1) {
          success = true;
        } else {
          LOGGER.severe("Could not store user, insert failed");
        }

      } catch (final SQLException sqle) {
        LOGGER.severe("Statement Execution Error: " + sqle.getMessage());
      }

      if (success) {
        user.setUuidBytes(uuid_bytes);
        LOGGER.fine("Successfully stored user " + user.getUsername() + " with ID " + id);
      }

    } catch (final SQLException sqle) {
      LOGGER.severe("Connection Error: " + sqle.getMessage());
    }
  }

  @Override
  public User getUser(final String username, final String plainTextPassword) {
    if (username == null) {
      LOGGER.warning("Username is null");
      return null;
    }
    if (plainTextPassword == null) {
      LOGGER.warning("Password is null");
      return null;
    }

    try (final Connection connection = dataSource.getConnection()) {

      try (final PreparedStatement statement = connection.prepareStatement(SELECT_USER)) {

        statement.setString(1, username);
        statement.setString(2, StringUtils.getMD5(plainTextPassword));

        try (final ResultSet result = statement.executeQuery()) {

          if (result.next()) {
            final User user = new User();
            user.setId(result.getInt("PK_USER_ID"));
            user.setUuidBytes(result.getBytes("UUID"));
            user.setUsername(result.getString("USERNAME"));
            user.setForename(result.getString("FORENAME"));
            user.setSurname(result.getString("SURNAME"));

            if (result.next()) {
              // record not unique
              LOGGER.severe("Multiple users found with username " + username);
              return null;
            } else if (LOGGER.isLoggable(Level.FINE)) {
              LOGGER.fine(String.format("Found user %s (%s %s) with ID %s",
                      user.getUsername(),
                      user.getForename(),
                      user.getSurname(),
                      user.getId()));
            }

            return user;

          }

          LOGGER.fine("User \"" + username + "\" not found");

        } catch (final SQLException sqle) {
          LOGGER.severe("Result Error: " + sqle.getMessage());
        }

      } catch (final SQLException sqle) {
        LOGGER.severe("Statement Execution Error: " + sqle.getMessage());
      }

    } catch (final SQLException sqle) {
      LOGGER.severe("Connection Error: " + sqle.getMessage());
    }

    return null;
  }

  @Override
  public String getName() {
    return NAME;
  }

}
