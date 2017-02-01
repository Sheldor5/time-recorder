package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SessionBuilder {

  public static List<Session> buildSessions(final List<Record> records) {
    final List<List<Record>> days = new ArrayList<>();

    List<Record> day = new ArrayList<>();

    LocalDate date = records.get(0).getDate();
    Record record = null;
    for (int i = 0; i < records.size();) {
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

    for (int i = 0; i < days.size(); i++) {
      final List<Record> list = days.get(i);
      for (int j = 0; j < list.size(); j++) {
        final Session session = new Session(j + 1);
        session.build(list.get(j++), list.get(j));
        sessions.add(session);
      }
    }

    return sessions;
  }

  public static Day buildDay(final List<Session> sessions) {
    if (sessions == null || sessions.size() < 1) {
      return null;
    }

    final LocalDate date = sessions.get(0).date;
    final Day day = new Day(date.getDayOfMonth());
    int i = 0;
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