package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.util.List;

/**
 * Factory class to build {@link Day}s.
 */
public class DayFactory {

  /**
   * Build day from {@link Session}s.
   *
   * @param sessions List of sessions of this day.
   * @return The day with the sessions.
   */
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
