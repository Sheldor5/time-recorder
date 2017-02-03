package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Session;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class TimeOperation {

  protected final LocalTime time;
  protected final double multiplier;
  protected final boolean days[] = new boolean[7];

  public TimeOperation(final LocalTime time, double multiplier, final Integer... days) {
    if (time == null) {
      throw new NullPointerException("Time is null");
    }
    this.time = time;
    this.multiplier = multiplier;
    for (int i = 0; i < 7 && i < days.length; i++) {
      if (days[i] < 1 || days[i] > 7) {
        throw new IllegalArgumentException("Invalid day of month: " + days[i]);
      }
      this.days[days[i]-1] = true;
    }
  }

  public boolean applies(final Day day) {
    return applies(day.getDate());
  }

  public boolean applies(final LocalDate date) {
    return days[date.getDayOfWeek().getValue() - 1];
  }

  public void update(final Session session) {
    session.setMultiplier(multiplier);
  }

  public abstract boolean applies(final LocalTime time);

  public abstract boolean applies(final Session session);

  public abstract Session split(final Session session);

}