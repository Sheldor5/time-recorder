package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Account;
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
import java.util.logging.Logger;

@Named("dataAccessLayer")
@RequestScoped
public class DataAccessLayer implements Serializable, AutoCloseable {

  private static final Logger LOGGER = Logger.getLogger(DataAccessLayer.class.getName());

  private final EntityManager entityManager;

  private final UserProvider userProvider;
  private final UserMappingProvider userMappingProvider;
  private final ProjectProvider projectProvider;
  private final UserProjectMappingProvider userProjectMappingProvider;
  private final SessionProvider sessionProvider;
  private final ScheduleProvider scheduleProvider;
  private final AccountProvider accountProvider;

  protected DataAccessLayer() {
    // CDI
    this(null);
  }

  @Inject
  public DataAccessLayer(final EntityManager entityManager) {
    this.entityManager = entityManager;
    this.userProvider = new UserProvider(entityManager);
    this.userMappingProvider = new UserMappingProvider(entityManager);
    this.projectProvider = new ProjectProvider(entityManager);
    this.userProjectMappingProvider = new UserProjectMappingProvider(entityManager);
    this.sessionProvider = new SessionProvider(entityManager);
    this.scheduleProvider = new ScheduleProvider(entityManager);
    this.accountProvider = new AccountProvider(entityManager);
  }

  public List<Project> getProjects(final UserMapping userMapping) {
    return userProjectMappingProvider.getProjects(userMapping);
  }

  public void save(final UserProjectMapping userProjectMapping) {
    userProjectMappingProvider.save(userProjectMapping);
  }

  public List<Project> getProjects() {
    return projectProvider.get("");
  }

  public Project getProject(int id) {
    return projectProvider.get(id);
  }

  public Project getProject(final String name) {
    return projectProvider.getProject(name);
  }

  public List<Project> getProjects(final String namePart) {
    return projectProvider.get(namePart);
  }

  public void save(final Session session) {
    sessionProvider.save(session);
  }

  public void save(final UserMapping userMapping) {
    userMappingProvider.save(userMapping);
  }

  public void save(final Project project) {
    projectProvider.save(project);
  }

  public List<Session> getSessions(final UserMapping userMapping, final LocalDate date) {
    return sessionProvider.get(userMapping, date);
  }

  public Day getDay(final UserMapping userMapping, final LocalDate date) {
    Day day = new Day(date);
    day.setItems(sessionProvider.get(userMapping, date));
    return day;
  }

  public List<Session> getSessions(final UserMapping userMapping, final LocalDate from, final LocalDate to) {
    return sessionProvider.get(userMapping, from, to);
  }

  public Schedule getSchedule(final UserMapping userMapping) {
    return scheduleProvider.getLatest(userMapping);
  }

  public List<Schedule> getSchedules(final UserMapping userMapping) {
    return scheduleProvider.get(userMapping);
  }

  public List<User> getUsers() {
    return userProvider.getList("");
  }

  public User getUser(String username) {
    return userProvider.get(username);
  }

  public Account getAccount(final UserMapping userMapping, final LocalDate date) {
    return accountProvider.get(userMapping, date);
  }

  public List<Account> getAccounts(final UserMapping userMapping, final LocalDate from, final LocalDate to) {
    return accountProvider.get(userMapping, from, to);
  }

  public void save(final Account account) {
    accountProvider.save(account);
  }

  public User getUser(UUID uuid) {
    return userProvider.get(uuid);
  }

  public void save(final User user) {
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
    scheduleProvider.save(schedule);
  }

  @Override
  public void close() {
    try {
      entityManager.close();
    } catch (final Exception e) {
      LOGGER.warning(String.format("Error closing EntityManager: %s", e.getMessage()));
    }
  }

  @PreDestroy
  public void cleanup() {
    close();
  }
}
