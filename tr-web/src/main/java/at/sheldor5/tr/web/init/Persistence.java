package at.sheldor5.tr.web.init;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.mappings.UserProjectMapping;
import at.sheldor5.tr.persistence.provider.ProjectProvider;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;
import at.sheldor5.tr.persistence.provider.UserProjectMappingProvider;
import at.sheldor5.tr.persistence.provider.UserProvider;
import org.mindrot.jbcrypt.BCrypt;

public class Persistence implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(Persistence.class.getName());

  private static final String ADMIN = "admin";
  private static final String TESTUSER = "test";

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    EntityManagerHelper.setupGlobalProperties();
    systemUsers();
  }
  private void systemUsers() {
    final User admin = new User(ADMIN, BCrypt.hashpw(ADMIN, BCrypt.gensalt()), "Admin", "");
    final User testUser = new User(TESTUSER, BCrypt.hashpw(TESTUSER, BCrypt.gensalt()), "Test", "");

    final UserMapping adminMapping = user(admin, Role.ADMIN);
    final UserMapping testUserMapping = user(testUser, Role.USER);

    final Project tr = project("time-recorder", new UserMapping[] {adminMapping, testUserMapping});
    final Project x = project("Project X", new UserMapping[] {testUserMapping});
    final Project hl3 = project("Half Life 3", new UserMapping[] {testUserMapping});

    TestData testData = new TestData();
    testData.setup(adminMapping, tr);
  }

  private UserMapping user(final User user, final Role role) {
    final UserProvider userProvider = new UserProvider();
    final UserMappingProvider userMappingProvider = new UserMappingProvider();
    final UserMapping userMapping = new UserMapping();

    final User existing = userProvider.get(user.getUsername());
    if (existing == null) {
      userProvider.save(user);
      userMapping.setUuid(user.getUuid());
      userMapping.setRole(role);
      userMappingProvider.save(userMapping);
    }
    return userMapping;
  }

  private Project project(final String name, final UserMapping userMappings[]) {
    final Project project = new Project(name);
    final ProjectProvider projectProvider = new ProjectProvider();
    projectProvider.save(project);

    final UserProjectMappingProvider userProjectMappingProvider = new UserProjectMappingProvider();

    for (final UserMapping userMapping : userMappings) {
      userProjectMappingProvider.save(new UserProjectMapping(userMapping, project));
    }
    return project;
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    EntityManagerHelper.closeEntityManagerFactory();
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
