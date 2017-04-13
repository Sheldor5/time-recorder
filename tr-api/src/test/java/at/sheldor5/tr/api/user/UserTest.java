package at.sheldor5.tr.api.user;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

  @Test
  public void should_set_password() {
    final String expected = "passwort";
    final User user = new User();
    user.setPassword(expected);
    Assert.assertEquals(expected, user.getPassword());
  }

  @Test
  public void should_hash_password() {
    final String unexpected = "passwort";
    final User user = new User();
    user.setPlainTextPassword(unexpected);
    Assert.assertNotEquals(unexpected, user.getPassword());
  }

}