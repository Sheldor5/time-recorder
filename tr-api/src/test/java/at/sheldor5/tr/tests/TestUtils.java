package at.sheldor5.tr.tests;

import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.RecordType;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.utils.GlobalConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestUtils {

  public static long getTimeInConfiguredUnit(int hours) {
    return getTimeInConfiguredUnit(hours, 0, 0, 0);
  }

  public static long getTimeInConfiguredUnit(int hours, int minutes) {
    return getTimeInConfiguredUnit(hours, minutes, 0, 0);
  }

  public static long getTimeInConfiguredUnit(int hours, int minutes, int seconds) {
    return getTimeInConfiguredUnit(hours, minutes, seconds, 0);
  }

  public static long getTimeInConfiguredUnit(int hours, int minutes, int seconds, int nanos) {
    final LocalTime time = LocalTime.of(hours, minutes, seconds, nanos);
    return LocalTime.MIN.until(time, GlobalConfiguration.MEASURE_UNIT);
  }

  private static final LocalDate firstMondayOf2017 = LocalDate.of(2017, 1, 2);

  /**
   * TODO
   *
   * @param date
   * @return
   */
  public static Session getDefaultSessionAnteMeridiem(final LocalDate date) {
    final Record start = new Record(date, LocalTime.of(8, 0), RecordType.CHECKIN);
    final Record end = new Record(date, LocalTime.of(12, 0), RecordType.CHECKOUT);
    return new Session(start, end);
  }

  /**
   * TODO
   *
   * @param date
   * @return
   */
  public static Session getDefaultSessionPostMeridiem(final LocalDate date) {
    final Record start = new Record(date, LocalTime.of(12, 30), RecordType.CHECKIN);
    final Record end = new Record(date, LocalTime.of(16, 30), RecordType.CHECKOUT);
    return new Session(start, end);
  }

  /**
   * TODO
   *
   * @return
   */
  public static Day getDefaultDay() {
    return getDefaultDayOfWeek(DayOfWeek.MONDAY);
  }

  /**
   * TODO ?
   * First day of week of 2017 with 2 sessions (8:00-12:00 and 12:30-16:30).
   *
   * @param dayOfWeek
   * @return
   */
  public static Day getDefaultDayOfWeek(final DayOfWeek dayOfWeek) {
    final LocalDate date = firstMondayOf2017.plusDays(dayOfWeek.getValue() - 1);
    final Day day = new Day(date);
    day.addItem(getDefaultSessionAnteMeridiem(date));
    day.addItem(getDefaultSessionPostMeridiem(date));
    return day;
  }
}
