package at.sheldor5.tr.web.init;

import at.sheldor5.tr.core.rules.RuleManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class Rules implements ServletContextListener {

  private static final File RULES_FOLDER = new File("rules");

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    final RuleManager ruleManager = RuleManager.getInstance();
    try {
      ruleManager.load(RULES_FOLDER);
    } catch (final Exception e) {
      final File executionPath = new File("");
      //throw new RuntimeException("Failed to load rules from folder \"" + rules + "\" in: " + executionPath.getAbsolutePath());
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}