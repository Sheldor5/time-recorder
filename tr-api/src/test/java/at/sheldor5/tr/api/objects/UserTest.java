package at.sheldor5.tr.api.objects;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class UserTest {

  private static final String USERNAME = "testuser";
  private static final String FORENAME = "Test";
  private static final String SURNAME = "User";

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_id() {
    final User user = new User();
    user.setUUID(null);
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
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USERNAME, FORENAME, SURNAME);

    Assert.assertEquals(uuid, user.getUUID());
    Assert.assertEquals(USERNAME, user.getUsername());
    Assert.assertEquals(FORENAME, user.getForename());
    Assert.assertEquals(SURNAME, user.getSurname());
  }

  @Test
  public void test_getter_setter() {
    final UUID uuid = UUID.randomUUID();
    final User user = new User();
    user.setUUID(uuid);
    user.setUsername(USERNAME);
    user.setForename(FORENAME);
    user.setSurname(SURNAME);

    Assert.assertEquals(uuid, user.getUUID());
    Assert.assertEquals(USERNAME, user.getUsername());
    Assert.assertEquals(FORENAME, user.getForename());
    Assert.assertEquals(SURNAME, user.getSurname());
  }

  @Test
  public void test_equality() {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USERNAME, FORENAME, SURNAME);
    final User ok = new User(uuid, USERNAME, FORENAME, SURNAME);

    Assert.assertEquals(user, ok);
  }

  @Test
  public void test_inequality() {
    final UUID uuid1 = UUID.randomUUID();
    final UUID uuid2 = UUID.randomUUID();
    final User user = new User(uuid1, USERNAME, FORENAME, SURNAME);
    final User diff1 = new User(uuid2, USERNAME, FORENAME, SURNAME);
    final User diff2 = new User(uuid1, "testuser1", FORENAME, SURNAME);
    final User diff3 = new User(uuid1, USERNAME, "Sheldor1", SURNAME);
    final User diff4 = new User(uuid1, USERNAME, FORENAME, "6");

    Assert.assertNotEquals(user, diff1);
    Assert.assertNotEquals(user, diff2);
    Assert.assertNotEquals(user, diff3);
    Assert.assertNotEquals(user, diff4);
    Assert.assertNotEquals(user, null);
    Assert.assertNotEquals(user, new Object());
  }

  @Test
  public void test_uuid_bytes() {
    String uuid_s = "2d5f12e9-e916-4b6f-8340-a4bd2ba84dbc";
    long mostSigBits = 3269352650341501807L;
    long leastSigBits = -8989003723843285572L;
    final UUID uuid = new UUID(mostSigBits, leastSigBits);
    final User expected = new User(uuid, USERNAME, FORENAME, SURNAME);

    final User actual = new User(USERNAME, FORENAME, SURNAME);
    actual.setUUIDBytes(expected.getUUIDBytes());

    Assert.assertEquals(expected, actual);
  }
}