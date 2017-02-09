package at.sheldor5.tr.web.init;

import at.sheldor5.tr.auth.db.DatabaseAuthentication;
import at.sheldor5.tr.core.authentication.AuthenticationManager;
import at.sheldor5.tr.core.plugins.PluginsManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Plugins implements ServletContextListener {

  private static final String PLUGINS_PATH = "plugins";

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      PluginsManager.INSTANCE.load(PLUGINS_PATH);
    } catch (final Exception e) {
      // throw new RuntimeException("Unable to load plugins");
    }
    AuthenticationManager.getInstance().addAuthenticationPlugin(new DatabaseAuthentication());
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}