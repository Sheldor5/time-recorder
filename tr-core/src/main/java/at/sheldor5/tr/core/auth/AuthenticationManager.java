package at.sheldor5.tr.core.auth;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.plugins.PluginManager;
import at.sheldor5.tr.api.utils.UuidUtils;

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

  private static final String INSERT_USER_MAPPING = "INSERT INTO [user_mapping] ([uuid]) VALUES (?)";
  private static final String SELECT_MAPPED_USER = "SELECT [pk_user_id] FROM [user_mapping] WHERE [uuid] = ?";

  private static AuthenticationManager instance;

  public static AuthenticationManager getInstance() {
    if (instance == null) {
      instance = new AuthenticationManager();
    }
    return instance;
  }

  private DataSource dataSource;

  private final List<AuthenticationPlugin> plugins = new ArrayList<>();

  private boolean sorted = false;

  @Override
  public void initialize() throws IllegalStateException {
    for (final AuthenticationPlugin plugin : plugins) {
      plugin.initialize();
    }
  }

  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
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
    User user;
    for (final AuthenticationPlugin plugin : plugins) {
      user = plugin.getUser(username, password);
      if (user != null) {
        mapUser(user);
        if (user.getId() < 1) {
          addUserMapping(user);
        }
        if (user.getId() > 0) {
          return user;
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

  public void addUserMapping(final User user) {
    if (user == null) {
      LOGGER.severe("User is null");
      return;
    }

    final UUID uuid = user.getUuid();

    if (uuid == null) {
      LOGGER.severe("UUID is null");
      return;
    }

    int id = 0;
    try (final Connection connection = dataSource.getConnection()) {

      try (final PreparedStatement statement = connection.prepareStatement(INSERT_USER_MAPPING, Statement.RETURN_GENERATED_KEYS)) {

        statement.setBytes(1, UuidUtils.getBytes(uuid));

        if (statement.executeUpdate() != 0) {

          try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {

            if (generatedKeys.next()) {
              id = generatedKeys.getInt(1);
              user.setId(id);
              LOGGER.fine("Successfully mapped UUID " + uuid + " to User ID " + id);
            }

          } catch (final SQLException sqle) {
            LOGGER.severe(sqle.getMessage());
          }

        } else {
          LOGGER.severe("Could not map user, insertion failed");
        }

      } catch (final SQLException sqle) {
        LOGGER.severe(sqle.getMessage());
      }

      if (id > 0) {
        commit(connection);
      } else {
        LOGGER.severe("Could not map user, no ID obtained");
        rollback(connection);
      }

    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
    }
  }

  public void mapUser(final User user) {
    if (user == null) {
      LOGGER.severe("User is null");
      return;
    }

    final byte uuid_bytes[] = user.getUuidBytes();
    if (uuid_bytes == null || uuid_bytes.length != 16) {
      LOGGER.severe("User UUID is invalid");
      return;
    }

    int id = 0;
    try (final Connection connection = dataSource.getConnection()) {
      try (final PreparedStatement statement = connection.prepareStatement(SELECT_MAPPED_USER)) {
        statement.setBytes(1, uuid_bytes);

        try (final ResultSet result = statement.executeQuery()) {
          if (result.next()) {
            id = result.getInt("pk_user_id");
            LOGGER.fine(String.format("Found mapped ID %s for UUID %s", id, user.getUuid()));
          }
          if (result.next()) {
            // user not unique
            id = 0;
            LOGGER.severe("Multiple user mappings found with UUID " + user.getUuid());
          }
        } catch (final SQLException sqle) {
          LOGGER.severe(sqle.getMessage());
        }
      } catch (final SQLException sqle) {
        LOGGER.severe(sqle.getMessage());
      }

      if (id > 0) {
        user.setId(id);
      } else {
        LOGGER.fine("No user mapping found for UUID " + user.getUuid());
      }
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
    }
  }

  private void commit(final Connection connection) throws SQLException {
    if (!connection.getAutoCommit()) {
      connection.commit();
    }
  }

  private void rollback(final Connection connection) throws SQLException {
    if (!connection.getAutoCommit()) {
      connection.rollback();
    }
  }

}