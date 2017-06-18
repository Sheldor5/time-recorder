package at.sheldor5.tr.web.init;

import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.rules.Holiday;
import at.sheldor5.tr.rules.RuleManager;

import java.io.InputStream;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Rules implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(Rules.class.getName());

  private static final String RULES_XSD = "rules/rules.xsd";
  private static final String RULES_XML = "time-recorder/rules.xml";
  private static final String TEST_XML = "time-recorder/test.xml";

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    final RuleManager ruleManager = RuleManager.getInstance();

    if ("false".equals(GlobalProperties.getProperty("system.rules"))) {
      return;
    }

    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    try {
      final InputStream xsd = classLoader.getResourceAsStream(RULES_XSD);

      String xmlFile;
      if (GlobalProperties.getBoolean("system.test.data")) {
        xmlFile = TEST_XML;
      } else {
        final String custom = GlobalProperties.getProperty("system.rules.file");
        xmlFile = custom == null ? RULES_XML : String.format("time-recorder/%s.xml", custom);
      }

      final InputStream xml = classLoader.getResourceAsStream(xmlFile);

      ruleManager.setXSD(xsd);

      ruleManager.load(xml);
      //ruleManager.addRule(new Holiday());
    } catch (final Exception e) {
      throw new RuntimeException(String.format("Failed to load rules: %s", e.getMessage()));
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
