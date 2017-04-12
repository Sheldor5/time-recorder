package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.UuidUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

public class UserMappingManager {

  public static UserMapping createUserMapping(UUID uuid) {
    SessionFactory sessionFactory = PersistenceManager.getConfiguration().buildSessionFactory();
    Session session = sessionFactory.getCurrentSession();
    Transaction tx = session.beginTransaction();

    if (!isUnique(session, uuid)) {
      tx.rollback();
      throw new RuntimeException("UUID collision: " + uuid);
    }

    UserMapping userMapping = new UserMapping();
    userMapping.setUuid(uuid);
    session.save(userMapping);
    session.flush();
    tx.commit();
    session.close();

    return userMapping;
  }

  public static UserMapping getUserMapping(UUID uuid) {
    if (uuid == null ) {
      return null;
    }
    SessionFactory sessionFactory = PersistenceManager.getConfiguration().buildSessionFactory();
    Session session = sessionFactory.getCurrentSession();
    Transaction transaction = session.beginTransaction();

    List<UserMapping> list = findByField(session, UserMapping.class, "uuid", UUID.class, uuid);

    UserMapping userMapping = null;

    if (list != null && list.size() == 1) {
      userMapping = list.get(0);
    }

    transaction.commit();

    return userMapping;
  }

  /**
   *
   * @param session
   * @param entityClass
   * @param fieldName
   * @param fieldType
   * @param fieldValue
   * @param <E> Entity
   * @param <T> Field Type
   * @return
   */
  public static  <E, T> List<E> findByField(Session session, Class<E> entityClass, String fieldName, Class<T> fieldType, T fieldValue) {
    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
    CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);

    // SELECT Object FROM ObjectTable
    Root<E> entity = criteriaQuery.from(entityClass);
    criteriaQuery.select(entity);

    // WHERE fieldName = fieldValue
    ParameterExpression<T> parameter = criteriaBuilder.parameter(fieldType);
    Path path = entity.get(fieldName);
    Predicate predicate = criteriaBuilder.equal(path, parameter);
    criteriaQuery.where(predicate);

    TypedQuery<E> query = session.createQuery(criteriaQuery);
    query.setParameter(parameter, fieldValue);

    return query.getResultList();
  }

  private static boolean isUnique(final Session session, final UUID uuid) {
    if (session == null) {
      return false;
    }
    Query query = session.createQuery("SELECT id FROM at.sheldor5.tr.api.user.UserMapping WHERE uuid = :uuid");
    query.setParameter("uuid", uuid);

    List list = query.list();

    if (list == null || list.size() == 0) {
      return true;
    }

    list.clear();
    return false;
  }
}
