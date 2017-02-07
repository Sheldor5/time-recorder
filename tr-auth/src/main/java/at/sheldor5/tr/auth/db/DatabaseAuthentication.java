package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.AuthenticationPlugin;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.persistence.DatabaseConnection;
import at.sheldor5.tr.api.utils.StringUtils;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseAuthentication implements AuthenticationPlugin {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(DatabaseConnection.class);

  private static final String INSERT_USER = "INSERT INTO [users] ([pk_user_id], [username], [password], [forename], [surname]) VALUES (?, ?, ?, ?, ?)";
  private static final String SELECT_USER = "SELECT [pk_user_id], [username], [forename], [surname] FROM [users] WHERE [username] = ? AND [password] = ?";

  private DatabaseConnection databaseConnection;
  private Connection connection;

  public DatabaseAuthentication(final DatabaseConnection databaseConnection) throws SQLException {
    this.databaseConnection = databaseConnection;
    this.connection = databaseConnection.getConnection();

    if (!databaseConnection.tableExists("users")) {
      databaseConnection.executeScript("/sql/sqlserver/create_user_table.sql");
    }

    if (!databaseConnection.tableExists("users")) {
      throw new RuntimeException("Unable to create user table");
    }
  }

  private static byte[] getRandomUUID() {
    final UUID uuid = UUID.randomUUID();
    final ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }

  /**
   * @param user
   * @param plainTextPassword
   * @return Primary key (a.k.a. User ID, column [pk_user_id]) of the inserted user.
   * @throws SQLException
   */
  @Override
  public void addUser(final User user, final String plainTextPassword) {
    final byte uuid_bytes[] = (user.getUUID() == null ? getRandomUUID() : user.getUUIDBytes());
    final PreparedStatement statement;
    try {
      statement = connection.prepareStatement(
              INSERT_USER);

      statement.setBytes(1, uuid_bytes);
      statement.setString(2, user.getUsername());
      statement.setString(3, StringUtils.getMD5(plainTextPassword));
      statement.setString(4, user.getForename());
      statement.setString(5, user.getSurname());

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Adding user \"{}\" ({} {}) with password \"{}\"",
                user.getUsername(),
                user.getForename(),
                user.getSurname(),
                plainTextPassword);
      }

      if (statement.executeUpdate() == 0) {
        connection.rollback();
        throw new SQLException("Could not create user, insertion failed");
      }

      connection.commit();
      user.setUUIDBytes(uuid_bytes);
      LOGGER.debug("Successfully created user with ID {}", user.getUUID());
    } catch (final SQLException sqle) {
      LOGGER.error(sqle.getMessage());
    }
  }

  @Override
  public User getUser(String username, String plainTextPassword) {
    final PreparedStatement statement;
    try {
      statement = connection.prepareStatement(
              SELECT_USER,
              Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, username);
      statement.setString(2, StringUtils.getMD5(plainTextPassword));

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Searching for user \"{}\" with password \"{}\"", username, plainTextPassword);
      }

      final ResultSet result = statement.executeQuery();

      final User user = new User();

      if (result.next()) {
        user.setUUIDBytes(result.getBytes("pk_user_id"));
        user.setUsername(result.getString("username"));
        user.setForename(result.getString("forename"));
        user.setSurname(result.getString("surname"));
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Found user {} ({} {}) with ID {}",
                  user.getUsername(),
                  user.getForename(),
                  user.getSurname(),
                  user.getUUID());
        }
      } else {
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("User \"{}\" with password \"{}\" was not found", username, plainTextPassword);
        }
        return null;
      }

      if (result.next()) {
        // user not unique
        throw new SQLException("Multiple users found!");
      }

      return user;
    } catch (final SQLException sqle) {
      LOGGER.error(sqle.getMessage());
    }
    return null;
  }
}