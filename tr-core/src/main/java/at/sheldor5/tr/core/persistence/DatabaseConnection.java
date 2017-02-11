package at.sheldor5.tr.core.persistence;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import at.sheldor5.tr.api.utils.GlobalProperties;

/**
 * Simple Database Connection Wrapper class.
 */
public class DatabaseConnection {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

  private static String JDBC;

  private Connection connection;

  public DatabaseConnection(final Connection connection) {
    this.connection = connection;
  }

  public void initialize() {
    if (!tableExists("time-recorder") || !tableExists("records") || !tableExists("user_mapping")) {
      executeScript("/sqlserver/create_system_tables.sql");
    }

    if (!tableExists("time-recorder") || !tableExists("records") || !tableExists("user_mapping")) {
      throw new RuntimeException("System tables not found");
    }
  }

  /**
   * Singleton Instance Getter.
   *
   * @return Global Singleton Instance of this {@link DatabaseConnection}.
   */
  public void close() {
    try {
      connection.close();
    } catch (final SQLException sqle) {
      LOGGER.warning("Failed to close database connection: " + sqle.getMessage());
    }
  }


  public boolean tableExists(final String tableName) {
    boolean result = false;
    try {
      final DatabaseMetaData meta = connection.getMetaData();
      final ResultSet set = meta.getTables(null, null, tableName, new String[]{"TABLE"});
      result = set.next();
      set.close();
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
    }
    return result;
  }

  /**
   * /sql/sqlserver/create_tables.sql
   *
   * @param script
   */
  public void executeScript(final String script) {
    LOGGER.info("Executing script: " + script);
    final InputStream is = DatabaseConnection.class.getResourceAsStream(script);
    try (final BufferedReader reader =
                 new BufferedReader(new InputStreamReader(is))) {
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
    } catch (final Exception generalException) {
      generalException.printStackTrace();
    }
  }

  private void executeCommand(final String command) throws SQLException {
    LOGGER.fine("Executing command:\n" + command);
    final Statement statement = connection.createStatement();
    statement.executeUpdate(command);
    if (!connection.getAutoCommit()) {
      connection.commit();
    }
    statement.close();
  }



  /**
   * Establish a connection to the database.
   *
   * @throws Exception On connection errors.
   */
  public static Connection getConnection() throws SQLException {
    // load JDBC class
    final String jdbcClass = GlobalProperties.getProperty("db.jdbc.class");
    try {
      Class.forName(jdbcClass);
    } catch (final ClassNotFoundException cnfe) {
      throw new RuntimeException("JDBC class <" + jdbcClass + "> not found");
    }

    // get full JDBC URL
    final String jdbc = getJDBCUrl();

    LOGGER.fine("Connecting to database using JDBC Url: " + jdbc);

    // connect
    final Connection connection = DriverManager.getConnection(jdbc);

    LOGGER.fine("Successfully connected to database");

    return connection;
  }

  public static String getJDBCUrl() {
    if (JDBC != null) {
      return JDBC;
    }
    String jdbc = GlobalProperties.getProperty("db.jdbc.url");

    if (jdbc == null || jdbc.isEmpty()) {
      // build JDBC URL from single properties
      final String vendor = GlobalProperties.getProperty("db.vendor");
      final String host = GlobalProperties.getProperty("db.host");
      final String instance = GlobalProperties.getProperty("db.instance");
      final String name = GlobalProperties.getProperty("db.name");
      final String user = GlobalProperties.getProperty("db.user");
      final String pass = GlobalProperties.getProperty("db.pass");
      final String charset = GlobalProperties.getProperty("db.charset");
      jdbc = String.format(
              "jdbc:%s://%s%s;database=%s;user=%s;password=%s;CharacterSet=%s",
              vendor, host, instance.isEmpty() ? "" : "\\" + instance, name, user, pass, charset);
    }

    JDBC = jdbc;
    return JDBC;
  }
}