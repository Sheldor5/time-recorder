package at.sheldor5.tr.web.init;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.mappings.UserProjectMapping;
import at.sheldor5.tr.persistence.provider.ProjectProvider;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;
import at.sheldor5.tr.persistence.provider.UserProjectMappingProvider;
import at.sheldor5.tr.persistence.provider.UserProvider;
import at.sheldor5.tr.web.DataAccessLayer;
import at.sheldor5.tr.web.module.authentication.AuthenticationManager;
import at.sheldor5.tr.web.module.authentication.InternalAuthentication;
import org.mindrot.jbcrypt.BCrypt;

public class Persistence implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(Persistence.class.getName());

  private static final String USER_ADMIN = "admin";

  private static final String USER_TEST = "test";

  private static EntityManager ENTITY_MANAGER;
  private static DataAccessLayer DATA_ACCESS_LAYER;
  private static AuthenticationManager AUTHENTICATION_MANAGER = new AuthenticationManager();

  private static UserMapping ADMIN_MAPPING;
  private static UserMapping TEST_MAPPING;

  private static String DEFAULT_PROJECT_NAME = "time-recorder";
  private static Project PROJECT_DEFAULT;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    EntityManagerHelper.setupGlobalProperties();
    ENTITY_MANAGER = EntityManagerHelper.createEntityManager();
    DATA_ACCESS_LAYER = new DataAccessLayer(ENTITY_MANAGER);
    AUTHENTICATION_MANAGER.addPlugin(new InternalAuthentication());
    AuthenticationManager.getInstance().addPlugin(new InternalAuthentication());
    systemEntities();
    if (GlobalProperties.getBoolean("system.test.data")) {
      testEntities();
    }
    DATA_ACCESS_LAYER.close();
  }

  private void systemEntities() {
    final User admin = new User(USER_ADMIN, USER_ADMIN, "Administrator", "");

    ADMIN_MAPPING = get(admin, Role.ADMIN);

    if (ADMIN_MAPPING == null) {
      throw new RuntimeException("Unable to create system user \"" + USER_ADMIN + "\"");
    }

    PROJECT_DEFAULT = project(DEFAULT_PROJECT_NAME, new UserMapping[] {ADMIN_MAPPING});

    if (PROJECT_DEFAULT == null) {
      throw new RuntimeException("Unable to create default project \"time-recorder\"");
    }
  }

  private UserMapping get(final User user, final Role role) {
    UserMapping userMapping = AUTHENTICATION_MANAGER.getUserMapping(user.getUsername(), user.getPassword());
    if (userMapping == null) {
      AUTHENTICATION_MANAGER.saveUser(user);
      userMapping = AUTHENTICATION_MANAGER.getUserMapping(user.getUsername(), user.getUsername());
    }
    if (userMapping.getRole() != role) {
      userMapping.setRole(role);
      DATA_ACCESS_LAYER.save(userMapping);
    }
    return userMapping;
  }

  private void testEntities() {
    final User test = new User(USER_TEST, USER_TEST, "Test", "");

    TEST_MAPPING = get(test, Role.USER);

    if (TEST_MAPPING == null) {
      LOGGER.warning("Unable to create test data");
      return;
    }

    final Project tr = project("time-recorder", new UserMapping[]{TEST_MAPPING});
    final Project x = project("Project X", new UserMapping[]{TEST_MAPPING});
    final Project hl3 = project("Half Life 3", new UserMapping[]{TEST_MAPPING});

    TestData testData = new TestData();
    testData.setup(DATA_ACCESS_LAYER, TEST_MAPPING, tr);

  }

  private Project project(final String name, final UserMapping userMappings[]) {
    Project project = new Project(name);
    final ProjectProvider projectProvider = new ProjectProvider(ENTITY_MANAGER);

    if (projectProvider.exists(project)) {
      project = projectProvider.getProject(project.getName());
    } else {
      projectProvider.save(project);
    }

    final UserProjectMappingProvider userProjectMappingProvider = new UserProjectMappingProvider(ENTITY_MANAGER);

    UserProjectMapping userProjectMapping;
    for (final UserMapping userMapping : userMappings) {
      userProjectMapping = new UserProjectMapping(userMapping, project);
      if (!userProjectMappingProvider.exists(userProjectMapping)) {
        userProjectMappingProvider.save(userProjectMapping);
      }
    }
    return project;
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    EntityManagerHelper.closeEntityManagerFactory();
    final Enumeration<Driver> drivers = DriverManager.getDrivers();

    // clear drivers
    Driver driver;
    while (drivers.hasMoreElements()) {
      driver = drivers.nextElement();
      try {
        DriverManager.deregisterDriver(driver);
      } catch (final SQLException sqle) {
        LOGGER.warning("Failed to deregister driver: " + sqle.getMessage());
      }
    }
  }
}
