package at.sheldor5.tr.web.init;

import at.sheldor5.tr.api.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Configuration implements ServletContextListener {

  private static final File PROPERTIES = new File("time-recorder.properties");

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    Logger.getGlobal().setLevel(Level.FINE);
    // load configuration file
    try {
      GlobalProperties.load(PROPERTIES);
    } catch (IOException e) {
      final File executionPath = new File("");
      throw new RuntimeException("Failed to load configuration file \"" + PROPERTIES.getName() + "\" in: " + executionPath.getAbsolutePath());
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    // do nothing
  }
}
