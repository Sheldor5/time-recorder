package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.core.objects.Session;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 23.01.2017.
 */
public abstract class TimeOperation {

  protected final LocalTime time;
  protected final double multiplier;
  protected final List<String> days = new ArrayList<>();

  public TimeOperation(final LocalTime time, double multiplier, final List<String> days) {
    if (time == null) {
      throw new NullPointerException("Time is null");
    }
    this.time = time;
    this.multiplier = multiplier;
    this.days.addAll(days);
  }

  public boolean applies(final Session session) {
    return session.contains(time);
  }

  public abstract Session split(final Session session);

}