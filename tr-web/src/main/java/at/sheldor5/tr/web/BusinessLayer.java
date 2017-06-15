package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.web.jsf.beans.ClockController;
import at.sheldor5.tr.web.jsf.beans.UserController;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Michael Palata
 * @date 15.06.2017
 */
@Named("businessLayer")
@SessionScoped
public class BusinessLayer implements Serializable {

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

  public List<Project> getAllProjects() {
    return dataAccessLayer.getProjects();
  }

  public Project getProject(int id) {
    return dataAccessLayer.getProject(id);
  }

  public List<Project> getProjects(final String namePart) {
    return dataAccessLayer.getProjects(namePart);
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

    int d = 1;
    LocalDate last = sessions.get(0).getDate();
    Day day = new Day(LocalDate.of(y, m, last.getDayOfMonth()));
    LocalDate current;
    for (final Session session : sessions) {
      current = session.getDate();
      if (last.equals(current)) {
        day.addItem(session);
      } else {
        d++;
        month.addItem(day);
        day = new Day(LocalDate.of(y, m, current.getDayOfMonth()));
        day.addItem(session);
      }
      last = current;
    }
    month.addItem(day);

    return month;
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
    if (user.getAdmin()) {
      return dataAccessLayer.getUser(uuid);
    }
    return new User();
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

  public void save(final Schedule schedule) {
    schedule.setUserMapping(user.getUserMapping());
    dataAccessLayer.save(schedule);
  }

}