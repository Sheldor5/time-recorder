package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.web.DataProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Michael Palata
 * @date 11.06.2017
 */
@Named("schedule")
@SessionScoped
public class ScheduleController implements Serializable {

  private static final long serialVersionUID = 42L;

  @Inject
  private UserController user;

  @Inject
  private DataProvider dataProvider;

  @Inject
  private ClockController clock;

  private Schedule schedule;

  private LocalDate date;

  private List<Schedule> schedules;

  @PostConstruct
  public void init() {
    schedules = dataProvider.getSchedules(user.getUserMapping());
    if (schedules.size() > 0) {
      schedule = schedules.get(schedules.size() - 1);
    } else {
      schedule = new Schedule();
    }
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(final LocalDate date) {
    this.date = date;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(final Schedule schedule) {

  }

  public List<Schedule> getSchedules() {
    return schedules;
  }

  public void save() {
    schedule.setUserMapping(user.getUserMapping());
    schedule.setDueDate(date);
    dataProvider.save(schedule);
    schedules.add(schedule);
    clock.setSchedule(schedule);
    schedule = new Schedule();
  }
}
