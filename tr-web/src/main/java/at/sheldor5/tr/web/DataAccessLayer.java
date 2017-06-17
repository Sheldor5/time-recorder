package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.Role;
import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.mappings.UserProjectMapping;
import at.sheldor5.tr.persistence.provider.*;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Named("dataAccessLayer")
@RequestScoped
public class DataAccessLayer implements Serializable, AutoCloseable {

  private EntityManager entityManager;

  protected DataAccessLayer() {
    // CDI
  }

  @Inject
  public DataAccessLayer(final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public List<Project> getProjects(final UserMapping userMapping) {
    UserProjectMappingProvider userProjectMappingProvider = new UserProjectMappingProvider(entityManager);
    return userProjectMappingProvider.getProjects(userMapping);
  }

  public void save(final UserProjectMapping userProjectMapping) {
    UserProjectMappingProvider userProjectMappingProvider = new UserProjectMappingProvider(entityManager);
    userProjectMappingProvider.save(userProjectMapping);
  }

  public List<Project> getProjects() {
    ProjectProvider projectProvider = new ProjectProvider(entityManager);
    return projectProvider.get("");
  }

  public Project getProject(int id) {
    ProjectProvider projectProvider = new ProjectProvider(entityManager);
    return projectProvider.get(id);
  }

  public Project getProject(final String name) {
    ProjectProvider projectProvider = new ProjectProvider(entityManager);
    return projectProvider.getProject(name);
  }

  public List<Project> getProjects(final String namePart) {
    ProjectProvider projectProvider = new ProjectProvider(entityManager);
    return projectProvider.get(namePart);
  }

  public void save(final Session session) {
    SessionProvider sessionProvider = new SessionProvider(entityManager);
    sessionProvider.save(session);
  }

  public void save(final UserMapping userMapping) {
    UserMappingProvider userMappingProvider = new UserMappingProvider(entityManager);
    userMappingProvider.save(userMapping);
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

  public List<Session> getSessions(final UserMapping userMapping, final LocalDate from, final LocalDate to) {
    SessionProvider sessionProvider = new SessionProvider(entityManager);
    return sessionProvider.get(userMapping, from, to);
  }

  public Schedule getSchedule(final UserMapping userMapping) {
    ScheduleProvider scheduleProvider = new ScheduleProvider(entityManager);
    return scheduleProvider.getLatest(userMapping);
  }

  public List<Schedule> getSchedules(final UserMapping userMapping) {
    ScheduleProvider scheduleProvider = new ScheduleProvider(entityManager);
    return scheduleProvider.get(userMapping);
  }

  public List<User> getUsers() {
    UserProvider userProvider = new UserProvider(entityManager);
    return userProvider.getList("");
  }

  public User getUser(String username) {
    UserProvider userProvider = new UserProvider(entityManager);
    return userProvider.get(username);
  }

  public User getUser(UUID uuid) {
    UserProvider userProvider = new UserProvider(entityManager);
    return userProvider.get(uuid);
  }

  public void save(final User user) {
    UserProvider userProvider = new UserProvider(entityManager);
    userProvider.save(user);
  }

  public void save(final User user, Role role, List<Project> projects) {
    UserMapping usermapping = createUserMapping(user, role);
    addUserProjectMappings(usermapping, projects);
    save(user);
  }

  private UserMapping createUserMapping(User user, Role role) {
    final UserProvider userProvider = new UserProvider();
    final UserMappingProvider userMappingProvider = new UserMappingProvider();
    final UserMapping userMapping = new UserMapping();

    final User existing = userProvider.get(user.getUsername());
    if (existing == null) {
      userProvider.save(user);
      userMapping.setUuid(user.getUuid());
      userMapping.setRole(role);
      userMappingProvider.save(userMapping);
    }
    return userMapping;
  }

  public void addUserProjectMappings(UserMapping usermapping, List<Project> projects) {
    if(projects.size() > 0) {
      UserProjectMappingProvider userProjectMappingProvider = new UserProjectMappingProvider(entityManager);
      for(Project project : projects) {
        userProjectMappingProvider.save(new UserProjectMapping(usermapping, project));

      }
    }
  }

  public void save(final Schedule schedule) {
    ScheduleProvider scheduleProvider = new ScheduleProvider(entityManager);
    scheduleProvider.save(schedule);
  }

  @Override
  public void close() {
    entityManager.close();
    entityManager = null;
  }

  @PreDestroy
  public void cleanup() {
    close();
  }
}
