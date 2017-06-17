package at.sheldor5.tr.web.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.URL;
import java.util.logging.Logger;

/**
 * @author Michael Palata
 * @date 01.06.2017
 */
public class Security implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(Security.class.getName());

  private static final String JAAS_ENVIRONMENT_VARIABLE = "java.security.auth.login.config";
  private static final String JAAS_CONFIG_FILE = "/WEB-INF/conf/jaas.config";

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    if(System.getProperty(JAAS_ENVIRONMENT_VARIABLE) == null) {
      try {
        final URL jaasConfigURL = sce.getServletContext().getResource(JAAS_CONFIG_FILE);
        if(jaasConfigURL == null) {
          throw new RuntimeException("Security");
        }
        final String jaasConfigFile = jaasConfigURL.getFile();
        System.setProperty(JAAS_ENVIRONMENT_VARIABLE, jaasConfigFile);
      } catch (final Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }
}
