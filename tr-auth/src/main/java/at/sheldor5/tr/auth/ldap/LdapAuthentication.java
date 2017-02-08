package at.sheldor5.tr.auth.ldap;

import at.sheldor5.tr.api.AuthenticationPlugin;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import java.util.Hashtable;
import java.util.UUID;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LDAP Authentication Service Implementation.
 */
public class LdapAuthentication implements AuthenticationPlugin {

  private static final Logger LOGGER = LogManager.getLogger(LdapAuthentication.class);

  /**
   * Result attributes for LDAP queries.
   */
  private static final SearchControls CONTROLS = new SearchControls();

  static {
    CONTROLS.setSearchScope(SearchControls.SUBTREE_SCOPE);
    String[] attrIDs = {"entryUUID", "givenname", "sn"};
    CONTROLS.setReturningAttributes(attrIDs);
  }

  @Override
  public void addUser(final User user, final String plainTextPassword) {
    throw new UnsupportedOperationException("Currently not supported");
  }

  /**
   * @see AuthenticationPlugin#getUser(String, String).
   */
  public User getUser(final String username, final String password) {

    LOGGER.debug("Trying to authenticate user \"" + username + "\"");

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
          LOGGER.debug("Got user \"" + forename + " " + surname + "\" with ID " + uuid);
          return new User(uuid, username, forename, surname);
        }
        LOGGER.error("Found multiple LDAP entries");
      }
    } catch (final Exception generalException) {
      LOGGER.error(generalException.getMessage());
      return null;
    }
    return null;
  }

  protected static LdapContext getUserLdapContext(final String username, final String password) {
    LdapContext ctx = null;

    final String user = "uid=" + username + "," + GlobalProperties.getProperty("ldap.search.base");

    try {
      final Hashtable<String, String> env = new Hashtable<>();
      env.put(Context.PROVIDER_URL, "ldap://" + GlobalProperties.getProperty("ldap.host") + ":" + GlobalProperties.getProperty("ldap.port"));
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.SECURITY_AUTHENTICATION, "simple");
      env.put(Context.SECURITY_PRINCIPAL, user);
      env.put(Context.SECURITY_CREDENTIALS, password);
      env.put(Context.REFERRAL, "follow");

      ctx = new InitialLdapContext(env, null);

      LOGGER.debug("Successfully authenticated user: " + user);
    } catch (final NamingException ne) {
      final String msg = ne.getMessage();
      if (!msg.contains("error code 49")) {
        LOGGER.error(ne.getMessage());
      }

      LOGGER.debug("Authentication failed for user: " + user);
    }

    return ctx;
  }
}