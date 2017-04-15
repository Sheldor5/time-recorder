package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.user.User;

public class UserValidator {

  public static boolean validate(final User user) {
    if (user == null) {
      return false;
    }
    final String username = user.getUsername();
    final String password = user.getPassword();
    return username != null && !username.isEmpty() && password != null && !password.isEmpty();
  }
}
