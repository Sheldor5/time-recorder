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
   * Add a new user or update an existing user to this plugin's backend user directory.
   *
   * @param user                           the user to add orupdate.
   * @throws UnsupportedOperationException if this method is not supported by this plugin.
   */
  void saveUser(final User user) throws UnsupportedOperationException;

  /**
   * Try to authenticate and return an {@link User} object
   * which holds all information about the authenticated user.
   *
   * @param username the user's login name.
   * @param password the user's password in plain text.
   * @return         the successfully authenticated user, null otherwise.
   */
  User getUser(final String username, final String password);

}
