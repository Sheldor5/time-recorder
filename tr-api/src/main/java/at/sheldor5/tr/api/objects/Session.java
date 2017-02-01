package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Session extends Container<Record> {

  protected LocalDate date;
  private LocalTime start;
  private LocalTime end;
  private long duration;
  private boolean valid = false;
  protected double multiplier = 1.0D;

  public Session(int value) {
    super(value);
  }

  public void build(final Record start, final Record end) throws IllegalArgumentException {
    if (start == null || end == null) {
      throw new NullPointerException("Session has no start or end: null");
    }
    if (!start.isValid() || !end.isValid()) {
      throw new IllegalArgumentException("Session start or end is invalid");
    }
    this.duration = start.time.until(end.time, ChronoUnit.NANOS);
    if (this.duration < 0) {
      throw new IllegalArgumentException("Session starts after its end");
    }
    if (this.duration == 0 || start.time.equals(end.time)) {
      throw new IllegalArgumentException("Session starts and ends at the same time");
    }
    if (start.type == RecordType.CHECKOUT || end.type == RecordType.CHECKIN) {
      throw new IllegalArgumentException("Session starts with CHECKOUT or ends with CHECKIN");
    }
    if (!start.date.equals(end.date)) {
      throw new IllegalArgumentException("Session starts and ends on different days");
    }
    this.date = start.getDate();
    this.start = start.time;
    this.end = end.time;
    this.duration = this.start.until(this.end, ChronoUnit.SECONDS);
    valid = true;
  }

  public void build(double multiplier, final Record start, final Record end) throws IllegalArgumentException {
    build(start, end);
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

  public LocalTime getStart() {
    return start;
  }

  public LocalTime getEnd() {
    return end;
  }

  public boolean contains(final LocalTime time) {
    if (valid) {
      return time.isAfter(start) && time.isBefore(end);
    }
    throw new IllegalStateException("Session was not built yet");
  }

  public Session split(final LocalTime time) {
    if (!valid) {
      throw new IllegalStateException("Session was not built yet");
    }

    if (time == null || !contains(time)) {
      return null;
    }
    // create new object
    final Session session = new Session(-1);
    session.date = date;
    session.start = time;
    session.end = end;
    session.duration = time.until(end, ChronoUnit.SECONDS);
    session.valid = true;
    // update this object
    end = time;
    duration = start.until(time, ChronoUnit.SECONDS);
    // return new object
    return session;
  }

  @Override
  public String toString() {
    return date + ": " + start + " - " + end;
  }

  @Override
  public long getSummary() {
    if (valid) {
      return duration;
    }
    throw new IllegalStateException("Session was not built yet");
  }

  @Override
  public long getValuedSummary() {
    if (valid) {
      return (long) (duration * multiplier);
    }
    throw new IllegalStateException("Session was not built yet");
  }
}