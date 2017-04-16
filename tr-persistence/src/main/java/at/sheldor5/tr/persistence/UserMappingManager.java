package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.utils.QueryUtils;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserMappingManager implements Engine<UserMapping, UUID> {

  @Override
  public UserMapping create(final UserMapping userMapping) {
    EntityManager entityManager = EntityManagerHelper.getEntityManager();
    EntityManagerHelper.beginTransaction();

    UserMapping result = null;

    try {
      entityManager.persist(userMapping);
      EntityManagerHelper.commit();
      result = userMapping;
    } catch (final Exception e) {
      EntityManagerHelper.rollback();
      e.printStackTrace();
      throw e;
    }

    EntityManagerHelper.closeEntityManager();

    return result;
  }

  @Override
  public UserMapping read(final UUID uuid) {
    EntityManager entityManager = EntityManagerHelper.getEntityManager();
    EntityManagerHelper.beginTransaction();

    UserMapping userMapping = null;

    TypedQuery<UserMapping> query = QueryUtils.findByField(entityManager, UserMapping.class, "uuid", UUID.class, uuid);
    try {
      List<UserMapping> list = query.getResultList();
      if (list != null) {
        if (list.size() == 1) {
          userMapping = list.get(0);
        } else if (list.size() > 1) {
          System.out.println("duplications found");
        }
      }
      EntityManagerHelper.commit();
    } catch (final Exception e) {
      EntityManagerHelper.rollback();
      e.printStackTrace();
    }
    EntityManagerHelper.closeEntityManager();
    return userMapping;
  }

  @Override
  public void update(UserMapping userMapping) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(UUID uuid) {
    throw new UnsupportedOperationException();
  }
}
