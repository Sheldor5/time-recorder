package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.api.utils.TimeUtils;
import at.sheldor5.tr.web.DataProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Named("clock")
@SessionScoped
public class ClockController implements Serializable {

  private static final Logger LOGGER = Logger.getLogger(ClockController.class.getName());
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private static final int OBFUSCATOR_MAX = 1337;

  private static final long serialVersionUID = 1L;

  @Inject
  @RequestScoped
  private DataProvider dataProvider;

  @Inject
  private UserController user;

  protected Project project;

  protected Session session;
  protected LocalTime time;
  protected LocalDate date;

  private boolean isSynchronized = false;
  private List<Project> projects;
  private Day today;
  private int obfuscator;

  @PostConstruct
  public void init() {
    projects = dataProvider.getProjects(user.getUserMapping());
    today = dataProvider.getDay(user.getUserMapping(), date == null ? LocalDate.now() : date);
    List<Session> sessions = today.getItems();

    if (sessions.size() > 0) {
      today.setSchedule(user.getSchedule());
      Collections.sort(sessions);
      session = sessions.get(sessions.size() - 1);
      project = session.getProject();
      if (session.getEnd() != null) {
        session = null;
      }
    }

    if (project == null) {
      if (projects.size() > 0) {
        project = projects.get(0);
      } else {
        project = dataProvider.getProject(1);
      }
    }

    obfuscator = (int) (OBFUSCATOR_MAX * SECURE_RANDOM.nextDouble());
  }

  public Project getProject() {
    return project;
  }

  public int getProjectId() {
    return obfuscator + (project == null ? 0 : project.getId());
  }

  public void setProjectId(int id) {
    id -= obfuscator;
    for (final Project p : projects) {
      if (id == p.getId()) {
        this.project = p;
        break;
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
    isSynchronized = true;
    if (time != null) {
      if (session == null) {
        session = new Session(project, user.getUserMapping(), date, time);
        session.setDate(LocalDate.now());
        session.setStart(time);
        dataProvider.save(session);
        today.addItem(session);
      } else {
        session.setEnd(time);
        dataProvider.save(session);
        session = null;
      }
    } else {
      LOGGER.info("null");
    }
  }

  public void setSchedule(final Schedule schedule) {
    today.setSchedule(schedule);
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void switchProject(final Project project) {
    if (session != null && session.getEnd() == null) {
      stamp();
      this.project = project;
      stamp();
    }
    LOGGER.info(project.getName());
    LOGGER.info("" + user);
  }

  public boolean hasStarted() {
    return session != null;
  }

  public Day getToday() {
    return today;
  }

  public String getHumanReadableSummary(long time) {
    return TimeUtils.getHumanReadableSummary(time);
  }

  public void sync() {
    init();
    isSynchronized = true;
  }

  public boolean getIsSynchronized() {
    return isSynchronized;
  }

}
