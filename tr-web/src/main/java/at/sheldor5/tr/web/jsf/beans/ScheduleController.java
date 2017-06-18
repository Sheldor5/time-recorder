package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.time.Account;
import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.rules.Holiday;
import at.sheldor5.tr.web.BusinessLayer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Michael Palata
 * @date 11.06.2017
 */
@Named("schedule")
@SessionScoped
public class ScheduleController implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(ScheduleController.class.getName());

  private static final long serialVersionUID = 42L;

  private BusinessLayer businessLayer;

  private UserController userController;

  private Schedule schedule;

  private LocalDate date;

  private List<Schedule> schedules;


  public ScheduleController() {
    // CDI
  }

  @Inject
  public ScheduleController(final BusinessLayer businessLayer, final UserController userController) {
    this.businessLayer = businessLayer;
    this.userController = userController;
  }

  @PostConstruct
  public void init() {
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
    updateScheduleAccounts();
    schedule = new Schedule();
  }

  // todo put in business layer maybe
  private void updateScheduleAccounts() {
    LOGGER.info("month value: " + date.getMonthValue());
    LOGGER.info("year: " + date.getYear());
    Collections.sort(schedules, Comparator.comparing(Schedule::getDueDate));
    List<Schedule> relevantSchedules = getRelevantSchedulesForMonth(date, schedules);
    long workTimeAccordingToScheduleForMonth = getScheduleTimeForMonth(date, relevantSchedules);
    Account accountOfMonth = businessLayer.getAccountOfMonth(date);
    accountOfMonth.setTime(accountOfMonth.getTimeWorked() - workTimeAccordingToScheduleForMonth);
    businessLayer.save(accountOfMonth, false);
    LOGGER.info("work time according to schedule: " + workTimeAccordingToScheduleForMonth);
  }

  private List<Schedule> getRelevantSchedulesForMonth(LocalDate date, List<Schedule> allSchedules) {
    int month = date.getMonthValue();
    List<Schedule> relevantSchedules = new ArrayList<>();
    for(int i = 0; i < allSchedules.size(); i++) {
      int currentScheduleMonth = allSchedules.get(i).getDueDate().getMonth().getValue();
      int currentScheduleDay = allSchedules.get(i).getDueDate().getDayOfMonth();
      if(currentScheduleMonth == month) {
        if(relevantSchedules.size() == 0) {
          // looking for first relevant schedule, can be from the month before
          if(currentScheduleDay != 1) {
            // is there a schedule from the previous month?
            if(i > 0) {
              // yes, take last schedule from previous month
              relevantSchedules.add(allSchedules.get(i - 1));
            }
            else {
              // no
              relevantSchedules.add(allSchedules.get(i));
            }
          }
          else {
            // there is a schedule that was activated on the 1st of the relevant month,
            // so take this one as the first relevant schedule
            relevantSchedules.add(allSchedules.get(i));
          }
        }
        else {
          // we already have the first relevant schedule, so just add every schedule
          // with the correct month
          relevantSchedules.add(allSchedules.get(i));
        }
      }
    }
    return relevantSchedules;
  }

  /**
   * takes the month from the given date calculates the time that the person should work in this month using the given schedules
   * @param date
   * @param relevantSchedules must be in ascending order starting with the first schedule that should be used for the calculation
   * @return returns the time a person schould work in a given month
   */
  private long getScheduleTimeForMonth(LocalDate date, List<Schedule> relevantSchedules) {
    int daysInMonth = getDaysForMonth(date.getMonthValue(), date.getYear());
    LocalDate currentDate = getStartDate(date, relevantSchedules);
    LocalDate endDate;
    long time = 0;
    for(int i = 0; i < relevantSchedules.size(); i++) {
      if(i == relevantSchedules.size() - 1) {
        // we are at the last schedule, so it will be applied until the end of the month
        endDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), daysInMonth);
      }
      else {
        // endDate is the dueDate from the next schedule
        endDate = relevantSchedules.get(i + 1).getDueDate();
      }
      for(Schedule currentSchedule = relevantSchedules.get(i); currentDate.getDayOfMonth() < endDate.getDayOfMonth(); currentDate = currentDate.plusDays(1)){
        //if(!isHoliday(currentDate)) { todo uncomment!
          time += currentSchedule.getTime(currentDate.getDayOfWeek());
        //}
      }
    }
    return time;
  }

  private LocalDate getStartDate(LocalDate date, List<Schedule> relevantSchedules) {
    Schedule firstSchedule = relevantSchedules.get(0);
    if(firstSchedule.getDueDate().getMonthValue() == date.getMonthValue() && firstSchedule.getDueDate().getDayOfMonth() != 1) {
      // there is no schedule that covers the time from the 1st of the month to the current day,
      // so we begin with the dueDate from the first schedule
      return firstSchedule.getDueDate();
    }
    else {
      // begin with the 1st of the month because there is a schedule that covers this time period
      return LocalDate.of(date.getYear(), date.getMonth(), 1);
    }
  }

  private int getDaysForMonth(int month, int year) {
    return LocalDate.of(year, month, 1).lengthOfMonth();
  }

  private boolean isHoliday(LocalDate date) {
    Holiday holiday = new Holiday(date);
    try {
      return holiday.applies();
    }
    catch (GeneralSecurityException | IOException e) {
      LOGGER.severe("Cannot perform holiday check for " + date + ". isHoliday() now returns false. This will lead to incorrect flexitime calculations.");
      return false;
    }
  }
}
