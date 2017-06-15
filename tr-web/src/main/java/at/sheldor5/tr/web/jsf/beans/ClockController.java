package at.sheldor5.tr.web.jsf.beans;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.api.utils.TimeUtils;
import at.sheldor5.tr.web.BusinessLayer;

import javax.annotation.PostConstruct;
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

  private BusinessLayer businessLayer;

  private Project project;

  private Session session;
  private LocalTime time;
  private LocalDate date;

  private boolean isSynchronized = false;
  private List<Project> projects;
  private Day today;
  private int obfuscator;

  @PostConstruct
  @Inject
  public void init(final BusinessLayer businessLayer) {
    this.businessLayer = businessLayer;

    if (businessLayer == null) {
      throw new NullPointerException("@Inject BusinessLayer");
    }

    obfuscator = (int) (OBFUSCATOR_MAX * SECURE_RANDOM.nextDouble());

    setup();
  }

  private void setup() {
    projects = businessLayer.getProjects();
    today = businessLayer.getDay(date == null ? LocalDate.now() : date);
    List<Session> sessions = today.getItems();

    if (sessions.size() > 0) {
      today.setSchedule(businessLayer.getSchedule());
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
        project = businessLayer.getProject(1);
      }
    }
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
        session = new Session(project, date, time);
        session.setDate(LocalDate.now());
        session.setStart(time);
        businessLayer.save(session);
        today.addItem(session);
      } else {
        session.setEnd(time);
        businessLayer.save(session);
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
    setup();
    isSynchronized = true;
  }

  public boolean getIsSynchronized() {
    return isSynchronized;
  }

  public void update() {
    System.out.println("Clocl#update()");
  }

}
