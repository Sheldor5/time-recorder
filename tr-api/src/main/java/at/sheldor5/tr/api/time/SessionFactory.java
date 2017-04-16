package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory class to build {@link Session}s.
 */
public class SessionFactory {

  /**
   * Builds a list of sessions based on a list of records.
   * In the first step the records are split up by their date and
   * stored in a list which represents a list of days on which the
   * sessions took place. In the second step all records of a day are
   * converted to sessions. If a day starts with a CHECKOUT record,
   * a CHECKIN record ({@link LocalTime#MIN}) will be added to the
   * beginning of the day. If a day ends with a CHECKIN record,
   * a CHECKOUT record ({@link LocalTime#MAX}) will be added
   * to the end of the day. This ensures that no day depends on
   * another day.
   *
   * @param records The list of records to build the sessions based on.
   * @return A list of sessions.
   */
  public static List<Session> buildSessions(final List<Record> records) {
    final List<List<Record>> days = new ArrayList<>();

    List<Record> day = new ArrayList<>();

    LocalDate date = records.get(0).getDate();
    Record record = null;
    for (int i = 0; i < records.size(); ) {
      // day start
      if (record == null) {
        record = records.get(i++);
      }

      // day has to start with CHECKIN
      if (record.type == RecordType.CHECKOUT) {
        day.add(new Record(date, LocalTime.MIN, RecordType.CHECKIN));
      }

      // store each record
      do {
        day.add(record);
        if (i >= records.size()) {
          break;
        }
        record = records.get(i++);
      } while (date.equals(record.date));

      // day has to end with CHECKOUT
      if (day.get(day.size() - 1).type == RecordType.CHECKIN) {
        day.add(new Record(date, LocalTime.MAX, RecordType.CHECKOUT));
      }

      // check if count of records is even
      if (day.size() % 2 == 1) {
        throw new IllegalStateException("Uneven records for this day");
      }

      days.add(day);
      if (i < records.size()) {
        day = new ArrayList<>();
        date = record.date;
      } else if (i == records.size() && record.type == RecordType.CHECKIN) {
        day = new ArrayList<>();
        date = record.date;
        if (record.type == RecordType.CHECKIN) {
          day.add(record);
          day.add(new Record(date, LocalTime.MAX, RecordType.CHECKOUT));
        } else {
          day.add(new Record(date, LocalTime.MIN, RecordType.CHECKIN));
          day.add(record);
        }
        days.add(day);
      }
    }

    final List<Session> sessions = new ArrayList<>();

    for (List<Record> list : days) {
      for (int j = 0; j < list.size(); j++) {
        final Session session = new Session(list.get(j++), list.get(j));
        sessions.add(session);
      }
    }

    return sessions;
  }
}
