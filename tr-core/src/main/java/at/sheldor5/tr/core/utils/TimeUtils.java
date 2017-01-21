package at.sheldor5.tr.core.utils;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class TimeUtils {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
  private static final String SUMMARY_FORMAT = "%d:%02d:%02d";

  public static Date truncateTime(final Date date) {
    java.util.Date tempDate;
    try {
      tempDate = DATE_FORMAT.parse(date.toString());
    } catch (final ParseException pe) {
      throw new RuntimeException(pe);
    }
    return new Date(tempDate.getTime());
  }

  public static Time truncateDate(final Time time) {
    java.util.Date tempTime;
    try {
      tempTime = TIME_FORMAT.parse(time.toString());
    } catch (final ParseException pe) {
      throw new RuntimeException(pe);
    }
    return new Time(tempTime.getTime());
  }

  public static Time getStartOfDay() {
    return Time.valueOf(LocalTime.MIN);
  }

  public static Time getEndOfDay() {
    return Time.valueOf(LocalTime.MAX);
  }

  public static String getHumanReadableTime(long millis) {
    long seconds = (millis / 1000) % 60;
    long minutes = (millis / (1000 * 60)) % 60;
    long hours = (millis / (1000 * 60 * 60));
    return String.format(SUMMARY_FORMAT, hours, minutes, seconds);
  }

  public static int getLastDayOfMonth(int year, int month) {
    if (month == 1
            || month == 3
            || month == 5
            || month == 7
            || month == 8
            || month == 10
            || month == 12) {
      return 31;
    } else if (month == 4
            || month == 6
            || month == 9
            || month == 11) {
      return 30;
    } else if (month == 2) {
      if ((year % 4 == 0 || year % 400 == 0) && year % 100 != 0) {
        return 29;
      }
      return 28;
    }
    return -1;
  }
}