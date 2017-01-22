package at.sheldor5.tr.core.utils;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class TimeUtils {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

  private static final String TIME_FORMAT_STRING = "%02d:%02d:%02d";
  private static final String SUMMARY_FORMAT_STRING = "%d:%02d:%02d";

  private static final GregorianCalendar CALENDAR = new GregorianCalendar();

  private static final int TIME_ZONE_OFFSET = (CALENDAR.get(Calendar.ZONE_OFFSET) + CALENDAR.get(Calendar.DST_OFFSET)) / (60 * 1000);

  public static final long SECOND_IN_MILLIS = 1000L;
  public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60L;
  public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60L;

  private static final long TIME_ZONE_OFFSET_IN_MILLIS = MINUTE_IN_MILLIS * TIME_ZONE_OFFSET;

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

  public static String getHumanReadableSummary(long millis) {
    long seconds = (millis / SECOND_IN_MILLIS) % 60L;
    long minutes = (millis / MINUTE_IN_MILLIS) % 60L;
    long hours = millis / HOUR_IN_MILLIS;
    return String.format(SUMMARY_FORMAT_STRING, hours, minutes, seconds);
  }

  public static String getHumanReadableTime(long millis) {
    long seconds = (millis / SECOND_IN_MILLIS) % 60L;
    long minutes = (millis / MINUTE_IN_MILLIS) % 60L;
    long hours = millis / HOUR_IN_MILLIS;
    return String.format(TIME_FORMAT_STRING, hours, minutes, seconds);
  }

  public static Time getTime(long hours, long minutes, long seconds, long milliseconds) {
    long millis =  milliseconds;
    millis += seconds * SECOND_IN_MILLIS;
    millis +=  minutes * MINUTE_IN_MILLIS;
    millis +=  hours * HOUR_IN_MILLIS;
    //millis -= TIME_ZONE_OFFSET_IN_MILLIS;
    return new Time(millis);
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