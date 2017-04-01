package at.sheldor5.tr.api.time;

import at.sheldor5.tr.api.utils.GlobalConfiguration;
import at.sheldor5.tr.api.utils.TimeUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a period in the time line.
 * Each <tt>{@link Session}</tt> has a start and end time
 * and a multiplier to value the time between the start
 * and end time.
 *
 * @author Michael Palata
 * @since 1.0.0
 */
public class Session extends Container<Record> {

  protected LocalTime start;
  protected LocalTime end;
  protected double multiplier;

  /**
   * Default Constructor.
   *
   * @param date The date on which this session took place.
   */
  protected Session(final LocalDate date) {
    super(date);
  }

  /**
   * Constructor for given records.
   *
   * @param start The record on which this session starts.
   * @param end   The record on which this session ends.
   * @throws IllegalArgumentException If this session can not be built based on the given records.
   */
  public Session(final Record start, final Record end) throws IllegalArgumentException {
    this(start, end, 1.0D);
  }

  /**
   * Constructor for given records and multiplier.
   *
   * @param start      The record on which this session starts.
   * @param end        The record on which this session ends.
   * @param multiplier The multiplier to value this sessions duration.
   * @throws IllegalArgumentException If this session can not be built based on the given records.
   */
  public Session(final Record start, final Record end, double multiplier)
          throws IllegalArgumentException {
    super(neverNull(start));

    if (end == null) {
      throw new NullPointerException("Session has no start or end: null");
    }
    if (!start.date.equals(end.date)) {
      throw new IllegalArgumentException("Session starts and ends on different days");
    }
    if (start.type == RecordType.CHECKOUT || end.type == RecordType.CHECKIN) {
      throw new IllegalArgumentException("Session starts with CHECKOUT or ends with CHECKIN");
    }

    setStart(start.time);
    setEnd(end.time);

    long duration = start.time.until(end.time, GlobalConfiguration.MEASURE_UNIT);

    if (duration == 0 || start.time.equals(end.time)) {
      throw new IllegalArgumentException("Session starts and ends at the same time");
    }

    setMultiplier(multiplier);
  }

  public double getMultiplier() {
    return multiplier;
  }

  /**
   * Setter for the multiplier.
   *
   * @param multiplier The multiplier to value this sessions duration.
   */
  public void setMultiplier(double multiplier) {
    this.multiplier = multiplier;
  }

  /**
   * Getter for the multiplier.
   *
   * @return The multiplier of this session.
   */
  public LocalTime getStart() {
    return start;
  }

  /**
   * Setter for the start time.
   *
   * @param time The time on which this session starts.
   * @throws IllegalArgumentException If the start time is after this session's end time.
   */
  public void setStart(final LocalTime time) {
    if (time == null) {
      throw new IllegalArgumentException("Time is null");
    }
    if (end == null || end.isAfter(time)) {
      start = time;
    } else {
      throw new IllegalArgumentException("Session starts after it ends");
    }
  }

  /**
   * Getter for the start time.
   *
   * @return The time on which this session ends.
   */
  public LocalTime getEnd() {
    return end;
  }

  /**
   * Setter for the end time.
   *
   * @param time The time on which this session ends.
   * @throws IllegalArgumentException If the end time is before this session's start time.
   */
  public void setEnd(final LocalTime time) {
    if (time == null) {
      throw new IllegalArgumentException("Time is null");
    }
    if (start == null || start.isBefore(time)) {
      end = time;
    } else {
      throw new IllegalArgumentException("Session ends before it starts");
    }
  }

  /**
   * Check if a time point is within this session's start and end time.
   *
   * @param time The time to check.
   * @return True if the given time is after this session's start time and
   *         before this session's end time, false otherwise.
   */
  public boolean contains(final LocalTime time) {
    return time.isAfter(start) && time.isBefore(end);
  }

  /**
   * Split this session by the given time point.
   * This will update this session to end at the given time and will return
   * a new session starting at the given time and ending at this session's end.
   *
   * @param time The time point at which this session should be split.
   * @return The following session which results at the split, or null if
   *         the given time point is null or this session does not contain
   *         ({@link Session#contains(LocalTime)}) the given time point.
   */
  public Session split(final LocalTime time) {
    if (time == null || !contains(time)) {
      return null;
    }
    // create session for the second part of the split
    final Session session = new Session(date);
    session.start = time;
    session.end = end;
    session.multiplier = multiplier;
    // update this object
    end = time;
    // return following session
    return session;
  }

  /**
   * Does nothing except throwing an {@link UnsupportedOperationException}.
   * Also see {@link Container#addItem(Comparable)}.
   *
   * @param record Null.
   */
  @Override
  public final void addItem(final Record record) {
    throw new UnsupportedOperationException("Adding records to a session is not permitted, "
            + "use the constructor instead to initialize a session");
  }

  /**
   * See {@link Container#validateItem(Comparable)}.
   */
  @Override
  protected boolean validateItem(final Record item) {
    return true;
  }

  /**
   * See {@link Container#getItems()}.
   * Returns a list only containing 2 Records representing this sessions start and end.
   * @return
   */
  @Override
  public final List<Record> getItems() {
    return Arrays.asList(new Record(date, start, RecordType.CHECKIN), new Record(date, end, RecordType.CHECKOUT));
  }

  /**
   * Get a string representation of this session.
   *
   * @return The string representing this session.
   */
  @Override
  public String toString() {
    return String.format("%s: %s - %s = %s (%d%% = %s)",
            date,
            start,
            end,
            TimeUtils.getHumanReadableSummary(getSummary()),
            (long) (multiplier * 100),
            TimeUtils.getHumanReadableSummary(getValuedSummary()));
  }

  /**
   * Get the duration between the start and end time of this session in the configured time unit.
   *
   * @return The duration of this session.
   */
  @Override
  public long getSummary() {
    if (start == null || end == null) {
      return -1;
    }
    return start.until(end, GlobalConfiguration.MEASURE_UNIT);
  }

  /**
   * Get the valued duration between the start and end time of this session in the configured time unit.
   *
   * @return The valued duration of this session.
   */
  @Override
  public long getValuedSummary() {
    if (start == null || end == null) {
      return -1;
    }
    return (long) (start.until(end, GlobalConfiguration.MEASURE_UNIT) * multiplier);
  }

  /**
   * Compare this session to another object.
   *
   * @param other The container to compare.
   * @return Negative if this session starts before the other session or the other session is null,
   *         positive if this session starts after the other session, zero if
   *         the sessions start at the same time.
   */
  @Override
  public final int compareTo(final Container other) {
    if (other == null) {
      return -1;
    }
    if (other instanceof Session) {
      return this.start.compareTo(((Session) other).start);
    } else {
      return super.compareTo(other);
    }
  }

  /**
   * Builds a list of sessions based on a list of records.
   * In the first step the records are split up by their date and
   * stored in a list which represents a list of days on which the
   * sessions are. In the second step all records of a day are
   * converted to sessions. If a day starts with a CHECKOUT record,
   * a CHECKIN record ({@link LocalTime#MIN}) will be added to the
   * beginning of the day. If a day ends with a CHECKIN record,
   * a CHECKOUT record ({@link LocalTime#MAX}) will be added
   * to the end of the day. This ensures that there is no
   * invalid session in the returned list.
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

  /**
   * Ensure that a record is not null, needed for super(record.date).
   *
   * @param record The record to test.
   * @return The date of the given record.
   * @throws NullPointerException If the record is null.
   */
  private static LocalDate neverNull(final Record record) {
    if (record == null) {
      throw new NullPointerException("Session has no start or end: null");
    }
    return record.date;
  }
}
