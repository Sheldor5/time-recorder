package at.sheldor5.tr.core.auth;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.plugins.PluginManager;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.UuidUtils;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

  public void saveUser(final User user) {
    for (final AuthenticationPlugin plugin : plugins) {
      try {
        plugin.saveUser(user);
      } catch (final Exception e) {
        // ignore
      }
    }
  }

  public UserMapping getUserMapping(final String username, final String password) {
    UserMapping userMapping;
    User user;
    for (final AuthenticationPlugin plugin : plugins) {
      user = plugin.getUser(username, password);
      if (user != null) {
        userMapping = mapUser(user);
        if (userMapping != null) {
          userMapping.setUser(user);
          return userMapping;
        }
      }
    }
    return null;
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

  public UserMapping mapUser(final User user) {
    if (user == null) {
      LOGGER.severe("User is null");
      return null;
    }

    final UUID uuid = user.getUuid();
    if (uuid == null) {
      LOGGER.severe("User UUID is null");
      return null;
    }

    UserMapping userMapping;
    try (final UserMappingProvider userMappingProvider = new UserMappingProvider()) {
      userMapping = userMappingProvider.get(uuid);
      if (userMapping == null) {
        userMapping = new UserMapping(uuid);
        userMapping.setRole(Role.USER);
        userMappingProvider.save(userMapping);
      }
    }

    return userMapping;
  }

}
