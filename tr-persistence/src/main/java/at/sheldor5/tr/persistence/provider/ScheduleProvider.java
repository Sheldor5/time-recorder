package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Palata
 * @date 21.05.2017
 */
public class ScheduleProvider extends GenericProvider<Schedule, Integer> {

  private static final Class<Schedule> entityClass = Schedule.class;
  private static final Class<Integer> entityIdentifierClass = Integer.class;
  private static final String entityIdentifierName = "id";

  private static final AbstractIdentifier<Schedule, Integer> IDENTIFIER =
      new AbstractIdentifier<Schedule, Integer>(entityClass, entityIdentifierClass, entityIdentifierName) {
        @Override
        public Integer getIdentifier(final Schedule schedule) {
          return schedule == null ? null : schedule.getId();
        }
      };

  public ScheduleProvider() {
    this(EntityManagerHelper.createEntityManager());
  }

  public ScheduleProvider(final EntityManager entityManager) {
    super(entityManager, IDENTIFIER);
  }

  public List<Schedule> get(final UserMapping userMapping) {
    if (userMapping == null) {
      return new ArrayList<>();
    }

    TypedQuery<Schedule> findByField = QueryUtils.findByFieldOrdered(entityManager, Schedule.class, "userMapping", UserMapping.class, userMapping, "dueDate", false);

    List<Schedule> schedules;

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      schedules = findByField.getResultList();
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      schedules = new ArrayList<>();
      e.printStackTrace();
    }

    return schedules;
  }

  public Schedule getLatest(final UserMapping userMapping) {
    if (userMapping == null) {
      return null;
    }

    final TypedQuery<Schedule> findByFields = QueryUtils.findByFieldOrdered(entityManager, Schedule.class, "userMapping", UserMapping.class, userMapping, "dueDate", false);
    findByFields.setMaxResults(1);

    Schedule schedule = null;

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      final List<Schedule> resultList = findByFields.getResultList();
      if (resultList.size() == 1) {
        schedule = resultList.get(0);
      }
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return schedule;
  }

  public Schedule getFirst(final UserMapping userMapping, final LocalDate date) {
    if (userMapping == null || date == null) {
      return null;
    }

    Schedule schedule = null;

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Schedule> criteriaQuery = criteriaBuilder.createQuery(entityClass);

    // SELECT Object FROM ObjectTable
    Root<Schedule> entity = criteriaQuery.from(entityClass);
    criteriaQuery.select(entity);

    Order order = criteriaBuilder.desc(entity.get("date"));
    criteriaQuery.orderBy(order);

    // WHERE field1Name = field1Value
    ParameterExpression<UserMapping> parameter1 = criteriaBuilder.parameter(UserMapping.class);
    Path<UserMapping> path1 = entity.get("userMapping");
    Predicate predicate1 = criteriaBuilder.equal(path1, parameter1);

    // field2Name <= field2Value
    Expression<LocalDate> path2 = entity.get("date");
    Predicate predicate2 = criteriaBuilder.lessThanOrEqualTo(path2, date);

    Predicate predicate = criteriaBuilder.and(predicate1, predicate2);
    criteriaQuery.where(predicate);

    TypedQuery<Schedule> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(parameter1, userMapping);
    query.setMaxResults(1);

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      final List<Schedule> resultList = query.getResultList();
      if (resultList.size() == 1) {
        schedule = resultList.get(0);
      }
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return schedule;
  }
}
