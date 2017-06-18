package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Account;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.rules.RuleManager;
import at.sheldor5.tr.web.jsf.beans.UserController;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Michael Palata
 * @date 15.06.2017
 */
@Named("businessLayer")
@SessionScoped
public class BusinessLayer implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(BusinessLayer.class.getName());

  private DataAccessLayer dataAccessLayer;
  private UserController user;

  public BusinessLayer() {
    // CDI
  }

  @Inject
  public BusinessLayer(final DataAccessLayer dataAccessLayer, final UserController user) {
    this.dataAccessLayer = dataAccessLayer;
    this.user = user;
  }

  public List<Project> getProjects() {
    return dataAccessLayer.getProjects(user.getUserMapping());
  }

  public List<Project> getProjects(final UserMapping userMapping) {
    return dataAccessLayer.getProjects(userMapping);
  }

  public List<Project> getAllProjects() {
    return dataAccessLayer.getProjects();
  }

  public Project getProject(int id) {
    return dataAccessLayer.getProject(id);
  }

  public List<Project> getProjects(final String namePart) {
    return dataAccessLayer.getProjects(namePart);
  }

  public void addUserProjectMappings(UserMapping usermapping, List<Project> projects) {
    dataAccessLayer.addUserProjectMappings(usermapping, projects);
  }

  public void save(final Session session) {
    session.setUserMapping(user.getUserMapping());
    dataAccessLayer.save(session);
  }

  public void save(final Project project) {
    dataAccessLayer.save(project);
  }

  public Day getDay(final LocalDate date) {
    final List<Session> sessions = dataAccessLayer.getSessions(user.getUserMapping(), date);
    final Day day = new Day(date);
    day.setItems(sessions);
    return day;
  }

  public Month getMonth(final LocalDate date) {
    Month month = new Month(date);

    int y = date.getYear();
    int m = date.getMonthValue();

    LocalDate from = LocalDate.of(y, m, 1);
    LocalDate to = LocalDate.of(y, m, date.lengthOfMonth());

    final List<Session> sessions = dataAccessLayer.getSessions(user.getUserMapping(), from, to);

    if (sessions.size() == 0) {
      return month;
    }

    Collections.sort(sessions);

    final Schedule schedule = dataAccessLayer.getSchedule(user.getUserMapping());

    int d = 1;
    LocalDate last = sessions.get(0).getDate();
    Day day = new Day(LocalDate.of(y, m, last.getDayOfMonth()));
    day.setSchedule(schedule);
    LocalDate current;
    for (final Session session : sessions) {
      current = session.getDate();
      if (last.equals(current)) {
        day.addItem(session);
      } else {
        d++;
        month.addItem(day);
        day = new Day(LocalDate.of(y, m, current.getDayOfMonth()));
        day.setSchedule(schedule);
        day.addItem(session);
      }
      last = current;
    }
    month.addItem(day);

    return month;
  }

  public Month getValuedMonthMock(final Month month, final Schedule schedule) {
    for (final Day day : month.getItems()) {
      RuleManager.getInstance().apply(day);
    }
    final Iterator<Day> iterator = month.getItems().iterator();

    final LocalDate date = month.getDate();
    final Month valued = new Month(date);

    final LocalDate beginOfMonth = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
    LocalDate c;
    Day d = iterator.next();
    for (int i = 0; i < date.lengthOfMonth(); i++) {
      c = beginOfMonth.plusDays(i);
      if (c.equals(d.getDate())) {
        d.setSchedule(schedule);
        valued.addItem(d);
        if (iterator.hasNext()) {
          d = iterator.next();
        } else {
          d = new Day(c);
        }
      } else {
        d = new Day(c);
        d.setSchedule(schedule);
      }
    }

    return valued;
  }

  public Month getValuedMonth(final LocalDate date) {
    final Month month = getMonth(date);

    for (final Day day : month.getItems()) {
      RuleManager.getInstance().apply(day);
    }

    final Schedule schedule = dataAccessLayer.getSchedule(user.getUserMapping(),
        LocalDate.of(date.getYear(), date.getMonthValue(), date.lengthOfMonth()));
    final Iterator<Day> iterator = month.getItems().iterator();

    final Month valued = new Month(date);

    final LocalDate beginOfMonth = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
    LocalDate c;
    Day d = iterator.next();
    for (int i = 0; i < date.lengthOfMonth(); i++) {
      c = beginOfMonth.plusDays(i);
      if (c.equals(d.getDate())) {
        d.setSchedule(schedule);
        valued.addItem(d);
        if (iterator.hasNext()) {
          d = iterator.next();
        } else {
          d = new Day(c);
        }
      } else {
        d = new Day(c);
        d.setSchedule(schedule);
      }
    }

    return valued;
  }

  public Schedule getSchedule() {
    return dataAccessLayer.getSchedule(user.getUserMapping());
  }

  public List<Schedule> getSchedules() {
    return dataAccessLayer.getSchedules(user.getUserMapping());
  }

  public List<User> getUsers() {
    if (isAdmin()) {
      return dataAccessLayer.getUsers();
    }
    return new ArrayList<>();
  }

  public User getUser(UUID uuid) {
    User user = dataAccessLayer.getUser(uuid);
    return user == null ? new User() : user;
  }

  public User getUser() {
    return user.getUser();
  }

  public User getUserCopy() {
    final User copy = new User();
    copy.setForename(user.getForename());
    copy.setSurname(user.getSurname());
    copy.setUsername(user.getUsername());
    return copy;
  }

  public boolean isAdmin() {
    return user.getAdmin();
  }

  public void saveUser() {
    dataAccessLayer.save(user.getUser());
  }

  public void save(final User user, Role role, List<Project> projects) {
    dataAccessLayer.save(user, role, projects);
  }

  public void save(final Schedule schedule) {
    schedule.setUserMapping(user.getUserMapping());
    dataAccessLayer.save(schedule);
  }

  /**
   * Gets the Account of the date's month.
   * e.g. the date "5. January 2017" would return the
   * Account for the month January in 2017.
   *
   * @param date the date of which month's Account is requested.
   * @return The Account of the date's month.
   */
  public Account getAccountOfMonth(LocalDate date) {
    // the date will be modified to represent the last day of month
    // of the date's month, e.g. "14.01.2017 -> 01.01.2017"
    date = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
    return dataAccessLayer.getAccount(user.getUserMapping(), date);
  }

  /**
   * Get the Account of the date's year.
   * @param date
   * @return
   */
  public Account getAccountOfYear(LocalDate date) {
    // the date will be modified to represent the last day
    // of the date's year, e.g. "14.11.2017 -> 31.12.2017"
    date = LocalDate.of(date.getYear(), 12, 31);
    return dataAccessLayer.getAccount(user.getUserMapping(), date);
  }

  public void save(final Account account, boolean ofYear) {
    if (ofYear) {
      final LocalDate date = LocalDate.of(account.getDate().getYear(), 12, 31);
      if (!date.equals(account.getDate())) {
        account.setDate(date);
      }
    } else {
      final LocalDate date = LocalDate.of(account.getDate().getYear(), account.getDate().getMonthValue(), 1);
      if (!date.equals(account.getDate())) {
        account.setDate(date);
      }
    }
    dataAccessLayer.save(account);
  }

  public List<Session> getMockupSessions(){
    LocalTime begin;
    LocalTime end;

    List<Session> sessionList= new ArrayList<>();

    begin =  LocalTime.MIN;
    end =  LocalTime.of(12, 0);
    Session session1 = new Session(LocalDate.of(2017, 1, 2), begin, end);

    begin = LocalTime.of(12,0);
    end = LocalTime.MAX;
    Session session2 = new Session(LocalDate.of(2017, 1, 3), begin, end);

    begin = LocalTime.of(8,0);
    end =  LocalTime.of(12,0);
    Session session3 = new Session(LocalDate.of(2017, 1, 4), begin, end);

    begin =  LocalTime.MIN;
    end =  LocalTime.MAX;
    Session session4 = new Session(LocalDate.of(2017, 1, 5), begin, end);

    begin =  LocalTime.MIN;
    end =  LocalTime.of(12, 0);
    Session session5 = new Session(LocalDate.of(2017, 1, 6), begin, end);

    begin =  LocalTime.of(12,0);
    end = LocalTime.MAX;
    Session session6 = new Session(LocalDate.of(2017, 1, 9), begin, end);

    begin = LocalTime.of(8,0);
    end = LocalTime.of(16,0);
    Session session7 = new Session(LocalDate.of(2017, 1, 10), begin, end);

    sessionList.add(session1);
    sessionList.add(session2);
    sessionList.add(session3);
    sessionList.add(session4);
    sessionList.add(session5);
    sessionList.add(session6);
    sessionList.add(session7);
    return sessionList;
  }

}
