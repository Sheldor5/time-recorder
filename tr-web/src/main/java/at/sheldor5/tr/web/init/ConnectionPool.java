package at.sheldor5.tr.web.init;

import at.sheldor5.tr.core.auth.AuthenticationManager;
import at.sheldor5.tr.core.persistence.DatabaseConnection;
import at.sheldor5.tr.api.utils.GlobalProperties;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import at.sheldor5.tr.core.persistence.DatabaseEngine;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class ConnectionPool implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(ConnectionPool.class.getName());
  private static final DataSource datasource = new DataSource();

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    final PoolProperties properties = getProperties();
    datasource.setPoolProperties(properties);

    Connection connection = null;

    // test connection
    try {
      connection = datasource.getConnection();
      final Statement st = connection.createStatement();
      final ResultSet rs = st.executeQuery("SELECT COUNT(*) AS count FROM [records]");
      while (rs.next()) {
        System.out.println("Total records in database: " + rs.getInt("count"));
      }
      rs.close();
      st.close();
    } catch (final SQLException sqle) {
      // TODO
      throw new RuntimeException("Unable to connect to database: " + sqle.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    }

    // check system tables
    try {
      connection = datasource.getConnection();
      final DatabaseConnection databaseConnection = new DatabaseConnection(connection);
      databaseConnection.initialize();
    } catch (final SQLException sqle) {
      // TODO
      throw new RuntimeException("Application tables could not be determined: " + sqle.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    }

    DatabaseEngine.getInstance().setDataSource(datasource);
    AuthenticationManager.getInstance().setDataSource(datasource);
  }

  public static Connection getConnection() {
    try {
      return datasource.getConnection();
    } catch (final SQLException sqle) {
      sqle.printStackTrace();
    }
    return null;
  }

  public static javax.sql.DataSource getDataSource() {
    return datasource;
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    final Enumeration<Driver> drivers = DriverManager.getDrivers();

    Driver driver;

    // clear drivers
    while(drivers.hasMoreElements()) {
      try {
        driver = drivers.nextElement();
        DriverManager.deregisterDriver(driver);

      } catch (final SQLException sqle) {
        LOGGER.warning("Failed to deregister driver: " + sqle.getMessage());
      }
    }
  }

  private static PoolProperties getProperties() {
    final PoolProperties properties = new PoolProperties();

    properties.setUrl(DatabaseConnection.getJDBCUrl());
    properties.setDriverClassName(GlobalProperties.getProperty("db.jdbc.class"));
    properties.setUsername(GlobalProperties.getProperty("db.user"));
    properties.setPassword(GlobalProperties.getProperty("db.pass"));

    properties.setJmxEnabled(true);
    properties.setTestWhileIdle(false);
    properties.setTestOnBorrow(true);
    properties.setValidationQuery("SELECT 1");
    properties.setTestOnReturn(false);
    properties.setValidationInterval(30000);
    properties.setTimeBetweenEvictionRunsMillis(30000);
    properties.setMaxActive(100);
    properties.setInitialSize(10);
    properties.setMaxWait(10000);
    properties.setRemoveAbandonedTimeout(60);
    properties.setMinEvictableIdleTimeMillis(30000);
    properties.setMinIdle(10);
    properties.setLogAbandoned(true);
    properties.setRemoveAbandoned(true);
    properties.setJdbcInterceptors(
            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                    + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer;"
                    + "org.apache.tomcat.jdbc.pool.interceptor.ResetAbandonedTimer");

    return properties;
  }
}