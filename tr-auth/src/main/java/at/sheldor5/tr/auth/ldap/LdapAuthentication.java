package at.sheldor5.tr.auth.ldap;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import java.util.Hashtable;
import java.util.UUID;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * LDAP Authentication Service Implementation.
 */
public class LdapAuthentication implements AuthenticationPlugin {

  private static final Logger LOGGER = Logger.getLogger(LdapAuthentication.class.getName());

  private static final String NAME = "tr-ldap";

  private static String PROVIDER_URL;
  private static String SEARCH_BASE;

  /**
   * Result attributes for LDAP queries.
   */
  private static final SearchControls CONTROLS = new SearchControls();

  static {
    CONTROLS.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String[] attrIDs = {"entryUUID", "givenname", "sn"};
    CONTROLS.setReturningAttributes(attrIDs);
  }

  /**
   * @see AuthenticationPlugin#initialize().
   */
  @Override
  public void initialize() throws IllegalStateException {
    PROVIDER_URL = "ldap://" + GlobalProperties.getProperty("ldap.host") + ":" + GlobalProperties.getProperty("ldap.port");
    SEARCH_BASE = "uid=%s," + GlobalProperties.getProperty("ldap.search.base");
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

  /**
   * @see AuthenticationPlugin#getUser(String, String).
   */
  @Override
  public User getUser(final String username, final String password) {

    LOGGER.fine("Trying to authenticate userMapping \"" + username + "\"");

    final LdapContext context = getUserLdapContext(username, password);

    if (context == null) {
      return null;
    }

    try {
      final String name = GlobalProperties.getProperty("ldap.search.base");
      final String filter = "uid=" + username;
      final NamingEnumeration<SearchResult> answer = context.search(name, filter, CONTROLS);
      if (answer.hasMore()) {
        Attributes attrs = answer.next().getAttributes();

        final UUID uuid = UUID.fromString((String) attrs.get("entryUUID").get());

        String forename;
        try {
          forename = attrs.get("givenname").get().toString();
        } catch (final NullPointerException npe) {
          forename = "";
        }

        String surname;
        try {
          surname = attrs.get("sn").get().toString();
        } catch (final NullPointerException npe) {
          surname = "";
        }

        if (!answer.hasMore()) {
          final User user = new User();
          user.setUuid(uuid);
          user.setUsername(username);
          user.setForename(forename);
          user.setSurname(surname);
          LOGGER.fine("Got userMapping \"" + forename + " " + surname + "\" with ID " + uuid);
          return user;
        }
        LOGGER.warning("Found multiple LDAP entries");
      }
    } catch (final Exception generalException) {
      LOGGER.severe(generalException.getMessage());
      return null;
    }
    return null;
  }

  protected static LdapContext getUserLdapContext(final String username, final String password) {
    LdapContext ctx = null;

    final String user = String.format(SEARCH_BASE, username);

    LOGGER.fine("Authenticating userMapping: " + user);

    try {
      final Hashtable<String, String> env = new Hashtable<>();
      env.put(Context.PROVIDER_URL, PROVIDER_URL);
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.SECURITY_AUTHENTICATION, "simple");
      env.put(Context.SECURITY_PRINCIPAL, user);
      env.put(Context.SECURITY_CREDENTIALS, password);
      env.put(Context.REFERRAL, "follow");

      ctx = new InitialLdapContext(env, null);

      LOGGER.fine("Successfully authenticated userMapping: " + user);
    } catch (final NamingException ne) {
      final String msg = ne.getMessage();
      if (!msg.contains("error code 49")) {
        LOGGER.warning(ne.getMessage());
      }
      LOGGER.fine("Authentication failed for userMapping: " + user);
    }

    return ctx;
  }

  @Override
  public String getName() {
    return NAME;
  }
}