package at.sheldor5.tr.api.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class TimeUtils {

  private static final String TIME_FORMAT_STRING = "%02d:%02d:%02d";
  private static final String SUMMARY_FORMAT_STRING = "%d:%02d:%02d";

  private static final GregorianCalendar CALENDAR = new GregorianCalendar();


  public static final long SECOND_IN_MILLIS = 1000L;
  public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60L;
  public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60L;

  public static String getHumanReadableSummary(long seconds) {
    long s = seconds % 60L;
    long m = (seconds / 60L) % 60L;
    long h = seconds / 3600L;
    return String.format(SUMMARY_FORMAT_STRING, h, m, s);
  }

  public static String getHumanReadableTime(long millis) {
    long seconds = (millis / SECOND_IN_MILLIS) % 60L;
    long minutes = (millis / MINUTE_IN_MILLIS) % 60L;
    long hours = millis / HOUR_IN_MILLIS;
    return String.format(TIME_FORMAT_STRING, hours, minutes, seconds);
  }

  public static long getMillis(final String string) {
    int h = string.indexOf(':');
    int m = string.indexOf(':', h + 1);

    int hours;
    int minutes;
    int seconds;

    long millis =  0;
      try {
        hours = Integer.valueOf(string.substring(0, h));
        minutes = Integer.valueOf(string.substring(h + 1, m));
        seconds = Integer.valueOf(string.substring(m + 1));
    } catch (final NumberFormatException nfe) {
      nfe.printStackTrace();
      return -1;
    }

    millis += seconds * SECOND_IN_MILLIS;
    millis +=  minutes * MINUTE_IN_MILLIS;
    millis +=  hours * HOUR_IN_MILLIS;

    return millis;
  }

  public static long secondsToHours(long seconds) {
    return seconds / 3600;
  }

  public static long hoursToSeconds(long hours) {
    return hours * 3600;
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
      if (CALENDAR.isLeapYear(year)) {
        return 29;
      }
      return 28;
    }
    return -1;
  }
}