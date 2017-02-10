package at.sheldor5.tr.sdk.auth;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.objects.User;
import helper.TestUtils;
import java.sql.SQLException;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;


public class DummyAuthenticationTest {

  private static final String USER_NAME_PREFIX = "testuser_";
  private static final String USER_PASS = "testpass";
  private static final String USER_FORE = "Sheldor";
  private static final String USER_SUR = "5";

  private final AuthenticationPlugin auth = new DummyAuthentication();

  @Test
  public void test_add_user() {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);

    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_user_uniqueness() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);

    auth.addUser(user, USER_PASS);
    auth.addUser(user, USER_PASS);
  }

  @Test
  public void test_get_invalid_user() {
    final UUID uuid = UUID.randomUUID();
    final User expected = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(expected, USER_PASS);
    Assert.assertNotNull(expected.getUUID());

    final User actual = auth.getUser(expected.getUsername(), "invalid");
    Assert.assertNull(actual);
  }

  @Test
  public void test_get_valid_user() {
    final UUID uuid = UUID.randomUUID();
    final User expected = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(expected, USER_PASS);
    Assert.assertNotNull(expected.getUUID());

    final User actual = auth.getUser(expected.getUsername(), USER_PASS);
    Assert.assertEquals(expected, actual);
  }
}