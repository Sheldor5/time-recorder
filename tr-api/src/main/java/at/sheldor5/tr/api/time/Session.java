package at.sheldor5.tr.api.time;

import at.sheldor5.tr.api.utils.GlobalConfiguration;
import at.sheldor5.tr.api.utils.TimeUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a period in the time line.
 * Each {@link Session} has a {@link Record} representing the session's start
 * and a {@link Record} representing the session's end.
 * A session has also a multiplier to value the time between the start and end time.
 *
 * @author Michael Palata
 * @since 1.0.0
 */
public class Session extends Container<Record> {

  protected Record start;
  protected Record end;
  protected double multiplier = 1.0D;

  /**
   * Default Constructor.
   *
   * @param date The date on which this session took place.
   */
  public Session(final LocalDate date) {
    super(date);
  }

  /**
   * Default Constructor.
   *
   * @param date The date on which this session took place.
   */
  public Session(final Record start, final Record end) {
    super(neverNull(start));
    setStart(start);
    setEnd(end);
  }

  /**
   * Default Constructor.
   *
   * @param date The date on which this session took place.
   */
  public Session(final Record start, final Record end, double multiplier) {
    super(neverNull(start));
    setStart(start);
    setEnd(end);
    setMultiplier(multiplier);
  }

  /**
   * Constructor for given records.
   *
   * @param start The record on which this session starts.
   * @param end   The record on which this session ends.
   * @throws IllegalArgumentException If this session can not be built based on the given records.
   */
  public Session(final LocalDate date, final Record start, final Record end) throws IllegalArgumentException {
    this(date, start, end, 1.0D);
  }

  /**
   * Constructor for given records and multiplier.
   *
   * @param start      The record on which this session starts.
   * @param end        The record on which this session ends.
   * @param multiplier The multiplier to value this sessions duration.
   * @throws IllegalArgumentException If this session can not be built based on the given records.
   */
  public Session(final LocalDate date, final Record start, final Record end, double multiplier)
          throws IllegalArgumentException {
    super(date);
    if (end == null) {
      throw new NullPointerException("Session has no start or end: null");
    }
    if (!start.date.equals(end.date)) {
      throw new IllegalArgumentException("Session starts and ends on different days");
    }
    if (start.type == RecordType.CHECKOUT || end.type == RecordType.CHECKIN) {
      throw new IllegalArgumentException("Session starts with CHECKOUT or ends with CHECKIN");
    }

    setStart(start);
    setEnd(end);

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
  public Record getStart() {
    return start;
  }

  /**
   * Setter for the start time.
   *
   * @param record The record on which this session starts.
   * @throws IllegalArgumentException If the start time is after this session's end time.
   */
  public void setStart(final Record record) {
    if (record == null || record.type == null || record.type == RecordType.CHECKOUT) {
      throw new IllegalArgumentException("Invalid record");
    }
    if (date != null && !date.equals(record.date)) {
      throw new IllegalArgumentException("Session starts on another day");
    }
    if (end == null || end.time.isBefore(record.time)) {
      start = record;
    } else {
      throw new IllegalArgumentException("Session starts after its end");
    }
  }

  /**
   * Getter for the start time.
   *
   * @return The time on which this session ends.
   */
  public Record getEnd() {
    return end;
  }

  /**
   * Setter for the end time.
   *
   * @param record The time on which this session ends.
   * @throws IllegalArgumentException If the end time is before this session's start time.
   */
  public void setEnd(final Record record) {
    if (record == null || record.type == null || record.type == RecordType.CHECKIN) {
      throw new IllegalArgumentException("Invalid record");
    }
    if (date != null && !date.equals(record.date)) {
      throw new IllegalArgumentException("Session starts on another day");
    }
    if (start == null || start.time.isBefore(record.time)) {
      end = record;
    } else {
      throw new IllegalArgumentException("Session ends before its start");
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
    return time != null && time.isAfter(start.time) && time.isBefore(end.time);
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
    final Record otherStart = new Record(date, time, RecordType.CHECKIN);
    final Session session = new Session(date);
    session.start = otherStart;
    session.end = end;
    session.multiplier = multiplier;

    // update this object
    final Record thisEnd = new Record(date, time, RecordType.CHECKOUT);
    end = thisEnd;

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
    return Arrays.asList(start, end);
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
            start.time,
            end.time,
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
    if (start == null || start.time == null || end == null || end.time == null) {
      return -1;
    }
    return start.time.until(end.time, GlobalConfiguration.MEASURE_UNIT);
  }

  /**
   * Get the valued duration between the start and end time of this session in the configured time unit.
   *
   * @return The valued duration of this session.
   */
  @Override
  public long getValuedSummary() {
    if (start == null || start.time == null || end == null || end.time == null) {
      return -1;
    }
    return (long) (multiplier * start.time.until(end.time, GlobalConfiguration.MEASURE_UNIT));
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
