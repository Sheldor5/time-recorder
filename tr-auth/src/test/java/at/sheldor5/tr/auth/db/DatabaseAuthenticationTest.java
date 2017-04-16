package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.api.utils.RandomUtils;
import at.sheldor5.tr.api.utils.StringUtils;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DatabaseAuthenticationTest {

  private static final String PROPERTIES = "db.properties";
  private static final DatabaseAuthentication AUTH_DB = new DatabaseAuthentication();

  private static final String USERNAME_PREFIX = "USER_";
  private static final String FORENAME = "Vorname";
  private static final String SURNAME = "Nachname";

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
    AUTH_DB.initialize();
  }

  @Test
  public void should_persist_and_return_user() {
    final String username = RandomUtils.getRandomUsername(USERNAME_PREFIX);
    final String plainTextPassword = StringUtils.getMD5(username);
    final String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    final User user = new User(username, hashedPassword, FORENAME, SURNAME);

    AUTH_DB.saveUser(user);

    final User actual = AUTH_DB.getUser(username, plainTextPassword);
    Assert.assertNotNull(actual);
    Assert.assertTrue(UserValidator.validate(actual));
  }

  @Test
  public void should_update_user_information() {
    final String username = RandomUtils.getRandomUsername(USERNAME_PREFIX);
    final String plainTextPassword = StringUtils.getMD5(username);
    final String hashedPassword = BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    final User user = new User(username, hashedPassword, FORENAME, SURNAME);

    AUTH_DB.saveUser(user);
    final User persisted = AUTH_DB.getUser(username, plainTextPassword);

    Assert.assertNotNull(persisted);
    Assert.assertTrue(UserValidator.validate(persisted));

    final String newPlainTextPassword = StringUtils.getMD5(username);
    final String newHashedPassword = BCrypt.hashpw(newPlainTextPassword, BCrypt.gensalt());
    final String newForename = "NeuerVorname";
    final String newSurname = "NeuerNachname";
    final User update = new User(username, newHashedPassword, newForename, newSurname);

    AUTH_DB.saveUser(update);
    final User updated = AUTH_DB.getUser(username, plainTextPassword);

    Assert.assertNotNull(updated);
    Assert.assertTrue(UserValidator.validate(updated));
    Assert.assertEquals(newHashedPassword, updated.getPassword());
    Assert.assertEquals(newForename, updated.getForename());
    Assert.assertEquals(newSurname, updated.getSurname());
  }

}
