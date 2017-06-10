package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.provider.ProjectProvider;
import at.sheldor5.tr.persistence.provider.SessionProvider;
import at.sheldor5.tr.persistence.provider.UserProjectMappingProvider;
import at.sheldor5.tr.persistence.provider.UserProvider;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Named
@RequestScoped
public class DataProvider implements Serializable, AutoCloseable {

  @Inject
  private EntityManager entityManager;

  public DataProvider() {

  }

  public DataProvider(final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public List<Project> getProjects(final UserMapping userMapping) {
    UserProjectMappingProvider userProjectMappingProvider = new UserProjectMappingProvider(entityManager);
    return userProjectMappingProvider.getProjects(userMapping);
  }

  public List<Project> getProjects() {
    ProjectProvider projectProvider = new ProjectProvider(entityManager);
    return projectProvider.get("");
  }

  public Project getProject(int id) {
    ProjectProvider projectProvider = new ProjectProvider(entityManager);
    return projectProvider.get(id);
  }

  public List<Project> getProjects(final String namePart) {
    ProjectProvider projectProvider = new ProjectProvider(entityManager);
    return projectProvider.get(namePart);
  }

  public void save(final Session session) {
    SessionProvider sessionProvider = new SessionProvider(entityManager);
    sessionProvider.save(session);
  }

  public void save(final Project project) {
    ProjectProvider projectProvider = new ProjectProvider(entityManager);
    projectProvider.save(project);
  }

  public List<Session> getSessions(final UserMapping userMapping, final LocalDate date) {
    SessionProvider sessionProvider = new SessionProvider(entityManager);
    return sessionProvider.get(userMapping, date);
  }

  public Day getDay(final UserMapping userMapping, final LocalDate date) {
    SessionProvider sessionProvider = new SessionProvider(entityManager);
    Day day = new Day(date);
    day.setItems(sessionProvider.get(userMapping, date));
    return day;
  }

  public Month getMonth(final UserMapping userMapping, final LocalDate date) {
    SessionProvider sessionProvider = new SessionProvider(entityManager);
    Month month = new Month(date);

    int y = date.getYear();
    int m = date.getMonthValue();

    LocalDate from = LocalDate.of(y, m, 1);
    LocalDate to = LocalDate.of(y, m, date.lengthOfMonth());

    List<Session> sessions = sessionProvider.get(userMapping, from, to);

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

  public List<User> getUsers() {
    UserProvider userProvider = new UserProvider(entityManager);
    return userProvider.getList("");
  }

  public User getUser(UUID uuid) {
    UserProvider userProvider = new UserProvider(entityManager);
    return userProvider.get(uuid);
  }

  public void save(final User user) {
    UserProvider userProvider = new UserProvider(entityManager);
    userProvider.save(user);
  }

  @Override
  public void close() {
    entityManager.close();
  }
}
