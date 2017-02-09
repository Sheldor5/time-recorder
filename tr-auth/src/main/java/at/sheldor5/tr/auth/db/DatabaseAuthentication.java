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
import java.util.logging.Logger;

public class DatabaseAuthentication implements AuthenticationPlugin {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

  private static final String INSERT_USER = "INSERT INTO [users] ([pk_user_id], [username], [password], [forename], [surname]) VALUES (?, ?, ?, ?, ?)";
  private static final String SELECT_USER = "SELECT [pk_user_id], [username], [forename], [surname] FROM [users] WHERE [username] = ? AND [password] = ?";

  private static byte[] getRandomUUIDBytes() {
    final UUID uuid = UUID.randomUUID();
    final ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }

  @Override
  public void initialize() throws IllegalStateException {
    Connection connection;
    try {
      connection = DatabaseConnection.getConnection();
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
   * @return Primary key (a.k.a. User ID, column [pk_user_id]) of the inserted user.
   * @throws SQLException
   */
  @Override
  public void addUser(final User user, final String plainTextPassword) {
    if (user == null || plainTextPassword == null) {
      LOGGER.warning("Password is too short");
      throw new IllegalArgumentException("Password too short");
    }
    if (plainTextPassword.length() < 5) {
      throw new IllegalArgumentException("");
    }

    final byte uuid_bytes[] = (user.getUUID() == null ? getRandomUUIDBytes() : user.getUUIDBytes());

    final Connection connection;
    try {
      connection = DatabaseConnection.getConnection();
    } catch (final SQLException sqle) {
      LOGGER.severe("Failed to establish database connection: " + sqle.getMessage());
      return;
    }
    final PreparedStatement statement;
    try {
      statement = connection.prepareStatement(INSERT_USER);

      statement.setBytes(1, uuid_bytes);
      statement.setString(2, user.getUsername());
      statement.setString(3, StringUtils.getMD5(plainTextPassword));
      statement.setString(4, user.getForename());
      statement.setString(5, user.getSurname());

      if (statement.executeUpdate() == 0) {
        statement.close();
        connection.rollback();
        connection.close();
        throw new SQLException("Could not create user, insertion failed");
      }

      statement.close();
      if (!connection.getAutoCommit()) {
        connection.commit();
      }
      connection.close();
      user.setUUIDBytes(uuid_bytes);
      LOGGER.fine("Successfully created user with ID " + user.getUUID());
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
    }
  }

  @Override
  public User getUser(String username, String plainTextPassword) {
    final Connection connection;
    try {
      connection = DatabaseConnection.getConnection();
    } catch (final SQLException sqle) {
      LOGGER.severe("Failed to establish database connection: " + sqle.getMessage());
      return null;
    }
    final PreparedStatement statement;
    try {
      statement = connection.prepareStatement(
              SELECT_USER,
              Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, username);
      statement.setString(2, StringUtils.getMD5(plainTextPassword));

      final ResultSet result = statement.executeQuery();

      final User user = new User();

      if (result.next()) {
        user.setUUIDBytes(result.getBytes("pk_user_id"));
        user.setUsername(result.getString("username"));
        user.setForename(result.getString("forename"));
        user.setSurname(result.getString("surname"));
        LOGGER.fine(String.format("Found user %s (%s %s) with ID %s",
                user.getUsername(),
                user.getForename(),
                user.getSurname(),
                user.getUUID()));
      } else {
        LOGGER.fine("User \"" + username + "\" not found");
        result.close();
        statement.close();
        connection.close();
        return null;
      }

      if (result.next()) {
        // user not unique
        result.close();
        statement.close();
        connection.close();
        throw new SQLException("Multiple users found!");
      }

      result.close();
      statement.close();
      connection.close();
      return user;
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
    }
    return null;
  }
}