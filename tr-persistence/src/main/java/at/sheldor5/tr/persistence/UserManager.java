package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.StringUtils;
import at.sheldor5.tr.api.utils.UuidUtils;
import at.sheldor5.tr.persistence.utils.QueryUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class UserManager {

  public static void save(final User user) {
    if (user == null) {
      return;
    }

    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    session.save(user);
    session.flush();
    tx.commit();
  }

  public static User getUser(final String username, final String plainTextPassword) {
    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    final String password = StringUtils.getMD5(plainTextPassword);

    TypedQuery<User> query = QueryUtils.findByFields(
            session, User.class,
            "username", String.class, username,
            "password", String.class, password, true);

    List<User> users = query.getResultList();

    User user = null;
    if (users != null) {
      if (users != null) {
        if (users.size() == 1) {
          user = users.get(0);
        }
        users.clear();
      }
    }

    tx.commit();

    return user;
  }

}
