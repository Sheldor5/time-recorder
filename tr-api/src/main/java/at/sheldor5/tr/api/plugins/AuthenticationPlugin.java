package at.sheldor5.tr.api.plugins;

import at.sheldor5.tr.api.user.User;

/**
 * Interface to serve username:password authentication.
 */
public interface AuthenticationPlugin extends Plugin {

  /**
   * Used for initialization.
   *
   * @throws IllegalStateException if the initialization fails and
   *                               this plugin can not be used for further operations.
   */
  void initialize() throws IllegalStateException;

  /**
   * Add a new userMapping or update an existing userMapping to this plugin's backend userMapping directory.
   *
   * @param user                           the userMapping to add orupdate.
   * @throws UnsupportedOperationException if this method is not supported by this plugin.
   */
  void saveUser(final User user) throws UnsupportedOperationException;

  /**
   * Try to authenticate and return an {@link User} object
   * which holds all information about the authenticated userMapping.
   *
   * @param username the userMapping's login name.
   * @param password the userMapping's password in plain text.
   * @return         the successfully authenticated userMapping, null otherwise.
   */
  User getUser(final String username, final String password);

}
