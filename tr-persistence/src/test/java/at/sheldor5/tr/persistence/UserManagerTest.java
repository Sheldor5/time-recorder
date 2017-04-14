package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.persistence.user.DatabaseAuthentication;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class UserManagerTest {

  private static final String PROPERTIES = "test.properties";
  private static final AuthenticationPlugin AUTH_DB = new DatabaseAuthentication();

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
    DatabaseManager.init();
  }

  @Test
  public void should_persist_user_and_assign_id() {
    final String plainTextPassword = "testpass";
    final User user = new User("testuser", "Forename", "Surname");
    AUTH_DB.addUser(user, plainTextPassword);
    Assert.assertTrue(user.getId() > 0);
  }

  @Test
  public void should_return_persisted_user() {
    final String username = "Sheldor5";
    final String plainTextPassword = "admin1234";
    final User user = new User(username, "Michael", "Palata");
    AUTH_DB.addUser(user, plainTextPassword);
    Assert.assertTrue(user.getId() > 0);

    final User actual = AUTH_DB.getUser(username, plainTextPassword);
    Assert.assertNotNull(actual);
    System.out.println(actual);
  }

}
