package at.sheldor5.tr.web.init;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.tomcat.jdbc.pool.DataSource;

public class ConnectionPool implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(ConnectionPool.class.getName());
  private static final DataSource datasource = new DataSource();

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    // TODO
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
}