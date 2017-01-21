package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.core.objects.Record;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class Rule {

  private double multiplier;

  public Rule() {

  }

  public boolean applies(final Record record) {
    return false;
  }

  public double getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(double multiplier) {
    this.multiplier = multiplier;
  }

}
