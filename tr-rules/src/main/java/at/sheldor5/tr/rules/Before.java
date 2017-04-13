package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Session;
import java.time.LocalTime;

public class Before extends TimeOperation {

  public Before(final LocalTime time, double multiplier, final Integer... days) {
    super(time, multiplier, days);
    if (LocalTime.MIN.equals(time)) {
      throw new IllegalArgumentException("Rule never applies");
    }
  }

  @Override
  public boolean applies(final LocalTime time) {
    return this.time.isAfter(time);
  }

  @Override
  public boolean applies(final Session session) {
    return applies(session.getDate()) && applies(session.getStart().getTime());
  }

  @Override
  public Session split(final Session session) {
    if (applies(session)) {
      if (this.time.isAfter(session.getEnd().getTime()) || this.time.equals(session.getStart().getTime())) {
        update(session);
        return null;
      }
      final Session secondPart = session.split(time);
      session.setMultiplier(multiplier);
      return secondPart;
    }
    return null;
  }

}