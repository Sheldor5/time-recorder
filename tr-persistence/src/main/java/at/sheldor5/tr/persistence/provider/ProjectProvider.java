package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class ProjectProvider extends GenericProvider<Project, Integer> {

  private static final Class<Project> entityClass = Project.class;
  private static final Class<Integer> entityIdentifierClass = Integer.class;
  private static final String entityIdentifierName = "id";

  private static final AbstractIdentifier<Project, Integer> IDENTIFIER =
      new AbstractIdentifier<Project, Integer>(entityClass, entityIdentifierClass, entityIdentifierName) {
        @Override
        public Integer getIdentifier(final Project project) {
          return project == null ? null : project.getId();
        }
      };

  public ProjectProvider() {
    this(EntityManagerHelper.createEntityManager());
  }

  public ProjectProvider(final EntityManager entityManager) {
    super(entityManager, IDENTIFIER);
  }

  @Override
  public boolean exists(final Project project) {
    long count = count("name", String.class, project.getName());
    return count > 0;
  }

  public Project getProject(final String name) {
    if (name == null) {
      return null;
    }

    Project project = null;

    TypedQuery<Project> findByName = QueryUtils.findByField(entityManager,
        Project.class, "name", String.class, name);
    findByName.setMaxResults(1);

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      final List<Project> resultList = findByName.getResultList();
      if (resultList.size() > 0) {
        project = resultList.get(0);
      }
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return project;
  }

  public List<Project> get(final String namePart) {
    if (namePart == null) {
      return new ArrayList<>();
    }

    List<Project> projects = new ArrayList<>();

    TypedQuery<Project> findByName = QueryUtils.findLikeField(entityManager,
        Project.class, "name", namePart);

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      projects = findByName.getResultList();
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return projects;
  }
}
