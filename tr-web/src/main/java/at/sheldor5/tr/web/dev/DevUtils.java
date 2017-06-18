package at.sheldor5.tr.web.dev;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.utils.GlobalConfiguration;

import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class DevUtils {

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

  public static Session getDefaultSessionAnteMeridian(final LocalDate date) {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    return new Session(project, null, date, startTime, endTime);
  }

  public static Session getDefaultSessionPostMeridian(final LocalDate date) {
    final LocalTime startTime = LocalTime.of(12, 30);
    final LocalTime endTime = LocalTime.of(16, 30);
    return new Session(project, null, date, startTime, endTime);
  }

  public static Day getDefaultDay() {
    return getDefaultDayOfWeek(DayOfWeek.MONDAY);
  }

  public static Month getDefaultMonth(LocalDate date) {
    date = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
    Month month = new Month(date);
    for (int i = 0; i < date.lengthOfMonth(); i++) {
      month.addItem(getDefaultDay(date.plusDays(i)));
    }
    return month;
  }

  public static Month getRandomMonth(LocalDate date) {
    date = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
    Month month = new Month(date);
    for (int i = 0; i < date.lengthOfMonth(); i++) {
      month.addItem(getRandomDay(date.plusDays(i)));
    }
    return month;
  }

  public static Month getDefaultMonth() {
    return getDefaultMonth(LocalDate.of(2017, 1, 1));
  }

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private static final Project PROJECT = new Project("mockup");

  public static Day getRandomDay(final LocalDate date) {
    final Day day = new Day(date);

    final Session session1 = new Session();
    session1.setDate(date);
    session1.setStart(LocalTime.of(4 + SECURE_RANDOM.nextInt(5), SECURE_RANDOM.nextInt(60), SECURE_RANDOM.nextInt(60)));
    session1.setEnd(LocalTime.of(10 + SECURE_RANDOM.nextInt(3), SECURE_RANDOM.nextInt(60), SECURE_RANDOM.nextInt(60)));
    session1.setProject(PROJECT);
    day.addItem(session1);

    final Session session2 = new Session();
    session2.setDate(date);
    session2.setStart(LocalTime.of(13, SECURE_RANDOM.nextInt(60), SECURE_RANDOM.nextInt(60)));
    session2.setEnd(LocalTime.of(15 + SECURE_RANDOM.nextInt(3), SECURE_RANDOM.nextInt(60), SECURE_RANDOM.nextInt(60)));
    session2.setProject(PROJECT);
    day.addItem(session2);

    if (SECURE_RANDOM.nextDouble() < 0.2D) {
      final Session session3 = new Session();
      session3.setDate(date);
      session3.setStart(LocalTime.of(18 + SECURE_RANDOM.nextInt(3), SECURE_RANDOM.nextInt(60), SECURE_RANDOM.nextInt(60)));
      session3.setEnd(LocalTime.of(21 + SECURE_RANDOM.nextInt(3), SECURE_RANDOM.nextInt(60), SECURE_RANDOM.nextInt(60)));
      session3.setProject(PROJECT);
      day.addItem(session3);
    }
    return day;
  }

  public static Day getDefaultDay(final LocalDate date) {
    Day day = new Day(date);
    day.addItem(getDefaultSessionAnteMeridian(date));
    day.addItem(getDefaultSessionPostMeridian(date));
    return day;
  }

  public static Day getDefaultDayOfWeek(final DayOfWeek dayOfWeek) {
    final LocalDate date = firstMondayOf2017.plusDays(dayOfWeek.getValue() - 1);
    final Day day = new Day(date);
    day.addItem(getDefaultSessionAnteMeridian(date));
    day.addItem(getDefaultSessionPostMeridian(date));
    return day;
  }
}
