package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Java Bean for time records.
 */
public class Record implements Comparable<Record> {

  private int id;
  protected LocalDate date;
  protected LocalTime time;
  RecordType type;

  public Record(int id, final LocalDate date, final LocalTime time, final RecordType type) {
    this.id = id;
    this.date = date;
    this.time = time;
    this.type = type;
  }

  Record() {

  }

  int getId() {
    return id;
  }

  void setId(int id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  protected void setDate(final LocalDate date) {
    this.date = date;
  }

  public LocalTime getTime() {
    return time;
  }

  void setTime(final LocalTime time) {
    this.time = time;
  }

  public RecordType getType() {
    return type;
  }

  void setType(final RecordType type) {
    this.type = type;
  }

  @Override
  public int compareTo(final Record other) {
    int diff = date.compareTo(other.date);
    if (diff == 0) {
      return time.compareTo(time);
    }
    return diff;
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || !(other instanceof Record)) {
      return false;
    }
    final Record record = (Record) other;
    return date.equals(record.date) && time.equals(record.time);
  }

}