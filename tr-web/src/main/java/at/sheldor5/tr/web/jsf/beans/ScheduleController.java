package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.web.BusinessLayer;

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

  private BusinessLayer businessLayer;

  private Schedule schedule;

  private LocalDate date;

  private List<Schedule> schedules;

  @PostConstruct
  @Inject
  public void init(final BusinessLayer businessLayer) {
    this.businessLayer = businessLayer;
    schedules = businessLayer.getSchedules();
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
    schedule.setDueDate(date);
    businessLayer.save(schedule);
    //businessLayer.updateClock();
    schedules.add(schedule);
    schedule = new Schedule();
  }
}
