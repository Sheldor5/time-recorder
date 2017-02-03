package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.api.objects.Session;
import java.time.LocalTime;

public class After extends TimeOperation {

  public After(final LocalTime time, double multiplier, final Integer... days) {
    super(time, multiplier, days);
    if (LocalTime.MAX.equals(time)) {
      throw new IllegalArgumentException("Rule never applies");
    }
  }

  @Override
  public boolean applies(final LocalTime time) {
    return this.time.isBefore(time);
  }

  @Override
  public boolean applies(final Session session) {
    return applies(session.getDate()) && applies(session.getEnd());
  }

  @Override
  public Session split(final Session session) {
    if (applies(session)) {
      if (this.time.isBefore(session.getStart()) || this.time.equals(session.getStart())) {
        update(session);
        return null;
      }
      final Session secondPart = session.split(time);
      secondPart.setMultiplier(multiplier);
      return secondPart;
    }
    return null;
  }

}