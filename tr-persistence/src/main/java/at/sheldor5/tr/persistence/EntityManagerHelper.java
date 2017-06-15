package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.utils.GlobalProperties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerHelper {

  private static final String PERSISTENCE_UNIT_NAME = "time-recorder";
  private static final ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<>();

  private static boolean setup = true;
  private static EntityManagerFactory entityManagerFactory;

  public static EntityManager getEntityManager() {
    EntityManager em = entityManagerThreadLocal.get();
    if (em == null) {
      em = entityManagerFactory.createEntityManager();
      em.setFlushMode(FlushModeType.COMMIT);
      entityManagerThreadLocal.set(em);
    }
    return em;
  }

  public static EntityManager createEntityManager() {
    EntityManager em = entityManagerFactory.createEntityManager();
    em.setFlushMode(FlushModeType.COMMIT);
    return em;
  }

  public static void setupPersistenceXML() {
    if (setup) {
      setup = false;
      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    } else {
      System.out.println("1");
    }
  }

  public static void setupGlobalProperties() {
    if (setup) {
      final Map<String, String> properties = new HashMap<>();
      properties.put("javax.persistence.jdbc.driver", GlobalProperties.getProperty("db.jdbc.class"));
      properties.put("javax.persistence.jdbc.url", GlobalProperties.getProperty("db.jdbc.url"));
      properties.put("javax.persistence.jdbc.user", GlobalProperties.getProperty("db.username"));
      properties.put("javax.persistence.jdbc.password", GlobalProperties.getProperty("db.password"));
      properties.put("hibernate.show_sql", GlobalProperties.getProperty("hibernate.show_sql"));
      properties.put("hibernate.hbm2ddl.auto", GlobalProperties.getProperty("hibernate.hbm2ddl.auto"));
      properties.put("current_session_context_class", "thread");
      properties.put("hibernate.globally_quoted_identifiers", "true");
      setup(properties);
    }
  }

  public static void setup(final Map properties) {
    if (setup) {
      setup = false;
      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
    } else {
      System.out.println("2");
    }
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
