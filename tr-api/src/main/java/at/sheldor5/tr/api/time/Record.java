package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents a point in time.
 *
 * @author Michael Palata
 * @since 1.0.0
 */
public class Record implements Comparable<Record> {

  private int id = -1;
  private int userId = -1;
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
   * @param date The date of this record.
   * @param time The time of this record.
   * @param type The type of this record.
   */
  public Record(final LocalDate date, final LocalTime time, final RecordType type) {
    setDate(date);
    setTime(time);
    setRecordType(type);
  }

  /**
   * Constructor to also specify record ID.
   *
   * @param id   The ID of this record.
   * @param date The date of this record.
   * @param time The time of this record.
   * @param type The type of this record.
   */
  public Record(int id, final LocalDate date, final LocalTime time, final RecordType type) {
    this(date, time, type);
    setId(id);
  }

  /**
   * Getter for the ID.
   *
   * @return The ID of this record.
   */
  public int getId() {
    return id;
  }

  /**
   * Setter for the ID.
   *
   * @param id The ID of this record.
   */
  public void setId(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("ID must not be negative");
    }
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * Getter for the date.
   *
   * @return The date of this record.
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Setter for the date.
   *
   * @param date The date of this record.
   */
  public void setDate(final LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("Date must not be null");
    }
    this.date = date;
  }

  /**
   * Getter for the time.
   *
   * @return The time of this record.
   */
  public LocalTime getTime() {
    return time;
  }

  /**
   * Setter for the time.
   *
   * @param time The time of this record.
   */
  public void setTime(final LocalTime time) {
    if (time == null) {
      throw new IllegalArgumentException("Time must not be null");
    }
    this.time = time;
  }

  /**
   * Getter for the type.
   *
   * @return The type of this record.
   */
  public RecordType getRecordType() {
    return type;
  }

  /**
   * Setter for the type.
   *
   * @param type The type of this record.
   */
  public void setRecordType(final RecordType type) {
    if (type == null) {
      throw new IllegalArgumentException("Type must not be null");
    }
    this.type = type;
  }

  /**
   * Getter for the type.
   *
   * @return The type of this record.
   */
  public boolean getType() {
    return type.getBoolean();
  }

  /**
   * Setter for the type.
   *
   * @param type The type of this record.
   */
  public void setType(boolean type) {
    this.type = RecordType.getType(type);
  }

  /**
   * Check if this record is valid.
   * Simply checks if all fields are set.
   *
   * @return True if all fields are set, false if any field is null.
   */
  public boolean isValid() {
    return date != null && time != null && type != null;
  }

  /**
   * Compare this record to another record.
   *
   * @param other The record to compare.
   * @return Negative if this record is before the compared record in the time line,
   *            positive if this record is after the compared record in the time line,
   *            zero if date and time are equal.
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
   * @param other The object to compare.
   * @return True if this record and the other record
   *            have equal date, time and type, false otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other == null || !(other instanceof Record)) {
      return false;
    }
    final Record record = (Record) other;
    return date.equals(record.date) && time.equals(record.time) && type.equals(record.type);
  }

  @Override
  public String toString() {
    return String.format("%s %s %s", date, time, type);
  }

}