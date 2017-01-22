package at.sheldor5.tr.core.records;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
@Deprecated
public class Summary {

  private static final String SUMMARY_FORMAT = "%d:%02d:%02d";
  private static final String SUMMARY_FORMAT_MILLIS = "%d:%02d:%02d:%03d";

  private int hours = 0;
  private int minutes = 0;
  private int seconds = 0;
  private long millis = 0;

  public Summary() {

  }

  public Summary(int hours, int minutes, int seconds) {
    addSeconds(seconds);
    addMinutes(minutes);
    addHours(hours);
  }

  public void addMillis(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("removing time is not supported");
    }
    this.millis += millis;
    addSeconds((int) (this.millis / 1000));
    this.millis = this.millis % 1000;
  }

  public int getSeconds() {
    return seconds;
  }

  public void addSeconds(int seconds) {
    if (seconds < 0) {
      throw new IllegalArgumentException("removing time is not supported");
    }
    this.seconds += seconds;
    this.addMinutes(this.seconds / 60);
    this.seconds = this.seconds % 60;
  }

  public void setSeconds(int seconds) {
    this.seconds = seconds;
  }

  public int getMinutes() {
    return minutes;
  }

  public void addMinutes(int minutes) {
    if (minutes < 0) {
      throw new IllegalArgumentException("removing time is not supported");
    }
    this.minutes += minutes;
    this.addHours(this.minutes / 60);
    this.minutes = this.minutes % 60;
  }

  public void setMinutes(int minutes) {
    this.minutes = minutes;
  }

  public int getHours() {
    return hours;
  }

  public void addHours(int hours) {
    if (hours < 0) {
      throw new IllegalArgumentException("removing time is not supported");
    }
    this.hours += hours;
  }

  public void setHours(int hours) {
    this.hours = hours;
  }

  public void addSummary(final Summary summary) {
    this.seconds += summary.seconds;
  }

  @Override
  public String toString() {
    return String.format(SUMMARY_FORMAT, hours, minutes, seconds);
  }

  public String toStringMillis() {
    return String.format(SUMMARY_FORMAT_MILLIS, hours, minutes, seconds, millis);
  }
}