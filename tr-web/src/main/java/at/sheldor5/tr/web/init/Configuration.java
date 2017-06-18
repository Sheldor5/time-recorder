package at.sheldor5.tr.web.init;

import at.sheldor5.tr.api.utils.GlobalProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Configuration implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

  private static final String PROPERTIES = "time-recorder/time-recorder.properties";
  private static final String DEFAULT = "time-recorder/time-recorder-default.properties";

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    try {
      GlobalProperties.load(classLoader.getResourceAsStream(DEFAULT));
      final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES);
      if (in == null) {
        logNoConfigProvided();
      } else {
        GlobalProperties.load(in);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load configuration file \"" + PROPERTIES);
    }
  }

  private static void logNoConfigProvided() {
    final String TEST_LINE = "# TEST MODE # TEST MODE # TEST MODE # TEST MODE # TEST MODE # TEST MODE #"
        + " TEST MODE # TEST MODE # TEST MODE # TEST MODE # TEST MODE # TEST MODE # TEST MODE #"
        + " TEST MODE # TEST MODE # TEST MODE # TEST MODE # TEST MODE # TEST MODE # TEST MODE #\n";

    final StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("\n\n");

    stringBuilder.append(TEST_LINE);
    stringBuilder.append(TEST_LINE);
    stringBuilder.append(TEST_LINE);

    stringBuilder.append("\n");

    stringBuilder.append("No external configuration file provided, using internal one in test mode\n");
    stringBuilder.append("To provide a configuration file follow these steps:\n");
    stringBuilder.append("\t1. create the following directories:\n");
    stringBuilder.append("\t\t${catalina.base}/shared/classes\n");
    stringBuilder.append("\t\t${catalina.base}/shared/lib\n");
    stringBuilder.append("\t2. modify \"${catalina.base}/conf/catalina.properties\" and define:\n");
    stringBuilder.append("\t\tshared.loader=\"${catalina.base}/shared/classes\",\"${catalina.base}/shared/lib/*.jar\n");
    stringBuilder.append("\t3. add a file named \"time-recorder.properties\" to the directory \"${catalina.base}/shared/classes/time-recorder/\"\n");
    stringBuilder.append("\t4. configure the application by modifying \"time-recorder.properties\"\n");
    stringBuilder.append("\t5. add the JDBC JAR of your database provider to the classpath (put the JAR either into \"${catalina.base}/lib/\" or \"${catalina.base}/shared/lib/\"\n");

    stringBuilder.append("\n");

    stringBuilder.append(TEST_LINE);
    stringBuilder.append(TEST_LINE);
    stringBuilder.append(TEST_LINE);

    LOGGER.warning(stringBuilder.toString());
    stringBuilder.setLength(0);
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    // do nothing
  }
}
