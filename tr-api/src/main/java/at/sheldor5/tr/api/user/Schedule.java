package at.sheldor5.tr.api.user;

import at.sheldor5.tr.api.utils.GlobalConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents the weekly schedule of organized time investments.
 */
public class Schedule {

  private int id;
  private int userId;
  private LocalDate dueDate;
  private LocalTime monday = LocalTime.MIN;
  private LocalTime tuesday = LocalTime.MIN;
  private LocalTime wednesday = LocalTime.MIN;
  private LocalTime thursday = LocalTime.MIN;
  private LocalTime friday = LocalTime.MIN;
  private LocalTime saturday = LocalTime.MIN;
  private LocalTime sunday = LocalTime.MIN;
  private final long[] days = new long[7];

  /**
   * Default constructor.
   */
  public Schedule() {

  }

  /**
   * Getter for the ID.
   *
   * @return The ID of this Schedule.
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Getter for the user ID.
   *
   * @return The id of the user this Schedule belongs to.
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Setter for the user ID.
   *
   * @param userId The id of the user this Schedule belongs to.
   */
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * Getter for the due date.
   *
   * @return The due date of this schedule.
   */
  public LocalDate getDueDate() {
    return dueDate;
  }

  /**
   * Setter for the due date.
   *
   * @param dueDate The due date of this schedule.
   */
  public void setDueDate(final LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public LocalTime getMonday() {
    return monday;
  }

  public void setMonday(final LocalTime time) {
    if (time == null) {
      return;
    }
    this.monday = time;
    days[0] = LocalTime.MIN.until(this.monday, GlobalConfiguration.MEASURE_UNIT);
  }

  public LocalTime getTuesday() {
    return tuesday;
  }

  public void setTuesday(final LocalTime time) {
    if (time == null) {
      return;
    }
    this.tuesday = time;
    days[1] = LocalTime.MIN.until(this.tuesday, GlobalConfiguration.MEASURE_UNIT);
  }

  public LocalTime getWednesday() {
    return wednesday;
  }

  public void setWednesday(final LocalTime time) {
    if (time == null) {
      return;
    }
    this.wednesday = time;
    days[2] = LocalTime.MIN.until(this.wednesday, GlobalConfiguration.MEASURE_UNIT);
  }

  public LocalTime getThursday() {
    return thursday;
  }

  public void setThursday(final LocalTime time) {
    if (time == null) {
      return;
    }
    this.thursday = time;
    days[3] = LocalTime.MIN.until(this.thursday, GlobalConfiguration.MEASURE_UNIT);
  }

  public LocalTime getFriday() {
    return friday;
  }

  public void setFriday(final LocalTime time) {
    if (time == null) {
      return;
    }
    this.friday = time;
    days[4] = LocalTime.MIN.until(this.friday, GlobalConfiguration.MEASURE_UNIT);
  }

  public LocalTime getSaturday() {
    return saturday;
  }

  public void setSaturday(final LocalTime time) {
    if (time == null) {
      return;
    }
    this.saturday = time;
    days[5] = LocalTime.MIN.until(this.saturday, GlobalConfiguration.MEASURE_UNIT);
  }

  public LocalTime getSunday() {
    return sunday;
  }

  public void setSunday(final LocalTime time) {
    if (time == null) {
      return;
    }
    this.sunday = time;
    days[6] = LocalTime.MIN.until(this.sunday, GlobalConfiguration.MEASURE_UNIT);
  }


  /**
   * Getter for amount of time of a specific day of week.
   *
   * @param dayOfWeek The desired day of week.
   * @return The amound of time in the configured time unit (see {@link GlobalConfiguration}).
   */
  public long getTime(final DayOfWeek dayOfWeek) {
    if (dayOfWeek == null) {
      return 0;
    }
    return days[dayOfWeek.getValue() - 1];
  }

  public void update() {
    days[0] = LocalTime.MIN.until(this.monday, GlobalConfiguration.MEASURE_UNIT);
    days[1] = LocalTime.MIN.until(this.tuesday, GlobalConfiguration.MEASURE_UNIT);
    days[2] = LocalTime.MIN.until(this.wednesday, GlobalConfiguration.MEASURE_UNIT);
    days[3] = LocalTime.MIN.until(this.thursday, GlobalConfiguration.MEASURE_UNIT);
    days[4] = LocalTime.MIN.until(this.friday, GlobalConfiguration.MEASURE_UNIT);
    days[5] = LocalTime.MIN.until(this.saturday, GlobalConfiguration.MEASURE_UNIT);
    days[6] = LocalTime.MIN.until(this.sunday, GlobalConfiguration.MEASURE_UNIT);
  }

  private LocalTime getTimeOfDayOfWeek(final DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
      case MONDAY:
        return this.monday;
      case TUESDAY:
        return this.tuesday;
      case WEDNESDAY:
        return this.wednesday;
      case THURSDAY:
        return this.thursday;
      case FRIDAY:
        return this.friday;
      case SATURDAY:
        return this.saturday;
      case SUNDAY:
        return this.sunday;
      default:
        return null;
    }
  }

  /**
   * Setter for the time amount of a specific day of week.
   *
   * @param dayOfWeek The day of week to set.
   * @param hours     The amount of hours for this day.
   * @param minutes   The amount of minutes for this day.
   * @param seconds   The amount of seconds for this day.
   */
  public void setTime(final DayOfWeek dayOfWeek, int hours, int minutes, int seconds) {
    setTime(dayOfWeek, LocalTime.of(hours, minutes, seconds));
  }

  /**
   * Setter for the time amount of a specific day of week.
   * Calculates the time between the start of the day (00:00) and
   * the given time in the configured time unit (see {@link GlobalConfiguration}).
   *
   * @param dayOfWeek The day of week to set.
   * @param time      The time representing the time amount between 00:00 and the given time,
   *                  and not the time on the clock.
   */
  public void setTime(final DayOfWeek dayOfWeek, final LocalTime time) {
    if (dayOfWeek == null || time == null) {
      return;
    }
    switch (dayOfWeek) {
      case MONDAY:
        this.monday = time;
        break;
      case TUESDAY:
        this.tuesday = time;
        break;
      case WEDNESDAY:
        this.wednesday = time;
        break;
      case THURSDAY:
        this.thursday = time;
        break;
      case FRIDAY:
        this.friday = time;
        break;
      case SATURDAY:
        this.saturday = time;
        break;
      case SUNDAY:
        this.sunday = time;
        break;
      default:
        return;
    }
    days[dayOfWeek.getValue() - 1] = LocalTime.MIN.until(time, GlobalConfiguration.MEASURE_UNIT);
  }
}
