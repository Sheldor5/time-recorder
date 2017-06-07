package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.user.Schedule;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;

import javax.persistence.EntityManager;

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
}
