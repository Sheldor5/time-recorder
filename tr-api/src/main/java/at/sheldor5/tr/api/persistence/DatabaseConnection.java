package at.sheldor5.tr.api.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import at.sheldor5.tr.api.utils.GlobalProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple Database Connection Wrapper class.
 */
public class DatabaseConnection {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(DatabaseConnection.class);


  /**
   * Singleton Instance.
   */
  private static DatabaseConnection instance;

  private Connection connection;

  public static DatabaseConnection getInstance() {
    if (instance == null) {
      try {
        instance = new DatabaseConnection();
      } catch (final SQLException sqle) {
        LOGGER.fatal(sqle);
        return null;
      }
    }
    return instance;
  }

  private DatabaseConnection() throws SQLException {
    connection = connect();

    if (!tableExists("time-recorder") || !tableExists("records")) {
      executeScript("/sql/sqlserver/create_system_tables.sql");
    }

    if (!tableExists("time-recorder") || !tableExists("records")) {
      throw new RuntimeException("System tables not found");
    }
  }

  /**
   * Singleton Instance Getter.
   *
   * @return Global Singleton Instance of this {@link DatabaseConnection}.
   */
  public Connection getConnection() {
    return connection;
  }

  /**
   * Establish a connection to the database.
   *
   * @throws Exception On connection errors.
   */
  private Connection connect() throws SQLException {
    // load JDBC class
    final String jdbcClass = GlobalProperties.getProperty("db.jdbc.class");
    try {
      Class.forName(jdbcClass);
    } catch (final ClassNotFoundException cnfe) {
      throw new RuntimeException("JDBC class <" + jdbcClass + "> not found");
    }

    // get full JDBC URL
    final String jdbc = getJDBCUrl();

    LOGGER.info("Connecting to database using JDBC URL \"{}\"", jdbc);

    // connect
    final Connection connection = DriverManager.getConnection(jdbc);

    LOGGER.info("Successfully connected to database");

    return connection;
  }

  public boolean tableExists(final String tableName) throws SQLException {
    boolean result = true;

    final DatabaseMetaData meta  = connection.getMetaData();

    final ResultSet set = meta.getTables(null, null, tableName, new String[] {"TABLE"});

    if (!set.next()) {
      result = false;
    }

    set.close();

    return result;
  }

  /**
   * /sql/sqlserver/create_tables.sql
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
    LOGGER.info("Successfully created application database tables ...");
  }

  private void executeCommand(final String command) throws SQLException {
    LOGGER.debug("Executing command:\n{}", command);
    final Statement statement = connection.createStatement();
    statement.executeUpdate(command);
    if (!connection.getAutoCommit()) {
      connection.commit();
    }
  }

  public static String getJDBCUrl() {
    String jdbc = GlobalProperties.getProperty("db.jdbc.url");

    if (jdbc == null || jdbc.isEmpty()) {
      // build JDBC URL from single properties
      final String host = GlobalProperties.getProperty("db.host");
      final String instance = GlobalProperties.getProperty("db.instance");
      final String name = GlobalProperties.getProperty("db.name");
      final String user = GlobalProperties.getProperty("db.user");
      final String pass = GlobalProperties.getProperty("db.pass");
      final String charset = GlobalProperties.getProperty("db.charset");
      jdbc = String.format(
              "jdbc:sqlserver://%s%s;database=%s;user=%s;password=%s;CharacterSet=%s",
              host, instance.isEmpty() ? "" : "\\" + instance, name, user, pass, charset);
    }

    return jdbc;
  }

}