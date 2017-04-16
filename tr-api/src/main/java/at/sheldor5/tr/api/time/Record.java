package at.sheldor5.tr.api.time;

import at.sheldor5.tr.api.user.UserMapping;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents a point in time.
 *
 * @author Michael Palata
 * @since 1.0.0
 */
public class Record implements Comparable<Record> {

  private int id;
  private UserMapping user;
  protected LocalDate date;
  protected LocalTime time;
  protected RecordType type;

  /**
   * Empty constructor.
   */
  public Record() {

  }

  /**
   * Default constructor.
   *
   * @param date the date of this record.
   * @param time the time of this record.
   * @param type the type of this record.
   */
  public Record(final LocalDate date, final LocalTime time, final RecordType type) {
    this.date = date;
    this.time = time;
    this.type = type;
  }

  /**
   * Returns the ID of this record.
   *
   * @return the ID of this record.
   */
  public int getId() {
    return id;
  }

  /**
   * Sets the ID for this record.
   *
   * @param id the ID for this record.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns the {@link UserMapping} of this record.
   *
   * @return the {@link UserMapping} of this record.
   */
  public UserMapping getUser() {
    return user;
  }

  /**
   * Sets the {@link UserMapping} for this record.
   *
   * @param user the {@link UserMapping} for this record.
   */
  public void setUser(final UserMapping user) {
    this.user = user;
  }

  /**
   * Returns the date of this record.
   *
   * @return the date of this record.
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Sets the date for this record.
   *
   * @param date the date for this record.
   */
  public void setDate(final LocalDate date) {
    this.date = date;
  }

  /**
   * Returns the time of this record.
   *
   * @return the time of this record.
   */
  public LocalTime getTime() {
    return time;
  }

  /**
   * Sets the time of this record.
   *
   * @param time the time for this record.
   */
  public void setTime(final LocalTime time) {
    this.time = time;
  }

  /**
   * Returns the {@link RecordType} of this record.
   *
   * @return the {@link RecordType} of this record.
   */
  public RecordType getType() {
    return type;
  }

  /**
   * Sets the {@link RecordType} for this record.
   *
   * @param type the {@link RecordType} for this record.
   */
  public void setType(final RecordType type) {
    this.type = type;
  }

  /**
   * Validates this record by checking if all fields are set.
   *
   * @return {@code true} if all fields are not null, false otherwise.
   */
  public boolean isValid() {
    return date != null && time != null && type != null;
  }

  /**
   * Compares this record to another record.
   * Support for sorting a collection of {@link Record}s.
   *
   * @param other the record to compare to.
   * @return      negative if this record appears before the {@code other} record in the time line,
   *              positive if this record appears after the {@code other} record in the time line,
   *              zero if date and time are equal.
   */
  @Override
  public int compareTo(final Record other) {
    if (date == null || time == null || other.date == null || other.time == null) {
      return -1;
    }
    int result = date.compareTo(other.date);
    if (result == 0) {
      return time.compareTo(other.time);
    }
    return result;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the object to compare to.
   * @return    true if this record and the {@code other} record
   *            have equal date, time and type, false otherwise.
   */
  @Override
  public boolean equals(final Object obj) {
    if (super.equals(obj)) {
      return true;
    }
    if (obj == null || !(obj instanceof Record)) {
      return false;
    }
    final Record record = (Record) obj;
    return date.equals(record.date) && time.equals(record.time) && type.equals(record.type);
  }

  /**
   * Returns a human readable string representation of this record.
   * Format: "{date} {time} {type}".
   * Example: "2017-12-31 13:45 CHECKIN"
   *
   * @return string representation of this record.
   */
  @Override
  public String toString() {
    return String.format("%s %s %s", date, time, type);
  }

}
