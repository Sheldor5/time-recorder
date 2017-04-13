package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class UserManagerTest {

  private static final String PROPERTIES = "test.properties";

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
    DatabaseManager.init();
  }

  @Test
  public void should_persist_user_and_assign_id() {
    final User user = new User("testuser", "testpass", "Forename", "Surname");
    UserManager.save(user);
    Assert.assertTrue(user.getId() > 0);
  }

  @Test
  public void should_return_persisted_user() {
    final String username = "Sheldor5";
    final String plainTextPassword = "admin1234";
    final User user = new User(username, plainTextPassword, "Michael", "Palata");
    UserManager.save(user);
    Assert.assertTrue(user.getId() > 0);

    final User actual = UserManager.getUser(username, "admin1234");
    Assert.assertNotNull(actual);
    System.out.println(actual);
  }

}
