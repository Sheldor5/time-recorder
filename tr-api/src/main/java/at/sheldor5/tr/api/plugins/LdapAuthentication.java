package at.sheldor5.tr.api.plugins;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;
import java.util.UUID;
import java.util.logging.Logger;

abstract public class LdapAuthentication implements AuthenticationPlugin {

  protected static final Logger LOGGER = Logger.getLogger(LdapAuthentication.class.getName());

  protected static final String NAME = "tr-ldap";

  protected String PROVIDER_URL;
  protected String SEARCH_BASE;

  /**
   * Result attributes for LDAP queries.
   */
  protected static final SearchControls CONTROLS = new SearchControls();

  /**
   * @see AuthenticationPlugin#initialize().
   */
  @Override
  abstract public void initialize() throws IllegalStateException;

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

    LOGGER.fine("Trying to authenticate user \"" + username + "\"");

    final LdapContext context = getUserLdapContext(username, password);

    if (context == null) {
      return null;
    }

    try {
      final String name = GlobalProperties.getProperty("ldap.search.base");
      final String filter = getFilter(username);
      final NamingEnumeration<SearchResult> answer = context.search(name, filter, CONTROLS);
      if (answer.hasMore()) {
        Attributes attrs = answer.next().getAttributes();

        final UUID uuid = getUUID(attrs);

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
          LOGGER.fine("Got user \"" + forename + " " + surname + "\" with ID " + uuid);
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

  protected abstract String getFilter(String username);

  abstract protected UUID getUUID(Attributes attrs) throws NamingException;

  protected LdapContext getUserLdapContext(final String username, final String password) {
    LdapContext ctx = null;
    final String user = createUserString(username);
    LOGGER.fine("Authenticating user: " + user);
    try {
      final Hashtable<String, String> env = setupEnvironment(user, password);
      ctx = new InitialLdapContext(env, null);
      LOGGER.fine("Successfully authenticated user: " + user);
    } catch (final NamingException ne) {
      final String msg = ne.getMessage();
      if (!msg.contains("error code 49")) {
        LOGGER.warning(ne.getMessage());
      }
      LOGGER.fine("Authentication failed for user: " + user);
    }

    return ctx;
  }

  abstract protected String createUserString(String username);

  abstract protected Hashtable<String, String> setupEnvironment(String user, String password);

  @Override
  public String getName() {
    return NAME;
  }
}
