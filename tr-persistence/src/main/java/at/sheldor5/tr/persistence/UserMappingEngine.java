package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.utils.QueryUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class UserMappingEngine implements Engine<UserMapping, UUID> {

  @Override
  public UserMapping create(final UserMapping userMapping) {
    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    try {
      session.save(userMapping);
      session.flush();
      tx.commit();
    } catch (final Exception e) {
      tx.rollback();
      throw e;
    }

    return userMapping;
  }

  @Override
  public UserMapping read(final UUID uuid) {
    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    TypedQuery<UserMapping> query = QueryUtils.findByField(session, UserMapping.class, "uuid", UUID.class, uuid);

    List<UserMapping> list = query.getResultList();


    UserMapping userMapping = null;

    if (list != null && list.size() == 1) {
      userMapping = list.get(0);
    }

    tx.commit();

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
