package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.api.objects.Day;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 23.01.2017.
 */
public class EffortOperation {

  final long millis;
  final double multiplier;

  public EffortOperation(long millis, double multiplier) {
    this.millis = millis;
    this.multiplier = multiplier;
  }

  public boolean applies(final Day day) {
    return day.getSummary() > millis;
  }

}