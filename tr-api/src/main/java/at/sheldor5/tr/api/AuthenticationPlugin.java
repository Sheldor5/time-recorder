package at.sheldor5.tr.api;

import at.sheldor5.tr.api.objects.User;

/**
 * Interface to serve username:password authentication.
 */
public interface AuthenticationPlugin {

  void addUser(final User user, final String plainTextPassword);

  /**
   * Try to authenticate and return {@link User} object
   * which holds all information about the authenticated user.
   *
   * @param username The user's login name.
   * @param password The user's password in plain text.
   * @return The user for this username and password.
   */
  User getUser(final String username, final String password);

}