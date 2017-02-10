package at.sheldor5.tr.api.plugins;

import at.sheldor5.tr.api.objects.User;

/**
 * Interface to serve username:password authentication.
 */
public interface AuthenticationPlugin extends Plugin {

  /**
   * Used for initialization.
   *
   * @throws IllegalStateException If the initialization fails and
   *        this plugin can not be used for further operations.
   */
  void initialize() throws IllegalStateException;

  /**
   * Add a new user to this plugin's backend user directory.
   *
   * @param user The user to add.
   * @param plainTextPassword The unencrypted password.
   * @throws UnsupportedOperationException If this method is not supported by this plugin.
   */
  void addUser(final User user, final String plainTextPassword) throws UnsupportedOperationException;

  /**
   * Try to authenticate and return an {@link User} object
   * which holds all information about the authenticated user.
   *
   * @param username The user's login name.
   * @param password The user's password in plain text.
   * @return The successfully authenticated user.
   */
  User getUser(final String username, final String password);

}