package at.sheldor5.tr.core.authentication;

import at.sheldor5.tr.api.AuthenticationPlugin;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.persistence.DatabaseConnection;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.auth.db.DatabaseAuthentication;
import at.sheldor5.tr.auth.ldap.LdapAuthentication;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class AuthenticationManagerTest {

  private static final File PROPERTIES = new File("test.properties");

  private static final String FORENAME = "Test";
  private static final String SURNAME = "User";

  private static final String LDAP_USER = "testuser";
  private static final String LDAP_PASS = "test";

  private static final String DB_USER = "db_test";
  private static final String DB_PASS = "db_pass";
  private static final String DB_FORENAME = "Database";
  private static final String DB_SURNAME = "User";

  private static AuthenticationManager manager;

  private static AuthenticationPlugin db;
  private static AuthenticationPlugin ldap;

  @Before
  public void init() throws IOException {
    GlobalProperties.load(PROPERTIES);

    manager = AuthenticationManager.getInstance();
    Assume.assumeNotNull(manager);

    db = new DatabaseAuthentication(DatabaseConnection.getInstance());
    Assume.assumeNotNull(db);

    ldap = new LdapAuthentication();
    Assume.assumeNotNull(ldap);
    Assume.assumeNotNull(ldap.getUser("time-recorder", "password"));

    final User user = db.getUser(DB_USER, DB_PASS);
    if (user == null) {
      db.addUser(new User(DB_USER, DB_FORENAME, DB_SURNAME), DB_PASS);
    }

    Assume.assumeNotNull(db.getUser(DB_USER, DB_PASS));

    manager.addAuthenticationPlugin(db);
    manager.addAuthenticationPlugin(ldap);
  }

  @Test
  public void test_ldap() {
    final User user = manager.getUser(LDAP_USER, LDAP_PASS);

    Assert.assertNotNull(user);
    Assert.assertEquals(FORENAME, user.getForename());
    Assert.assertEquals(SURNAME, user.getSurname());
    Assert.assertNotNull(user.getUUID());
  }

  @Test
  public void test_db() {
    final User user = manager.getUser(DB_USER, DB_PASS);

    Assert.assertNotNull(user);
    Assert.assertEquals(DB_FORENAME, user.getForename());
    Assert.assertEquals(DB_SURNAME, user.getSurname());
    Assert.assertNotNull(user.getUUID());
  }
}