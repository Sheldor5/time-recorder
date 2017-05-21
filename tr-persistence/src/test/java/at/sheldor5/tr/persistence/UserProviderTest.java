package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.persistence.provider.UserProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class UserProviderTest {

  private static final String PROPERTIES = "test.properties";
  private static final UserProvider USER_PROVIDER = new UserProvider();

  @BeforeClass
  public static void setup() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
  }

  @Test
  public void test_persist_user() {
    final String username = UUID.randomUUID().toString().replace("-", "");
    final User user = new User(username, "pASSw0rD");

    USER_PROVIDER.save(user);

    Assert.assertNotNull(user.getUuid());
  }

  @Test
  public void test_find_by_username() {
    final String username = UUID.randomUUID().toString().replace("-", "");
    final User user = new User(username, "pASSw0rD");

    USER_PROVIDER.save(user);

    Assert.assertNotNull(user.getUuid());

    final User actual = USER_PROVIDER.get(username);

    Assert.assertNotNull(actual);
  }

  @AfterClass
  public static void teardown() {
    USER_PROVIDER.close();
  }
}
