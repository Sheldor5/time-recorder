package at.sheldor5.tr.core.authentication;

import at.sheldor5.tr.api.AuthenticationPlugin;
import at.sheldor5.tr.api.objects.User;

import java.util.LinkedList;
import java.util.List;

public class AuthenticationManager implements AuthenticationPlugin {

  private static AuthenticationManager instance;

  public static AuthenticationManager getInstance() {
    if (instance == null) {
      instance = new AuthenticationManager();
    }
    return instance;
  }

  private final List<AuthenticationPlugin> plugins = new LinkedList<>();

  public void addAuthenticationPlugin(final AuthenticationPlugin plugin) {
    plugins.add(plugin);
  }

  @Override
  public void initialize() throws IllegalStateException {
    
  }

  @Override
  public void addUser(final User user, final String plainTextPassword) {
    for (final AuthenticationPlugin plugin : plugins) {
      plugin.addUser(user, plainTextPassword);
    }
  }

  @Override
  public User getUser(final String username, final String password) {
    User user = null;
    for (final AuthenticationPlugin plugin : plugins) {
      user = plugin.getUser(username, password);
      if (user != null) {
        return user;
      }
    }
    return user;
  }
}