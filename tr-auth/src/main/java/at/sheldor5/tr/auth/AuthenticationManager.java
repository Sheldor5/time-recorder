package at.sheldor5.tr.auth;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.plugins.PluginManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AuthenticationManager implements PluginManager<AuthenticationPlugin> {

  private static final Logger LOGGER = Logger.getLogger(AuthenticationManager.class.getName());

  private static AuthenticationManager instance;

  public static AuthenticationManager getInstance() {
    if (instance == null) {
      instance = new AuthenticationManager();
    }
    return instance;
  }

  private final List<AuthenticationPlugin> plugins = new ArrayList<>();

  private boolean sorted = false;

  @Override
  public void initialize() throws IllegalStateException {
    for (final AuthenticationPlugin plugin : plugins) {
      plugin.initialize();
    }
  }

  public void addUser(final User user, final String plainTextPassword) {
    for (final AuthenticationPlugin plugin : plugins) {
      try {
        plugin.addUser(user, plainTextPassword);
      } catch (final Exception e) {
        // ignore
      }
    }
  }

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

  @Override
  public void addPlugin(final AuthenticationPlugin plugin) {
    if (sorted) {
      LOGGER.severe("Authentication plugins can not be added after the authentication chain was built");
      return;
    }
    plugins.add(plugin);
  }

  @Override
  public void addPlugins(final List<AuthenticationPlugin> plugins) {
    if (sorted) {
      LOGGER.severe("Authentication plugins can not be added after the authentication chain was built");
      return;
    }
    plugins.addAll(plugins);
  }

  @Override
  public AuthenticationPlugin getPlugin(final String name) {
    if (name == null || name.isEmpty()) {
      return null;
    }
    for (final AuthenticationPlugin plugin : plugins) {
      if (name.equals(plugin.getName())) {
        return plugin;
      }
    }
    return null;
  }

  @Override
  public List<AuthenticationPlugin> getPlugins() {
    return plugins;
  }

  public void sort(final String[] nameOrder) {
    final Map<String, AuthenticationPlugin> unsorted = new HashMap<>();
    for (final AuthenticationPlugin plugin : plugins) {
      unsorted.put(plugin.getName(), plugin);
    }
    plugins.clear();
    LOGGER.info("Authentication chain:");
    AuthenticationPlugin plugin;
    for (final String name : nameOrder) {
      plugin = unsorted.get(name);
      if (plugin == null) {
        continue;
      }
      plugins.add(plugin);
      LOGGER.info(name + ": " + plugin.getClass().getName());
    }
  }
}