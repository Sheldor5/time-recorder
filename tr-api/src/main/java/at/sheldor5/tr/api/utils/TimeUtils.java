package at.sheldor5.tr.api.utils;

import java.time.chrono.IsoChronology;

public class TimeUtils {

  private static final String TIME_FORMAT_STRING = "%02d:%02d:%02d";
  private static final String SUMMARY_FORMAT_STRING = "%s%d:%02d:%02d";

  public static final long SECOND_IN_MILLIS = 1000L;
  public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60L;
  public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60L;

  public static String getHumanReadableSummary(long value) {
    final String prefix = value < 0 ? "-" : "";
    if (value < 0) {
      value *= -1;
    }
    int s = (int) (value / TimeUtils.getConfiguretUnitSecondsMultiplier());
    long seconds = s % 60L;
    long minutes = (s / 60L) % 60L;
    long hours = s / 3600L;
    return String.format(SUMMARY_FORMAT_STRING, prefix, hours, minutes, seconds);
  }

  public static long getSeconds(int hours, int minutes, int seconds) {
    return seconds + minutes * 60L + hours * 3600L;
  }

  public static long getMillis(final String string) {
    int h = string.indexOf(':');
    int m = string.indexOf(':', h + 1);

    int hours;
    int minutes;
    int seconds;

      try {
        hours = Integer.valueOf(string.substring(0, h));
        minutes = Integer.valueOf(string.substring(h + 1, m));
        seconds = Integer.valueOf(string.substring(m + 1));
    } catch (final Exception e) {
      e.printStackTrace();
      return -1;
    }

    long millis = seconds * SECOND_IN_MILLIS;
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

  public static int getConfiguretUnitSecondsMultiplier() {
    switch (GlobalConfiguration.MEASURE_UNIT) {
      case NANOS:
        return 1000000000;
      case MICROS:
        return 1000000;
      case MILLIS:
        return 1000;
      default:
        return 1;
    }
  }
}
