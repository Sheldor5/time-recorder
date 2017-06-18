package at.sheldor5.tr.web.init;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.web.module.authentication.AuthenticationManager;
import at.sheldor5.tr.exporter.ExporterManager;
import at.sheldor5.tr.web.module.authentication.InternalAuthentication;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

public class Plugins implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(Plugins.class.getName());

  private final AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
  private final ExporterManager exporterManager = ExporterManager.getInstance();
  private final FastClasspathScanner classpathScanner = new FastClasspathScanner();

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    classpathScanner.overrideClassLoaders(servletContextEvent.getServletContext().getClassLoader());

    final String verbose = GlobalProperties.getProperty("plugins.verbose");

    if (verbose != null && "true".equals(verbose)) {
      classpathScanner.verbose();
    }

    // Authentication Plugins
    classpathScanner.matchClassesImplementing(AuthenticationPlugin.class, c -> {
      if (Modifier.isAbstract(c.getModifiers())) {
        return;
      }
      try {
        AuthenticationPlugin plugin = c.newInstance();
        if (plugin.getName() == null || plugin.getName().isEmpty()) {
          LOGGER.warning("Authentication plugin <" + c.getName() + "> failed to initialize: name is null or empty");
          return;
        }
        LOGGER.fine(String.format("Loaded Authentication Plugin: %s", plugin.getName()));
        authenticationManager.addPlugin(plugin);
      } catch (final Exception e) {
        LOGGER.warning("Authentication plugin <" + c.getName() + "> failed to initialize: " + e.getMessage());
      }
    });

    // Exporter Plugins
    classpathScanner.matchClassesImplementing(ExporterPlugin.class, c -> {
      if (Modifier.isAbstract(c.getModifiers())) {
        return;
      }
      try {
        ExporterPlugin plugin = c.newInstance();
        if (plugin.getName() == null || plugin.getName().isEmpty()) {
          LOGGER.warning("Exporter plugin <" + c.getName() + "> failed to initialize: name is null or empty");
          return;
        }
        LOGGER.fine(String.format("Loaded Exporter Plugin: %s", plugin.getName()));
        exporterManager.addPlugin(plugin);
      } catch (final Exception e) {
        LOGGER.warning("Exporter plugin <" + c.getName() + "> failed to initialize: " + e.getMessage());
      }
    });

    classpathScanner.scan();

    if (authenticationManager.getPlugins().size() == 0) {
      System.out.println("no authentication plugins found");
    }

    final String chain = GlobalProperties.getProperty("auth.chain");
    if (chain == null || chain.isEmpty()) {
      authenticationManager.sort(new String[]{InternalAuthentication.NAME});
    } else {
      authenticationManager.sort(chain.split(","));
    }

    authenticationManager.initialize();
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
