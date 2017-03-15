package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Day;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RuleManager {

  private static final Logger LOGGER = Logger.getLogger(RuleManager.class.getName());

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

  public void load(final File rulePath) throws Exception {
    if (rulePath == null) {
      LOGGER.severe("Path is null");
      throw new FileNotFoundException("null");
    }
    if (!rulePath.exists()) {
      final File executionPath = new File("");
      LOGGER.severe("Path \"" + rulePath.getName() + "\" does not exist in: " + executionPath.getAbsoluteFile());
      throw new FileNotFoundException(rulePath.getName());
    }

    final File xsd = new File(this.getClass().getResource("/rules/rules.xsd").toURI());
    if (!xsd.exists()) {
      LOGGER.severe("Rule XSD file not found");
      throw new FileNotFoundException("/rules/rules.xsd");
    }

    final RuleLoader loader = new RuleLoader(xsd);
    for (final File xml : rulePath.listFiles()) {
      if (xml.getName().endsWith(".xml")) {
        LOGGER.fine("Validating rule file: " + xml.getName());
        if (loader.validateXML(xml)) {
          LOGGER.fine("Loading rules from: " + xml.getName());
          rules.addAll(loader.getRules(xml));
        } else {
          LOGGER.warning("Rule file is not valid: " + xml.getName());
        }
      }
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