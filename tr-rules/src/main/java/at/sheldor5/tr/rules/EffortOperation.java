package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Day;

public class EffortOperation {

  final long seconds;
  final double multiplier;

  public EffortOperation(long millis, double multiplier) {
    this.seconds = millis;
    this.multiplier = multiplier;
  }

  protected boolean applies(final Day day) {
    return day.getSummary() > seconds;
  }

  public void apply(final Day day) {
    if (!applies(day)) {
      return;
    }

  }

}