package at.sheldor5.tr.web.init;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.core.auth.AuthenticationManager;
import at.sheldor5.tr.core.utils.RuntimeUtils;
import at.sheldor5.tr.exporter.ExporterManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Plugins implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(Plugins.class.getName());

  private static final String PLUGINS_PATH = "plugins";
  private String pluginsPath;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    pluginsPath = GlobalProperties.getProperty("plugins.directory");
    if (pluginsPath == null || pluginsPath.isEmpty()) {
      LOGGER.info("Plugins directory is not configured");
      return;
    }
    loadAuthenticationPlugins();
    loadExporterPlugins();
    AuthenticationManager.getInstance().initialize();
  }

  private void loadAuthenticationPlugins() {
    final AuthenticationManager manager = AuthenticationManager.getInstance();

    List<Class<AuthenticationPlugin>> plugins;
    try {
      plugins = RuntimeUtils.getClassesImplementing(pluginsPath, AuthenticationPlugin.class);
    } catch (final IOException ioe) {
      LOGGER.warning(ioe.getMessage());
      plugins = new ArrayList<>();
    }

    AuthenticationPlugin plugin;
    for (final Class<AuthenticationPlugin> clazz : plugins) {
      try {
        plugin = clazz.newInstance();
        manager.addPlugin(plugin);
      } catch (final Exception e) {
        LOGGER.warning("Authentication plugin <" + clazz.getName() + "> failed to initialize: " + e.getMessage());
      }
    }

    if (manager.getPlugins().size() == 0) {
      System.out.println("no authentication plugins found");
      // TODO
    }

    final String chain = GlobalProperties.getProperty("auth.chain");
    if (chain == null || chain.isEmpty()) {
      manager.sort(new String[] { "tr-db" });
    } else {
      manager.sort(chain.split(","));
    }
  }

  private void loadExporterPlugins() {
    final ExporterManager manager = ExporterManager.getInstance();

    final List<Class<ExporterPlugin>> plugins;
    try {
      plugins = RuntimeUtils.getClassesImplementing(pluginsPath, ExporterPlugin.class);
    } catch (final IOException ioe) {
      LOGGER.warning(ioe.getMessage());
      return;
    }

    ExporterPlugin plugin;
    for (final Class<ExporterPlugin> clazz : plugins) {
      try {
        plugin = clazz.newInstance();
        manager.addPlugin(plugin);
      } catch (final Exception e) {
        LOGGER.warning("Authentication plugin <" + clazz.getName() + "> failed to initialize: " + e.getMessage());
      }
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}