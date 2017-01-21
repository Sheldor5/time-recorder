package at.sheldor5.tr.core.objects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
  public static Connection getConnection() throws SQLException {
    if (connection == null) {
      connect();
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
      throw new RuntimeException("JDBC Class <" + jdbcClass + "> not found!");
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

    LOGGER.info("Connecting to Database using JDBC URL \"{}\"", jdbc);

    // connect
    connection = DriverManager.getConnection(jdbc);

    LOGGER.info("Successfully connected to Database");
  }
}