package at.sheldor5.tr.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

public class EntityManagerHelper {

  private static final String PERSISTENCE_UNIT_NAME = "time-recorder";

  private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
  private static final ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<>();

  public static EntityManager getEntityManager() {
    EntityManager em = entityManagerThreadLocal.get();
    if (em == null) {
      em = entityManagerFactory.createEntityManager();
      em.setFlushMode(FlushModeType.COMMIT);
      entityManagerThreadLocal.set(em);
    }
    return em;
  }

  public static void closeEntityManager() {
    EntityManager em = entityManagerThreadLocal.get();
    if (em != null) {
      em.close();
      entityManagerThreadLocal.set(null);
    }
  }

  public static void closeEntityManagerFactory() {
    entityManagerFactory.close();
  }

  public static void beginTransaction() {
    getEntityManager().getTransaction().begin();
  }

  public static void rollback() {
    getEntityManager().getTransaction().rollback();
  }

  public static void commit() {
    getEntityManager().getTransaction().commit();
  }
}
