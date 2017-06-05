package at.sheldor5.tr.web;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.provider.ProjectProvider;
import at.sheldor5.tr.persistence.provider.SessionProvider;
import at.sheldor5.tr.persistence.provider.UserProjectMappingProvider;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

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

  @Override
  public void close() {
    entityManager.close();
  }
}
