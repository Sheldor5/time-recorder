package at.sheldor5.tr.persistence.user;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.StringUtils;
import at.sheldor5.tr.persistence.DatabaseManager;
import at.sheldor5.tr.persistence.utils.QueryUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by micha on 14.04.2017.
 */
public class DatabaseAuthentication implements AuthenticationPlugin {

  private static final String NAME = "tr-db";
  private static final String SALT = "w€]]@@@@w";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void initialize() throws IllegalStateException {

  }

  @Override
  public void addUser(User user, String plainTextPassword) throws UnsupportedOperationException {
    if (user == null) {
      return;
    }

    if (plainTextPassword != null && !plainTextPassword.isEmpty()) {
      user.setPlainTextPassword(plainTextPassword);
    }

    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    session.save(user);
    session.flush();
    tx.commit();
  }

  @Override
  public User getUser(String username, String plainTextPassword) {
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
      if (users.size() == 1) {
        user = users.get(0);
      }
      users.clear();
    }

    tx.commit();

    return user;
  }
}
