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
      return new Schedule();
    }

    TypedQuery<Schedule> findByFields = QueryUtils.findByFieldOrdered(entityManager, Schedule.class, "userMapping", UserMapping.class, userMapping, "dueDate", false);
    findByFields.setMaxResults(1);

    Schedule schedule = null;

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      schedule = findByFields.getSingleResult();
    } catch (final NoResultException nre) {
      // SELECT returns no Entity
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      transaction.commit();
    }

    return schedule;
  }
}
