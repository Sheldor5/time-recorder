package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.UuidUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.UUID;

public class UserManager {

  public User getUser(UUID uuid) {
    SessionFactory sessionFactory = PersistenceManager.getConfiguration().buildSessionFactory();
    Session session = sessionFactory.getCurrentSession();
    Transaction tx = session.beginTransaction();

    User result = session.get(User.class, UuidUtils.getBytes(uuid));

    tx.commit();

    return result;
  }

}
