package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.core.objects.Session;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 23.01.2017.
 */
public class Before extends TimeOperation {

  public Before(final LocalTime time, double multiplier, final List<String> days) {
    super(time, multiplier, days);
  }

  @Override
  public Session split(final Session session) {
    final Session secondPart = session.split(time);
    session.setMultiplier(multiplier);
    return secondPart;
  }
}