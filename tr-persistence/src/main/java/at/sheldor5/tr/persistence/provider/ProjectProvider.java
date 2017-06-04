package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
    this(EntityManagerHelper.getEntityManager());
  }

  public ProjectProvider(final EntityManager entityManager) {
    super(entityManager, IDENTIFIER);
  }

  public Project get(final String name) {
    if (name == null || name.isEmpty()) {
      return null;
    }

    EntityManagerHelper.beginTransaction();

    Project user = null;

    TypedQuery<Project> findByName = QueryUtils.findByField(entityManager,
        Project.class, "name", String.class, name);

    try {
      user = findByName.getSingleResult();
      EntityManagerHelper.commit();
    } catch (final Exception e) {
      EntityManagerHelper.rollback();
      e.printStackTrace();
    }

    return user;
  }
}
