package at.sheldor5.tr.api.objects;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_id() {
    final User user = new User();
    user.setId(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_username_shorter_than_5_characters() {
    final User user = new User();
    user.setUsername("1234");
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_username_longer_than_32_characters() {
    final User user = new User();
    user.setUsername("012345678901234567890123456789012");
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_forename() {
    final User user = new User();
    user.setForename("012345678901234567890123456789012");
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_surname() {
    final User user = new User();
    user.setSurname("012345678901234567890123456789012");
  }

  @Test
  public void test_initialization() {
    final User user = new User(1, "testuser", "Sheldor", "5");

    Assert.assertEquals("User initialization failed", 1, user.getId());
    Assert.assertEquals("User initialization failed", "testuser", user.getUsername());
    Assert.assertEquals("User initialization failed", "Sheldor", user.getForename());
    Assert.assertEquals("User initialization failed", "5", user.getSurname());
  }

  @Test
  public void test_getter_setter() {
    final User user = new User();
    user.setId(1);
    user.setUsername("testuser");
    user.setForename("Sheldor");
    user.setSurname("5");

    Assert.assertEquals("User getter/setter failed", 1, user.getId());
    Assert.assertEquals("User getter/setter failed", "testuser", user.getUsername());
    Assert.assertEquals("User getter/setter failed", "Sheldor", user.getForename());
    Assert.assertEquals("User getter/setter failed", "5", user.getSurname());
  }

  @Test
  public void test_equality() {
    final User user = new User(1, "testuser", "Sheldor", "5");
    final User ok = new User(1, "testuser", "Sheldor", "5");

    Assert.assertEquals("Users should be equal", user, ok);
  }

  @Test
  public void test_inequality() {
    final User user = new User(1, "testuser", "Sheldor", "5");
    final User diff1 = new User(2, "testuser", "Sheldor", "5");
    final User diff2 = new User(1, "testuser1", "Sheldor", "5");
    final User diff3 = new User(1, "testuser", "Sheldor1", "5");
    final User diff4 = new User(1, "testuser", "Sheldor", "6");

    Assert.assertNotEquals("Users should not be equal", user, diff1);
    Assert.assertNotEquals("Users should not be equal", user, diff2);
    Assert.assertNotEquals("Users should not be equal", user, diff3);
    Assert.assertNotEquals("Users should not be equal", user, diff4);
    Assert.assertNotEquals("Users should not be equal", user, null);
    Assert.assertNotEquals("Users should not be equal", user, new Object());
  }
}