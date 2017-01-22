package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import at.sheldor5.tr.core.utils.TimeUtils;

import java.sql.Time;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class Period {

  private double multiplier;
  private Record begin;
  private Record end;
  private long duration;
  private long valuedDuration;

  public Period(double multiplier, final Record begin, final Record end) throws IllegalArgumentException {
    this.duration = end.time.getTime() - begin.time.getTime();
    if (this.duration < 0 || begin.type != RecordType.CHECKIN || end.type != RecordType.CHECKOUT) {
      throw new IllegalArgumentException("The beginning of the period is after the end");
    }
    if (begin.type == RecordType.CHECKOUT || end.type == RecordType.CHECKIN) {
      throw new IllegalArgumentException("Period begins with CHECKOUT or ends with CHECKIN");
    }
    if (begin.date.getTime() != end.date.getTime()) {
      throw new IllegalArgumentException("Begin and end date were not on the same day");
    }
    this.valuedDuration = (long) (multiplier * duration);
    this.multiplier = multiplier;
    this.begin = begin;
    this.end = end;
  }

  public double getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(double multiplier) {
    this.multiplier = multiplier;
    valuedDuration = (long) (multiplier * duration);
  }

  public Record getBegin() {
    return begin;
  }

  public Record getEnd() {
    return end;
  }

  public long getDuration() {
    return duration;
  }

  public long getValuedDuration() {
    return valuedDuration;
  }

  public boolean contains(final Time time) {
    if (time == null) {
      return false;
    }
    return time.after(begin.time) && time.before(end.time);
  }

  public Period split(final Time time) {
    if (time == null || !contains(time)) {
      return null;
    }
    final Record thisEnd = new Record(0, begin.getDate(), time, RecordType.CHECKOUT);
    final Record otherBegin = new Record(0, end.getDate(), time, RecordType.CHECKIN);
    final Period period = new Period(1.0D, otherBegin, end);
    end = thisEnd;
    duration = end.time.getTime() - begin.time.getTime();
    valuedDuration = (long) (multiplier * duration);
    return period;
  }

  @Override
  public String toString() {
    return begin.date + ": " + TimeUtils.getHumanReadableSummary(begin.time.getTime()) + " - " + TimeUtils.getHumanReadableSummary(end.time.getTime());
  }
}