package at.sheldor5.tr.web.init;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
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

  private static final String ADMIN = "admin";
  private static final String TESTUSER = "time-recorder";

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    EntityManagerHelper.setupGlobalProperties();
    systemUsers();
  }
  private void systemUsers() {
    final EntityManager entityManager = EntityManagerHelper.getEntityManager();

    final User admin = new User(ADMIN, BCrypt.hashpw(ADMIN, BCrypt.gensalt()), "Admin", "");
    final User testuser = new User(TESTUSER, BCrypt.hashpw(TESTUSER, BCrypt.gensalt()), "time-recorder", "");

    user(admin, Role.ADMIN);
    user(testuser, Role.USER);

    entityManager.close();
  }

  private void user(final User user, final Role role) {
    final UserProvider userProvider = new UserProvider();
    final UserMappingProvider userMappingProvider = new UserMappingProvider();

    final User existing = userProvider.get(user.getUsername());
    if (existing == null) {
      userProvider.save(user);
      final UserMapping adminMapping = new UserMapping(user.getUuid());
      adminMapping.setRole(role);
      userMappingProvider.save(adminMapping);
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
