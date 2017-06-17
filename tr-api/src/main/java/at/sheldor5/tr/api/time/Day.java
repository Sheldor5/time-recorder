package at.sheldor5.tr.api.time;

import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.api.utils.GlobalConfiguration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Day extends Container<Session> {

  private Schedule schedule;

  public Day(final LocalDate date) {
    super(date);
  }

  @Override
  protected boolean validateItem(final Session item) {
    return this.date.equals(item.date);
  }

  @Override
  public final long getSummary() {
    final List<Session> sessions = this.getItems();

    long sum = 0;

    for (final Session session : sessions) {
      sum += session.getSummary();
    }

    return sum;
  }

  @Override
  public final long getValuedSummary() {
    final List<Session> sessions = this.getItems();

    long sum = 0;

    for (final Session session : sessions) {
      sum += session.getValuedSummary();
    }

    sum -= quota();

    return sum;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  private long quota() {
    if (schedule != null) {
      LocalTime time = schedule.getTimeOfDayOfWeek(date.getDayOfWeek());
      return LocalTime.MIN.until(time, GlobalConfiguration.MEASURE_UNIT);
    }
    return 0;
  }
}
