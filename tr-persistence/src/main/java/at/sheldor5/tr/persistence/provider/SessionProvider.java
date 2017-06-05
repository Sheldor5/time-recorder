package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SessionProvider extends GenericProvider<Session, Integer> {

  private static final Class<Session> entityClass = Session.class;
  private static final Class<Integer> entityIdentifierClass = Integer.class;
  private static final String entityIdentifierName = "id";

  private static final AbstractIdentifier<Session, Integer> IDENTIFIER =
      new AbstractIdentifier<Session, Integer>(entityClass, entityIdentifierClass, entityIdentifierName) {
        @Override
        public Integer getIdentifier(final Session session) {
          return session == null ? -1 : session.getId();
        }
      };

  public SessionProvider() {
    this(EntityManagerHelper.createEntityManager());
  }

  public SessionProvider(final EntityManager entityManager) {
    super(entityManager, IDENTIFIER);
  }

  public List<Session> get(final LocalDate from, final LocalDate to) {
    if (from == null || to == null) {
      return null;
    }

    return null;
  }

  public List<Session> get(final UserMapping userMapping, final LocalDate date) {
    if (userMapping == null || date == null) {
      return new ArrayList<>();
    }

    List<Session> sessions = new ArrayList<>();

    TypedQuery<Session> findByFields = QueryUtils.findByFields(entityManager, Session.class,
        "userMapping", UserMapping.class, userMapping, "date", LocalDate.class, date, true);

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      sessions = findByFields.getResultList();
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return sessions;
  }
}
