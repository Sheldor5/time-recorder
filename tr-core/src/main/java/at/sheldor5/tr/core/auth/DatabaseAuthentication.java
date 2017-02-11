package at.sheldor5.tr.core.auth;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.StringUtils;
import at.sheldor5.tr.api.utils.UuidUtils;
import at.sheldor5.tr.core.persistence.DatabaseConnection;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseAuthentication implements AuthenticationPlugin {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = Logger.getLogger(DatabaseAuthentication.class.getName());

  private static final String NAME = "tr-db";

  private static final String INSERT_USER = "INSERT INTO [users] ([uuid], [username], [password], [forename], [surname]) VALUES (?, ?, ?, ?, ?)";
  private static final String SELECT_USER = "SELECT [pk_user_id], [uuid], [username], [forename], [surname] FROM [users] WHERE [username] = ? AND [password] = ?";

  private DataSource dataSource;

  private static byte[] getRandomUUIDBytes() {
    final UUID uuid = UUID.randomUUID();
    final ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }

  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void initialize() throws IllegalStateException {
    Connection connection;
    try {
      connection = dataSource.getConnection();
    } catch (final SQLException sqle) {
      throw new IllegalStateException(sqle);
    }

    final DatabaseConnection databaseConnection = new DatabaseConnection(connection);

    if (!databaseConnection.tableExists("users")) {
      databaseConnection.executeScript("/sql/sqlserver/create_user_table.sql");
    }

    if (!databaseConnection.tableExists("users")) {
      throw new IllegalStateException("Unable to create user table");
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

      try (final PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {

        statement.setBytes(1, uuid_bytes);
        statement.setString(2, user.getUsername());
        statement.setString(3, StringUtils.getMD5(plainTextPassword));
        statement.setString(4, user.getForename());
        statement.setString(5, user.getSurname());

        if (statement.executeUpdate() != 0) {

          success = true;

        } else {
          LOGGER.severe("Could not store user, insert failed");
        }

      } catch (final SQLException sqle) {
        LOGGER.severe("Statement Execution Error: " + sqle.getMessage());
      }

      if (success) {
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        user.setUuidBytes(uuid_bytes);
        LOGGER.fine("Successfully stored user with ID " + id);
      } else {
        if (!connection.getAutoCommit()) {
          connection.rollback();
        }
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

      try (final PreparedStatement statement = connection.prepareStatement(SELECT_USER, Statement.RETURN_GENERATED_KEYS)) {

        statement.setString(1, username);
        statement.setString(2, StringUtils.getMD5(plainTextPassword));

        try (final ResultSet result = statement.executeQuery()) {

          if (result.next()) {
            final User user = new User();
            user.setId(result.getInt("pk_user_id"));
            user.setUuidBytes(result.getBytes("uuid"));
            user.setUsername(result.getString("username"));
            user.setForename(result.getString("forename"));
            user.setSurname(result.getString("surname"));

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