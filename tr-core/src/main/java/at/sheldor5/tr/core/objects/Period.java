package at.sheldor5.tr.core.objects;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class Period {

  private final double multiplier;
  private final Record begin;
  private final Record end;
  private final long duration;
  private final long valuedDuration;

  public Period(double multiplier, final Record begin, final Record end) throws IllegalArgumentException {
    this.duration = end.time.getTime() - begin.time.getTime();
    if (this.duration < 0) {
      throw new IllegalArgumentException("The beginning of the period is after the end");
    }
    this.valuedDuration = (long) (multiplier * duration);
    this.multiplier = multiplier;
    this.begin = begin;
    this.end = end;
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
}