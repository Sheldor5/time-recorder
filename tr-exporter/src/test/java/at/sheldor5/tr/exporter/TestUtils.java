package at.sheldor5.tr.exporter;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
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
  private static final Project project = new Project("time-recorder");

  /**
   * TODO
   *
   * @param date
   * @return
   */
  public static Session getDefaultSessionAnteMeridian(final LocalDate date) {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    return new Session(project, null, date, startTime, endTime);
  }

  /**
   * TODO
   *
   * @param date
   * @return
   */
  public static Session getDefaultSessionPostMeridian(final LocalDate date) {
    final LocalTime startTime = LocalTime.of(12, 30);
    final LocalTime endTime = LocalTime.of(16, 30);
    return new Session(project, null, date, startTime, endTime);
  }

  /**
   * TODO
   *
   * @return
   */
  public static Day getDefaultDay() {
    return getDefaultDayOfWeek(DayOfWeek.MONDAY);
  }

  public static Month getDefaultMonth() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    Month month = new Month(date);
    for (int i = 0; i < 31; i++) {
      month.addItem(getDefaultDay(date.plusDays(i)));
    }
    return month;
  }

  public static Day getDefaultDay(final LocalDate date) {
    Day day = new Day(date);
    day.addItem(getDefaultSessionAnteMeridian(date));
    day.addItem(getDefaultSessionPostMeridian(date));
    return day;
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
    day.addItem(getDefaultSessionAnteMeridian(date));
    day.addItem(getDefaultSessionPostMeridian(date));
    return day;
  }
}
