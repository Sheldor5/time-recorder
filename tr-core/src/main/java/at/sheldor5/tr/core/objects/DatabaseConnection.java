package at.sheldor5.tr.core.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import at.sheldor5.tr.core.utils.GlobalProperties;
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
   * Singleton Instance for Server.
   */
  private static Connection connection;

  /**
   * Singleton Instance Getter.
   *
   * @return Global Singleton Instance of this {@link DatabaseConnection}.
   */
  public static Connection getConnection() {
    if (connection == null) {
      try {
        connect();
      } catch (final SQLException sqle) {
        LOGGER.fatal(sqle);
        return null;
      }
    }
    return connection;
  }

  /**
   * Establish a connection to the database.
   *
   * @throws Exception On connection errors.
   */
  private static void connect() throws SQLException {
    // load JDBC class
    final String jdbcClass = GlobalProperties.getProperty("db.jdbc.class");
    try {
      Class.forName(jdbcClass);
    } catch (final ClassNotFoundException cnfe) {
      throw new RuntimeException("JDBC class <" + jdbcClass + "> not found");
    }

    // get full JDBC URL
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

    LOGGER.info("Connecting to database using JDBC URL \"{}\"", jdbc);

    // connect
    connection = DriverManager.getConnection(jdbc);

    LOGGER.info("Successfully connected to database");
  }

  protected static boolean tablesExist() throws SQLException {
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

  protected static void createTables() {
    LOGGER.info("Creating application database tables ...");
    try (final BufferedReader reader =
                 new BufferedReader(new InputStreamReader(new FileInputStream(
                         new File(DatabaseConnection.class.getResource("/sql/sqlserver/create_tables.sql").toURI()))))) {
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

  private static void executeCommand(final String command) throws SQLException {
    final Statement statement = connection.createStatement();
    statement.executeUpdate(command);
    if (!connection.getAutoCommit()) {
      connection.commit();
    }
  }

}