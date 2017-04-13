package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.utils.QueryUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class UserMappingManager {

  public static UserMapping createUserMapping(UUID uuid) {
    Session session = DatabaseManager.getSession();
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

    return userMapping;
  }

  public static UserMapping getUserMapping(UUID uuid) {
    if (uuid == null ) {
      return null;
    }
    Session session = DatabaseManager.getSession();
    Transaction transaction = session.beginTransaction();

    TypedQuery<UserMapping> query = QueryUtils.findByField(session, UserMapping.class, "uuid", UUID.class, uuid);

    List<UserMapping> list = query.getResultList();

    UserMapping userMapping = null;

    if (list != null && list.size() == 1) {
      userMapping = list.get(0);
    }

    transaction.commit();

    return userMapping;
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
