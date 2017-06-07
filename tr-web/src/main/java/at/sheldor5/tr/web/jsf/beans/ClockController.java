package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.web.DataProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Named("clock")
@RequestScoped
public class ClockController implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(ClockController.class.getName());

  private static final long serialVersionUID = 1L;

  @Inject
  private DataProvider dataProvider;

  @Inject
  private UserController user;

  protected Project last;
  protected Session session;
  protected LocalTime time;
  protected LocalDate date;

  private List<Project> projects;

  private List<Session> sessions;

  @PostConstruct
  public void init() {
    projects = dataProvider.getProjects(user.getUserMapping());
    sessions = dataProvider.getSessions(user.getUserMapping(), LocalDate.now());

    if (sessions.size() > 0) {
      Collections.sort(sessions);
      session = sessions.get(sessions.size() - 1);
      last = session.getProject();
      if (session.getEnd() != null) {
        session = null;
      }
    }
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(final LocalTime time) {
    this.time = time;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(final LocalDate date) {
    this.date = date;
  }

  public void stamp() {
    if (time != null) {
      if (session == null) {
        session = new Session(last, user.getUserMapping(), LocalDate.now(), time);
        session.setDate(LocalDate.now());
        session.setStart(time);
        dataProvider.save(session);
      } else {
        session.setEnd(time);
        dataProvider.save(session);
        session = null;
      }

    } else {
      LOGGER.info("null");
    }
  }

  public List<Project> getProjects() {
    return projects;
  }

  protected Project getLastProject() {
    return null;
  }

  public void switchProject(final Project project) {
    if (session != null && session.getEnd() == null) {
      session.setEnd(time);
      dataProvider.save(session);
      session = new Session(project, user.getUserMapping(), LocalDate.now(), time);
      last = project;
      dataProvider.save(session);
    }
    LOGGER.info(project.getName());
    LOGGER.info("" + user);
  }

  public boolean hasStarted() {
    return session != null;
  }

  public List<Session> getSessions() {
    return sessions;
  }

}
