package at.sheldor5.tr.sdk.auth;

import at.sheldor5.tr.api.AuthenticationPlugin;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyAuthentication implements AuthenticationPlugin {

  class Credentials {
    private final User user;
    private final String password;

    protected Credentials(final User user, final String password) {
      this.user = user;
      this.password = password;
    }

    @Override
    public boolean equals(final Object other) {
      if (other == null || !(other instanceof Credentials)) {
        return false;
      }
      final Credentials credentials = (Credentials) other;
      return user.equals(credentials.user) && password.equals(credentials.password);
    }
  }

  private final List<Credentials> users = new ArrayList<>();

  @Override
  public void addUser(final User user, final String plainTextPassword) {
    final Credentials credentials = new Credentials(user, plainTextPassword);
    if (users.contains(credentials)) {
      throw new IllegalArgumentException("Username already in use");
    }
    users.add(credentials);
  }

  @Override
  public User getUser(final String username, String plainTextPassword) {
    for (final Credentials credentials : users) {
      if (credentials.user.getUsername().equals(username) && credentials.password.equals(plainTextPassword)) {
        return credentials.user;
      }
    }
    return null;
  }
}