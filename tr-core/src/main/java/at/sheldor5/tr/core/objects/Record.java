package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import at.sheldor5.tr.core.utils.TimeUtils;
import java.sql.Date;
import java.sql.Time;

/**
 * Java Bean for time records.
 */
public class Record implements Comparable<Record> {

  private int id;
  protected Date date;
  protected Time time;
  RecordType type;

  public Record(int id, final Date date, final Time time, final RecordType type) {
    this.id = id;
    this.date = TimeUtils.truncateTime(date);
    this.time = TimeUtils.truncateDate(time);
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

  public Date getDate() {
    return date;
  }

  protected void setDate(final Date date) {
    this.date = TimeUtils.truncateTime(date);
  }

  public Time getTime() {
    return time;
  }

  void setTime(final Time time) {
    this.time = TimeUtils.truncateDate(time);
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