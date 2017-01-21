package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Java Bean for time records.
 */
public class Record implements Comparable<Record> {

  private Timestamp timestamp;
  private RecordType type;
  private int id;

  public Record(int id, final Timestamp timestamp, final RecordType type) {
    this.id = id;
    this.timestamp = timestamp;
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

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public RecordType getType() {
    return type;
  }

  public void setType(final RecordType type) {
    this.type = type;
  }

  public int compareTo(final Record other) {
    return timestamp.compareTo(other.timestamp);
  }

  @Override
  public boolean equals(final Object other) {
    if (other == null || !(other instanceof Record)) {
      return false;
    }
    final Record record = (Record) other;
    long diff = timestamp.getTime() - record.timestamp.getTime();
    if (diff < 0) {
      diff *= -1;
    }
    return diff < 1000 && type == record.type && id == record.id;
  }

  public static Timestamp now() {
    return new Timestamp(System.currentTimeMillis());
  }

  public static Timestamp getStartOfDay(final Timestamp timestamp) {
    LocalDateTime beginning = LocalDateTime.of(timestamp.toLocalDateTime().toLocalDate(), LocalTime.MIN);
    return Timestamp.valueOf(beginning);
  }

  public static Timestamp getEndOfDay(final Timestamp timestamp) {
    LocalDateTime beginning = LocalDateTime.of(timestamp.toLocalDateTime().toLocalDate(), LocalTime.MAX);
    return Timestamp.valueOf(beginning);
  }

}