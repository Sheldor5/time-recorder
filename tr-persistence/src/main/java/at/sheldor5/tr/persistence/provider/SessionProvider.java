package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
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
    this(EntityManagerHelper.getEntityManager());
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
}
