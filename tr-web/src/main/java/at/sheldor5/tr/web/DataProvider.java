package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;
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
import java.time.LocalTime;
import java.util.ArrayList;
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

  public List<Session> getSessions(final UserMapping userMapping, final LocalDate from, final LocalDate to) {
    SessionProvider sessionProvider = new SessionProvider(entityManager);
    //sessionProvider.get(from, to);
    return getMockupSessions();
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
