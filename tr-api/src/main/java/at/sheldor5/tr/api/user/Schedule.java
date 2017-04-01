package at.sheldor5.tr.api.user;

import at.sheldor5.tr.api.utils.GlobalConfiguration;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * This class represents the weekly schedule of organized time investments.
 */
public class Schedule {

  private final long days[] = new long[7];

  /**
   * Default constructor.
   */
  public Schedule() {

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
   * the given time in the configured time unit.
   *
   * @param dayOfWeek The day of week to set.
   * @param time      The time representing the time amount between 00:00 and the given time,
   *                  and not the time on the clock.
   */
  public void setTime(final DayOfWeek dayOfWeek, final LocalTime time) {
    if (dayOfWeek == null || time == null) {
      return;
    }
    days[dayOfWeek.getValue() - 1] = LocalTime.MIN.until(time, GlobalConfiguration.MEASURE_UNIT);
  }

  /**
   * Getter for amount of time of a specific day of week.
   *
   * @param dayOfWeek
   * @return
   */
  public long getTime(final DayOfWeek dayOfWeek) {
    return days[dayOfWeek.getValue() - 1];
  }
}
