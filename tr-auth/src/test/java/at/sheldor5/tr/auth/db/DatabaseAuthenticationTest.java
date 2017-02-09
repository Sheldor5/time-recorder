package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.persistence.DatabaseConnection;
import at.sheldor5.tr.api.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseAuthenticationTest {

  private static final File PROPERTIES = new File("db.properties");

  private static final Random RANDOM = new Random();
  private static final String USER_PREFIX = "testuser_";
  private static final String FORENAME = "Test";
  private static final String SURNAME = "User";
  private static final String PASSWORD = "password";

  private static Connection connection;
  private static DatabaseConnection databaseConnection;
  private static DatabaseAuthentication auth;

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(PROPERTIES);
    connection = DatabaseConnection.getConnection();
    databaseConnection = new DatabaseConnection(connection);
    databaseConnection.initialize();
    auth = new DatabaseAuthentication();
    auth.initialize();
  }

  @Test
  public void test_add_user() {
    final User user = new User(getSecureRandomUser(), FORENAME, SURNAME);
    auth.addUser(user, PASSWORD);
    Assert.assertNotNull(user.getUUID());
  }

  @Test
  public void test_get_user() {
    final UUID uuid = UUID.randomUUID();
    final String username = getSecureRandomUser();
    final User user = new User(uuid, username, FORENAME, SURNAME);
    auth.addUser(user, PASSWORD);
    Assert.assertNotNull(user.getUUID());

    final User actual = auth.getUser(username, PASSWORD);
    Assert.assertNotNull(actual.getUUID());
    Assert.assertEquals(uuid, actual.getUUID());
    Assert.assertEquals(username, actual.getUsername());
  }

  @AfterClass
  public static void cleanup() throws SQLException {
    connection.close();
  }

  private static String getSecureRandomUser() {
    long value;
    SecureRandom random;
    try {
      random = SecureRandom.getInstance("SHA1PRNG");
      value = random.nextLong();
      if (value < 0) {
        value  *= -1;
      }
    } catch (NoSuchAlgorithmException nsae) {
      System.out.println(nsae.getMessage());
      return USER_PREFIX + getRandomLong();
    }
    value = Math.abs(value);
    return USER_PREFIX + Long.toString(value);
  }

  private static synchronized long getRandomLong() {
    long next = RANDOM.nextLong();
    if (next < 0) {
      next  *= -1;
    }
    return next;
  }
}