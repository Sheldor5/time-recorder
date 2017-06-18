package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Day;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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

  private final List<IRule> rules = new ArrayList<>();

  private RuleManager() {

  }

  public void addRule(IRule rule) {
    rules.add(rule);
  }

  private InputStream xsd = null;

  public void setXSD(final InputStream xsd) {
    this.xsd = xsd;
  }

  public boolean load(final InputStream xml) {
    if (xsd == null || xml == null) {
      return false;
    }

    try {
      final RuleLoader loader = new RuleLoader(xsd);
      rules.addAll(loader.getRules(xml));
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public boolean applies(final Day day) {
    for (final IRule rule : rules) {
      if (rule.applies(day)) {
        return true;
      }
    }
    return false;
  }

  public void apply(final Day day) {
    for (final IRule rule : rules) {
      rule.apply(day);
    }
  }
}