package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import at.sheldor5.tr.core.utils.TimeUtils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Java Bean for time records.
 */
public class Record implements Comparable<Record> {

  private int id;
  private Date date;
  protected Time time;
  private RecordType type;

  public Record(int id, final Date date, final Time time, final RecordType type) {
    this.id = id;
    this.date = TimeUtils.truncateTime(date);
    this.time = TimeUtils.truncateDate(time);
    this.type = type;
  }

  public Record() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(final Date date) {
    this.date = TimeUtils.truncateTime(date);
  }

  public Time getTime() {
    return time;
  }

  public void setTime(final Time time) {
    this.time = TimeUtils.truncateDate(time);
  }

  public RecordType getType() {
    return type;
  }

  public void setType(final RecordType type) {
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