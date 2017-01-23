package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import at.sheldor5.tr.core.utils.TimeUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class Session {

  private LocalDate date;
  private LocalTime begin;
  private LocalTime end;
  private long duration;
  private double multiplier = 1.0D;

  private Session() {

  }

  public Session(double multiplier, final Record begin, final Record end) throws IllegalArgumentException {
    if (begin == null || end == null) {
      throw new NullPointerException("Beginning or end is null");
    }
    this.duration = begin.time.until(end.time, ChronoUnit.MILLIS);
    if (this.duration < 0 || begin.type != RecordType.CHECKIN || end.type != RecordType.CHECKOUT) {
      throw new IllegalArgumentException("The beginning of the period is after the end");
    }
    if (begin.type == RecordType.CHECKOUT || end.type == RecordType.CHECKIN) {
      throw new IllegalArgumentException("Period begins with CHECKOUT or ends with CHECKIN");
    }
    if (!begin.date.equals(end.date)) {
      throw new IllegalArgumentException("Begin and end date were not on the same day");
    }
    this.date = begin.getDate();
    this.begin = begin.time;
    this.end = end.time;
    this.multiplier = multiplier;
  }

  public double getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(double multiplier) {
    this.multiplier = multiplier;
  }

  public LocalDate getDate() {
    return date;
  }

  public LocalTime getBegin() {
    return begin;
  }

  public LocalTime getEnd() {
    return end;
  }

  public long getDuration() {
    return duration;
  }

  public long getValuedDuration() {
    return (long) (duration * multiplier);
  }

  public boolean contains(final LocalTime time) {
    if (time == null) {
      return false;
    }
    return time.isAfter(begin) && time.isBefore(end);
  }

  public Session split(final LocalTime time) {
    if (time == null || !contains(time)) {
      return null;
    }
    // create new object
    final Session session = new Session();
    session.date = date;
    session.begin = time;
    session.end = end;
    session.duration = time.until(end, ChronoUnit.MILLIS);
    // update this object
    end = time;
    duration = begin.until(time, ChronoUnit.MILLIS);
    // return new object
    return session;
  }

  @Override
  public String toString() {
    return date + ": " + begin + " - " + end;
  }
}