package at.sheldor5.tr.auth.ldap;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.api.utils.UuidUtils;

import java.util.Hashtable;
import java.util.UUID;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

/**
 * LDAP Active Directory Authentication Service Implementation.
 */
public class LdapADAuthenticationImpl extends at.sheldor5.tr.api.plugins.LdapAuthentication {

  /**
   * @see AuthenticationPlugin#initialize().
   */
  @Override
  public void initialize() throws IllegalStateException {
    PROVIDER_URL = "ldap://" + GlobalProperties.getProperty("ldap.host") + ":" + GlobalProperties.getProperty("ldap.port");
    SEARCH_BASE = "uid=%s," + GlobalProperties.getProperty("ldap.search.base");
    CONTROLS.setSearchScope(SearchControls.SUBTREE_SCOPE);
    CONTROLS.setReturningAttributes(new String[]{"objectGUID", "givenname", "sn"});
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
    return "sAMAccountName=" + username;
  }

  @Override
  protected UUID getUUID(Attributes attrs) throws NamingException {
    return UuidUtils.objectGUIDbytesToUUID((byte[]) attrs.get("objectGUID").get());
  }

  @Override
  protected String createUserString(String username) {
    String[] parts = SEARCH_BASE.split(",dc=");
    return username + "@" + parts[1] + "." + parts[2];
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
    env.put("java.naming.ldap.attributes.binary", "objectGUID");
    return env;
  }
}