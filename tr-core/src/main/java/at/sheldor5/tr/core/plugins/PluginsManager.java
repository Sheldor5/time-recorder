package at.sheldor5.tr.core.plugins;

import at.sheldor5.tr.api.AuthenticationPlugin;
import at.sheldor5.tr.api.Exporter;
import at.sheldor5.tr.api.ExporterPlugin;
import at.sheldor5.tr.api.utils.Authentication;
import at.sheldor5.tr.core.utils.RuntimeUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public enum  PluginsManager {

  INSTANCE;

  private static final Logger LOGGER = Logger.getLogger(PluginsManager.class.getName());

  private final Map<Authentication, AuthenticationPlugin> authenticationPlugins = new HashMap<>();
  private final Map<Exporter, ExporterPlugin> exporterPlugins = new HashMap<>();

  public void load(final String pluginsPath) throws IOException, IllegalAccessException, InstantiationException {
    loadAuthenticationPlugins(pluginsPath);
    loadExporterPlugins(pluginsPath);
  }

  private void loadAuthenticationPlugins(final String pluginsPath) throws IOException, IllegalAccessException, InstantiationException {
    final List<Class<AuthenticationPlugin>> plugins = RuntimeUtils.getClassesImplementing(pluginsPath, AuthenticationPlugin.class);
    for (final Class<AuthenticationPlugin> plugin : plugins) {
      for (final Annotation annotation : plugin.getAnnotations()) {
        if (annotation instanceof Authentication) {
          final Authentication authentication = (Authentication) annotation;
          if ("abstract".equals(authentication.name())) {
            LOGGER.warning("Authentication plugin <" + plugin.getName() + "> does not annotate <" + Authentication.class.getName() + ">");
            continue;
          }
          final AuthenticationPlugin authenticationPlugin = plugin.newInstance();
          try {
            authenticationPlugin.initialize();
            authenticationPlugins.put(authentication, authenticationPlugin);
          } catch (final IllegalStateException ise) {
            LOGGER.warning("Authentication plugin <" + plugin.getName() + "> failed to initialize: " + ise.getMessage());
          }
        }
      }
    }
  }

  private void loadExporterPlugins(final String pluginsPath) throws IOException, IllegalAccessException, InstantiationException {
    final List<Class<ExporterPlugin>> plugins = RuntimeUtils.getClassesImplementing(pluginsPath, ExporterPlugin.class);
    for (final Class<ExporterPlugin> plugin : plugins) {
      for (final Annotation annotation : plugin.getAnnotations()) {
        if (annotation instanceof Exporter) {
          final Exporter exporter = (Exporter) annotation;
          if ("abstract".equals(exporter.name())) {
            LOGGER.warning("Exporter plugin <" + plugin.getName() + "> does not annotate <" + Exporter.class.getName() + ">");
            continue;
          }
          exporterPlugins.put(exporter, plugin.newInstance());
        }
      }
    }
  }

  public ExporterPlugin getExporter(final String name) {
    for (final Exporter exporter : exporterPlugins.keySet()) {
      if (exporter.name().equals(name)) {
        return exporterPlugins.get(exporter);
      }
    }
    return null;
  }

  public ExporterPlugin getAuthentication(final String name) {
    for (final Authentication authentication : authenticationPlugins.keySet()) {
      if (authentication.name().equals(name)) {
        return exporterPlugins.get(authentication);
      }
    }
    return null;
  }
}