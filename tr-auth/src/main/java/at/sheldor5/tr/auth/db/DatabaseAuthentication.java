package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.StringUtils;
import at.sheldor5.tr.persistence.DatabaseManager;
import at.sheldor5.tr.persistence.utils.QueryUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.TypedQuery;
import java.util.List;

public class DatabaseAuthentication implements AuthenticationPlugin {

  private static final String NAME = "tr-db";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void initialize() throws IllegalStateException {
    DatabaseManager.getConfiguration().addResource("User.hbm.xml");
  }

  @Override
  public void saveUser(final User user) throws UnsupportedOperationException {
    if (!UserValidator.validate(user)) {
      return;
    }

    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    session.saveOrUpdate(user);
    session.flush();

    tx.commit();
  }

  @Override
  public User getUser(final String username, final String plainTextPassword) {
    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    User user = session.get(User.class, username);

    tx.commit();

    if (user == null || !BCrypt.checkpw(plainTextPassword, user.getPassword())) {
      return null;
    }

    return user;
  }
}
