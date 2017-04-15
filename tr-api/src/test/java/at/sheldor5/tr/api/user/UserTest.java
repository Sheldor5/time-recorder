package at.sheldor5.tr.api.user;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

  @Test
  public void should_set_password() {
    final String expected = "password";
    final User user = new User();
    user.setPassword(expected);
    Assert.assertEquals(expected, user.getPassword());
  }

}