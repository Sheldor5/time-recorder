package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import java.util.List;

public class Day extends Container<Session> {

  public Day(final LocalDate date) {
    super(date);
  }

  @Override
  protected boolean validateItem(final Session item) {
    return this.date.equals(item.date);
  }

  @Override
  public final synchronized long getSummary() {

    final List<Session> sessions = this.getItems();

    long sum = 0;

    for (final Session session : sessions) {
      sum += session.getSummary();
    }

    return sum;
  }

  @Override
  public final synchronized long getValuedSummary() {

    final List<Session> sessions = this.getItems();

    long sum = 0;

    for (final Session session : sessions) {
      sum += session.getValuedSummary();
    }

    return sum;
  }

  public static Day buildDay(final List<Session> sessions) {
    if (sessions == null || sessions.size() == 0) {
      return null;
    }

    final LocalDate date = sessions.get(0).date;
    final Day day = new Day(date);
    for (final Session session : sessions) {
      if (session.date.equals(date)) {
        day.addItem(session);
      } else {
        throw new IllegalStateException("List contains a session which belongs to another day");
      }
    }
    return day;
  }
}