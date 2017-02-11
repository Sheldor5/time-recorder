package at.sheldor5.tr.api.user;

import at.sheldor5.tr.api.utils.TimeUtils;
import java.time.DayOfWeek;

public class Schedule {

  private final long days[] = new long[7];

  public Schedule() {

  }

  public void setTime(final DayOfWeek dayOfWeek, int hours, int minutes, int seconds) {
    setTime(dayOfWeek, TimeUtils.getSeconds(hours, minutes, seconds));
  }

  public void setTime(final DayOfWeek dayOfWeek, long seconds) {
    if (seconds < 0) {
      return;
    }
    days[dayOfWeek.getValue() - 1] = seconds;
  }

  public long getTime(final DayOfWeek dayOfWeek) {
    return days[dayOfWeek.getValue() - 1];
  }
}