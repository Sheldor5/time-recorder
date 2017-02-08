package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.api.objects.Day;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RuleManager {

  private static final Logger LOGGER = LogManager.getLogger(RuleManager.class);

  private static RuleManager instance;

  public static RuleManager getInstance() {
    if (instance == null) {
      instance = new RuleManager();
    }
    return instance;
  }

  private final List<Rule> rules = new ArrayList<>();

  private RuleManager() {

  }

  public void load(final File rulePath) {
    if (rulePath == null) {
      LOGGER.error("Path is null");
      return;
    }
    if (!rulePath.exists()) {
      final File executionPath = new File("");
      LOGGER.error("Path \"{}\" does not exist in: {}", rulePath.getName(), executionPath.getAbsoluteFile());
      return;
    }
    try {
      final File xsd = new File(this.getClass().getResource("/rules/rules.xsd").toURI());
      if (!xsd.exists()) {
        LOGGER.fatal("Rule XSD file not found in: {}");
      }
      final RuleLoader loader = new RuleLoader(xsd);
      for (final File xml : rulePath.listFiles()) {
        if (xml.getName().endsWith(".xml")) {
          LOGGER.debug("Validating rule file: {}", xml.getName());
          if (loader.validateXML(xml)) {
            LOGGER.debug("Loading rules from: {}", xml.getName());
            rules.addAll(loader.getRules(xml));
          } else {
            LOGGER.warn("Rule file is not valid: {}", xml.getName());
          }
        }
      }
    } catch (final Exception e) {
      LOGGER.fatal(e.getMessage());
    }
  }

  public boolean applies(final Day day) {
    for (final Rule rule : rules) {
      if (rule.applies(day)) {
        return true;
      }
    }
    return false;
  }

  public void apply(final Day day) {
    for (final Rule rule : rules) {
      rule.apply(day);
    }
  }
}