package at.sheldor5.wtr.records;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class Record implements Comparable<Record> {
  //LocalDate today = LocalDate.now(ZoneId.of("Europe/Berlin"));

  public static Timestamp getStartOfDay(final Timestamp timestamp) {
    LocalDateTime beginning = LocalDateTime.of(timestamp.toLocalDateTime().toLocalDate(), LocalTime.MIN);
    return Timestamp.valueOf(beginning);
  }

  public static Timestamp getEndOfDay(final Timestamp timestamp) {
    LocalDateTime beginning = LocalDateTime.of(timestamp.toLocalDateTime().toLocalDate(), LocalTime.MAX);
    return Timestamp.valueOf(beginning);
  }

  private final Timestamp timestamp_;
  private final RecordType type_;

  public Record(final Timestamp timestamp, final RecordType type) {
    timestamp_ = timestamp;
    type_ = type;
  }

  public Timestamp getTimestamp() {
    return timestamp_;
  }

  public RecordType getType() {
    return type_;
  }

  public int compareTo(final Record other) {
    return this.timestamp_.compareTo(other.timestamp_);
  }

}