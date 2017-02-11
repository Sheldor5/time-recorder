package at.sheldor5.tr.sdk.auth;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.UuidUtils;

import java.util.ArrayList;
import java.util.List;

public class DummyAuthentication implements AuthenticationPlugin {

  private static final String NAME = "dummy";

  @Override
  public String getName() {
    return NAME;
  }

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
  public void initialize() throws IllegalStateException {

  }

  @Override
  public void addUser(final User user, final String plainTextPassword) {
    final Credentials credentials = new Credentials(user, plainTextPassword);
    if (users.contains(credentials)) {
      throw new IllegalArgumentException("Username already in use");
    }
    user.setUuidBytes(UuidUtils.getRandomUuidBytes());
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