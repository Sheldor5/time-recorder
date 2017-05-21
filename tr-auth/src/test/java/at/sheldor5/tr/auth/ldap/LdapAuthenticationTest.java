package at.sheldor5.tr.auth.ldap;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.naming.ldap.LdapContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LdapAuthenticationTest {

  private static final File PROPERTIES = new File("ldap.properties");

  private static final UUID USER_UUID = UUID.fromString("22823c12-80f6-1036-99a6-bb4e2dfc20ee");
  private static final String USERNAME = "testuser";
  private static final String FORENAME = "Test";
  private static final String SURNAME = "User";
  private static final String PASSWORD = "test";
  private static final AuthenticationPlugin LDAP = new LdapAuthentication();

  @Before
  public void init() throws IOException {
    GlobalProperties.load(PROPERTIES);
    LDAP.initialize();
  }

  @Test
  public void test_valid_user_context() {
    final LdapContext context = LdapAuthentication.getUserLdapContext(USERNAME, PASSWORD);
    Assert.assertNotNull("LDAP connection should return userMapping context", context);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void test_add_user() {
    LDAP.saveUser(null);
  }

  @Test
  public void test_invalid_user_context() {
    final LdapContext context = LdapAuthentication.getUserLdapContext(USERNAME, "invalid");
    Assert.assertNull(context);
  }

  @Test
  public void test_valid_credentials() throws Exception {
    final User user = LDAP.getUser(USERNAME, PASSWORD);
    Assert.assertNotNull(user);
    Assert.assertEquals(USER_UUID, user.getUuid());
    Assert.assertEquals(USERNAME, user.getUsername());
    Assert.assertEquals(FORENAME, user.getForename());
    Assert.assertEquals(SURNAME, user.getSurname());
  }

  @Test
  public void test_invalid_credentials() throws Exception {
    AuthenticationPlugin ldap = new LdapAuthentication();
    final User user = ldap.getUser(USERNAME, "invalid");
    Assert.assertNull(user);
  }

}