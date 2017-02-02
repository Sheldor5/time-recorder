package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Java Bean for time records.
 */
public class Record implements Comparable<Record> {

  private int id = -1;
  protected LocalDate date;
  protected LocalTime time;
  protected RecordType type;

  public Record() {

  }

  public Record(final LocalDate date, final LocalTime time, final RecordType type) {
    setDate(date);
    setTime(time);
    setType(type);
  }

  public Record(int id, final LocalDate date, final LocalTime time, final RecordType type) {
    this(date, time, type);
    setId(id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    if (id < 0) {
      throw new IllegalArgumentException("ID is negative");
    }
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(final LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("Date is null");
    }
    this.date = date;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(final LocalTime time) {
    if (time == null) {
      throw new IllegalArgumentException("Time is null");
    }
    this.time = time;
  }

  public RecordType getType() {
    return type;
  }

  public void setType(final RecordType type) {
    if (type == null) {
      throw new IllegalArgumentException("Type is null");
    }
    this.type = type;
  }

  public boolean isValid() {
    return date != null && time != null && type != null;
  }

  /**
   * Compare this record to another record.
   *
   * @param other The record to compare.
   * @return Negative if this record is before the compared record,
   * positive if this record is after the compared record,
   * zero if date and time are equal.
   */
  @Override
  public int compareTo(final Record other) {
    int result = date.compareTo(other.date);
    if (result == 0) {
      return time.compareTo(other.time);
    }
    return result;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || !(other instanceof Record)) {
      return false;
    }
    final Record record = (Record) other;
    return date.equals(record.date) && time.equals(record.time) && type.equals(record.type);
  }

}