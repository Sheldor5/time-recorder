package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
class DatabaseMapper {

  private static final Logger LOGGER = LogManager.getLogger(DatabaseMapper.class);

  private static final String INSERT_USER = "INSERT INTO [users] ([username], [password], [forename], [surname]) VALUES (?, ?, ?, ?)";
  private static final String SELECT_USER = "SELECT [pk_user_id], [username], [forename], [surname] FROM [users] WHERE [username] = ? AND [password] = ?";

  private static final String INSERT_RECORD = "INSERT INTO [records] ([fk_user_id], [datetime], [type]) VALUES (?, ?, ?)";
  private static final String SELECT_RECORD = "SELECT * FROM [records] WHERE [pk_record_id] = ?";

  private final Connection connection;

  public DatabaseMapper(final Connection connection) {
    this.connection = connection;
  }

  /**
   *
   * @param user
   * @param plainTextPassword
   * @return Primary key (a.k.a. User ID, column [pk_user_id]) of the inserted user.
   * @throws SQLException
   * @throws NoSuchAlgorithmException
   */
  int addUser(final User user, final String plainTextPassword) throws SQLException, NoSuchAlgorithmException {
    final PreparedStatement statement = connection.prepareStatement(
            INSERT_USER,
            Statement.RETURN_GENERATED_KEYS);

    statement.setString(1, user.getUsername());
    statement.setString(2, getMD5(plainTextPassword));
    statement.setString(3, user.getForename());
    statement.setString(4, user.getSurname());

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Adding user {} ({} {}) with password {}",
              user.getUsername(),
              user.getForename(),
              user.getSurname(),
              plainTextPassword);
    }

    if (statement.executeUpdate() == 0) {
      connection.rollback();
      throw new SQLException("Could not create user, insertion failed");
    }

    int userId;
    try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
      if (generatedKeys.next()) {
        userId = generatedKeys.getInt(1);
        LOGGER.debug("Successfully created user with ID {}", userId);
      }
      else {
        connection.rollback();
        throw new SQLException("Creating user failed, no ID obtained");
      }
    }

    connection.commit();
    user.setUserId(userId);
    return userId;
  }

  User getUser(final String username, final String plainTextPassword) throws SQLException, NoSuchAlgorithmException {
    final PreparedStatement statement = connection.prepareStatement(
            SELECT_USER,
            Statement.RETURN_GENERATED_KEYS);

    statement.setString(1, username);
    statement.setString(2, getMD5(plainTextPassword));

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Searching for user {} with password {}", username, plainTextPassword);
    }

    final ResultSet result = statement.executeQuery();

    final User user = new User();

    if (result.next()) {
      user.setUserId(result.getInt("pk_user_id"));
      user.setUsername(result.getString("username"));
      user.setForename(result.getString("forename"));
      user.setSurname(result.getString("surname"));
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Found user {} ({} {}) with ID {}",
                user.getUsername(),
                user.getForename(),
                user.getSurname(),
                user.getUserId());
      }
    } else {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("User {} with password {} was not found", username, plainTextPassword);
      }
      return null;
    }

    if (result.next()) {
      // user not unique
      throw new SQLException("Multiple users found!");
    }

    return user;
  }

  int addRecord(final User user, final Record record) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(
            INSERT_RECORD,
            Statement.RETURN_GENERATED_KEYS);

    statement.setInt(1, user.getUserId());
    statement.setTimestamp(2, record.getTimestamp());
    statement.setBoolean(3, record.getType().getBoolean());

    if (statement.executeUpdate() != 1) {
      connection.rollback();
      throw new SQLException("Could not store record: " + statement.toString());
    }

    int recordId;
    try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
      if (generatedKeys.next()) {
        recordId = generatedKeys.getInt(1);
      }
      else {
        connection.rollback();
        throw new SQLException("Storing record failed, no ID obtained");
      }
    }

    connection.commit();
    record.setId(recordId);
    return recordId;
  }

  public Record getRecord(int id) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(SELECT_RECORD);
    statement.setInt(1, id);
    final ResultSet result = statement.executeQuery();
    if (result.next()) {
      final Record record = new Record();
      record.setId(result.getInt("pk_record_id"));
      record.setTimestamp(result.getTimestamp("datetime"));
      record.setType(RecordType.getType(result.getBoolean("type")));
      return record;
    }
    return null;
  }

  List<Record> getDay(int day, int month, int year) {
    return null;
  }

  List<Record> getMonth(int month, int year) {
    return null;
  }

  List<Record> getYear(int year) {
    return null;
  }

  void updateRecord(int owner, int oldId, Record newValues) {

  }

  void deleteRecord(int owner, int id) {

  }

  protected boolean tablesExist() throws SQLException {
    DatabaseMetaData meta;

    meta = connection.getMetaData();
    final ResultSet sys = meta.getTables(null, null, "time-recorder", new String[] {"TABLE"});
    if (!sys.next()) {
      return false;
    }
    sys.close();

    meta = connection.getMetaData();
    final ResultSet users = meta.getTables(null, null, "users", new String[] {"TABLE"});
    if (!users.next()) {
      return false;
    }
    users.close();

    meta = connection.getMetaData();
    final ResultSet records = meta.getTables(null, null, "records", new String[] {"TABLE"});
    if (!records.next()) {
      return false;
    }
    records.close();

    return true;
  }

  protected void createTables() {
    LOGGER.info("Creating application database tables ...");
    try (final BufferedReader reader =
                 new BufferedReader(new InputStreamReader(new FileInputStream(
                         new File(this.getClass().getResource("/sql/sqlserver/create_tables.sql").toURI()))))) {
      final StringBuilder stringBuilder = new StringBuilder();
      String line, command;
      while (true) {
        line = reader.readLine();
        if (line == null || line.isEmpty()) {
          command = stringBuilder.toString();
          if (!command.isEmpty()) {
            executeCommand(command);
          }
          stringBuilder.setLength(0);
          if (line == null) {
            break;
          }
        } else if (line.startsWith("--")) {
          continue;
        } else {
          stringBuilder.append(line);
          stringBuilder.append('\n');
        }
      }
    } catch (final IOException ioe) {
      System.err.println(new File(".").getAbsolutePath());
      ioe.printStackTrace();
    } catch (final SQLException sqle) {
      sqle.printStackTrace();
    } catch (final URISyntaxException use) {
      use.printStackTrace();
    }
    LOGGER.info("Successfully created application database tables ...");
  }

  private void executeCommand(final String command) throws SQLException {
    final Statement statement = connection.createStatement();
    statement.executeUpdate(command);
    if (!connection.getAutoCommit()) {
      connection.commit();
    }
  }

  public static String getMD5(final String string) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");

    /*md.update(plainTextPassword.getBytes());
    byte[] digest = md.digest();
    final StringBuffer sb = new StringBuffer();
    for (byte b : digest) {
      sb.append(String.format("%02x", b & 0xff));
    }*/
    md.update(string.getBytes(), 0, string.length());
    return new BigInteger(1, md.digest()).toString(16);
  }

  /*private void runScript(final Reader reader) throws IOException,
          SQLException {
    StringBuffer command = null;
    try {
      LineNumberReader lineReader = new LineNumberReader(reader);
      String line = null;
      while ((line = lineReader.readLine()) != null) {
        if (command == null) {
          command = new StringBuffer();
        }
        String trimmedLine = line.trim();
        if (trimmedLine.startsWith("--")) {
          println(trimmedLine);
        } else if (trimmedLine.length() < 1
                || trimmedLine.startsWith("//")) {
          // Do nothing
        } else if (trimmedLine.length() < 1
                || trimmedLine.startsWith("--")) {
          // Do nothing
        } else if (!fullLineDelimiter
                && trimmedLine.endsWith(getDelimiter())
                || fullLineDelimiter
                && trimmedLine.equals(getDelimiter())) {
          command.append(line.substring(0, line
                  .lastIndexOf(getDelimiter())));
          command.append(" ");
          Statement statement = connection.createStatement();

          println(command);

          boolean hasResults = false;
          if (stopOnError) {
            hasResults = statement.execute(command.toString());
          } else {
            try {
              statement.execute(command.toString());
            } catch (SQLException e) {
              e.fillInStackTrace();
              printlnError("Error executing: " + command);
              printlnError(e);
            }
          }

          if (autoCommit && !connection.getAutoCommit()) {
            connection.commit();
          }

          ResultSet rs = statement.getResultSet();
          if (hasResults && rs != null) {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            for (int i = 0; i < cols; i++) {
              String name = md.getColumnLabel(i);
              print(name + "\t");
            }
            println("");
            while (rs.next()) {
              for (int i = 0; i < cols; i++) {
                String value = rs.getString(i);
                print(value + "\t");
              }
              println("");
            }
          }

          command = null;
          try {
            statement.close();
          } catch (Exception e) {
            // Ignore to workaround a bug in Jakarta DBCP
          }
          Thread.yield();
        } else {
          command.append(line);
          command.append(" ");
        }
      }
      if (!autoCommit) {
        connection.commit();
      }
    } catch (SQLException e) {
      e.fillInStackTrace();
      printlnError("Error executing: " + command);
      printlnError(e);
      throw e;
    } catch (IOException e) {
      e.fillInStackTrace();
      printlnError("Error executing: " + command);
      printlnError(e);
      throw e;
    } finally {
      connection.rollback();
      flush();
    }
  }*/

}