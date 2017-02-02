package at.sheldor5.tr.api.utils;

import java.time.chrono.IsoChronology;

public class TimeUtils {

  private static final String TIME_FORMAT_STRING = "%02d:%02d:%02d";
  private static final String SUMMARY_FORMAT_STRING = "%d:%02d:%02d";

  public static final long SECOND_IN_MILLIS = 1000L;
  public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60L;
  public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60L;

  public static String getHumanReadableSummary(long seconds) {
    long s = seconds % 60L;
    long m = (seconds / 60L) % 60L;
    long h = seconds / 3600L;
    return String.format(SUMMARY_FORMAT_STRING, h, m, s);
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

  public static int getLastDayOfMonth(int year, int month) {
    switch (month) {
      case 2:
        return (IsoChronology.INSTANCE.isLeapYear(year) ? 29 : 28);
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      default:
        return 31;
    }
  }
}