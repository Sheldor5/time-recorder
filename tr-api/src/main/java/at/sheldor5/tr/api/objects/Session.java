package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Session extends Container<Record> {

  private LocalTime start;
  private LocalTime end;
  protected double multiplier;
  private long duration;

  protected Session(final LocalDate date) {
    super(date);
  }

  @Override
  protected boolean validateItem(final Record item) {
    return true;
  }

  public Session(final Record start, final Record end) throws IllegalArgumentException {
    this(start.getDate(), start, end, 1.0D);
  }

  public Session(final LocalDate date, final Record start, final Record end) throws IllegalArgumentException {
    this(date, start, end, 1.0D);
  }

  public Session(final LocalDate date, final Record start, final Record end, double multiplier) throws IllegalArgumentException {
    super(date);
    if (start == null || end == null) {
      throw new NullPointerException("Session has no start or end: null");
    }
    if (!start.isValid() || !end.isValid()) {
      throw new IllegalArgumentException("Session start or end is invalid");
    }
    this.duration = start.time.until(end.time, GlobalConfiguration.MEASURE_UNIT);
    if (this.duration < 0) {
      throw new IllegalArgumentException("Session starts after its end");
    }
    if (this.duration == 0 || start.time.equals(end.time)) {
      throw new IllegalArgumentException("Session starts and ends at the same time");
    }
    if (start.type == RecordType.CHECKOUT || end.type == RecordType.CHECKIN) {
      throw new IllegalArgumentException("Session starts with CHECKOUT or ends with CHECKIN");
    }
    if (!start.date.equals(end.date)) {
      throw new IllegalArgumentException("Session starts and ends on different days");
    }
    this.start = start.time;
    this.end = end.time;
    this.duration = this.start.until(this.end, GlobalConfiguration.MEASURE_UNIT);
    this.multiplier = multiplier;
  }

  public double getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(double multiplier) {
    this.multiplier = multiplier;
  }

  public LocalTime getStart() {
    return start;
  }

  public LocalTime getEnd() {
    return end;
  }

  public boolean contains(final LocalTime time) {
    return time.isAfter(start) && time.isBefore(end);
  }

  public Session split(final LocalTime time) {
    if (time == null || !contains(time)) {
      return null;
    }
    // create new object
    final Session session = new Session(date);
    session.start = time;
    session.end = end;
    session.duration = time.until(end, GlobalConfiguration.MEASURE_UNIT);
    session.multiplier = multiplier;
    // update this object
    end = time;
    duration = start.until(time, GlobalConfiguration.MEASURE_UNIT);
    // return new object
    return session;
  }

  @Override
  public final void addItem(final Record record) {
    throw new UnsupportedOperationException("Adding records to a session is not permitted, use constructor instead to initialize a session");
  }

  @Override
  public final List<Record> getItems() {
    return Arrays.asList(new Record(date, start, RecordType.CHECKIN), new Record(date, end, RecordType.CHECKOUT));
  }

  @Override
  public String toString() {
    return date + ": " + start + " - " + end;
  }

  @Override
  public long getSummary() {
    return duration;
  }

  @Override
  public long getValuedSummary() {
    return (long) (duration * multiplier);
  }

  @Override
  public final int compareTo(final Container other) {
    if (other == null) {
      return Integer.MAX_VALUE;
    }
    if (other instanceof Session) {
      return this.start.compareTo(((Session) other).start);
    } else {
      return super.compareTo(other);
    }
  }

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
        final Session session = new Session(date, list.get(j++), list.get(j));
        sessions.add(session);
      }
    }

    return sessions;
  }
}