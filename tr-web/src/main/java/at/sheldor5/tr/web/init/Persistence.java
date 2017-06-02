package at.sheldor5.tr.web.init;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;
import at.sheldor5.tr.persistence.provider.UserProvider;
import org.mindrot.jbcrypt.BCrypt;

public class Persistence implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(Persistence.class.getName());

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    EntityManagerHelper.setupGlobalProperties();

    // admin user
    User admin;
    try (final UserProvider userProvider = new UserProvider()) {
      admin = userProvider.get("admin");
      if (admin == null) {
        admin = new User("admin", BCrypt.hashpw("admin", BCrypt.gensalt()));
        userProvider.save(admin);

        final UserMappingProvider userMappingProvider = new UserMappingProvider();
        final UserMapping adminMapping = new UserMapping(admin.getUuid());
        adminMapping.setRole(Role.ADMIN);
        userMappingProvider.save(adminMapping);
      }
    }
  }


  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    final Enumeration<Driver> drivers = DriverManager.getDrivers();
    Driver driver;

    // clear drivers
    while (drivers.hasMoreElements()) {
      try {
        driver = drivers.nextElement();
        DriverManager.deregisterDriver(driver);
      } catch (final SQLException sqle) {
        LOGGER.warning("Failed to deregister driver: " + sqle.getMessage());
      }
    }
  }
}
