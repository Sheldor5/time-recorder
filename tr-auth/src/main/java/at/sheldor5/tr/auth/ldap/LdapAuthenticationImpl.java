package at.sheldor5.tr.auth.ldap;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;

import java.util.Hashtable;
import java.util.UUID;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

/**
 * LDAP Authentication Service Implementation.
 */
public class LdapAuthenticationImpl extends at.sheldor5.tr.api.plugins.LdapAuthentication {

  /**
   * @see AuthenticationPlugin#initialize().
   */
  @Override
  public void initialize() throws IllegalStateException {
    PROVIDER_URL = "ldap://" + GlobalProperties.getProperty("ldap.host") + ":" + GlobalProperties.getProperty("ldap.port");
    SEARCH_BASE = "uid=%s," + GlobalProperties.getProperty("ldap.search.base");
    CONTROLS.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String[] attrIDs = {"entryUUID", "givenname", "sn"};
    CONTROLS.setReturningAttributes(attrIDs);
    LOGGER.fine("Using Url: " + PROVIDER_URL);
    LOGGER.fine("Using Search Base: " + SEARCH_BASE);
  }

  /**
   * @see AuthenticationPlugin#saveUser(User).
   */
  @Override
  public void saveUser(final User user) {
    throw new UnsupportedOperationException("Currently not supported");
  }

  @Override
  protected String getFilter(String username) {
    return "uid=" + username;
  }

  @Override
  protected UUID getUUID(Attributes attrs) throws NamingException {
    return UUID.fromString((String) attrs.get("entryUUID").get());
  }

  @Override
  protected String createUserString(String username) {
    return String.format(SEARCH_BASE, username);
  }

  @Override
  protected Hashtable<String, String> setupEnvironment(String user, String password) {
    Hashtable<String, String> env = new Hashtable<>();
    env.put(Context.PROVIDER_URL, PROVIDER_URL);
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    env.put(Context.SECURITY_PRINCIPAL, user);
    env.put(Context.SECURITY_CREDENTIALS, password);
    env.put(Context.REFERRAL, "follow");
    return env;
  }
}