package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import static at.sheldor5.tr.persistence.EntityManagerHelper.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseAuthentication implements AuthenticationPlugin {

  private static final String NAME = "tr-db";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void initialize() throws IllegalStateException {

  }

  @Override
  public void saveUser(final User user) throws UnsupportedOperationException {
    if (!UserValidator.validate(user)) {
      return;
    }

    EntityManager entityManager = getEntityManager();
    beginTransaction();

    try {
      entityManager.persist(user);
      commit();
    } catch (final EntityExistsException eee) {
      try {
        rollback();
        beginTransaction();
        entityManager.merge(user);
        commit();
      } catch (final Exception e) {
        rollback();
        eee.printStackTrace();
        e.printStackTrace();
      }
    } catch (final Exception e) {
      rollback();
      e.printStackTrace();
    }

    closeEntityManager();
  }

  @Override
  public User getUser(final String username, final String plainTextPassword) {
    EntityManager entityManager = getEntityManager();
    beginTransaction();

    User user = entityManager.find(User.class, username);

    commit();

    if (user == null || !BCrypt.checkpw(plainTextPassword, user.getPassword())) {
      return null;
    }

    return user;
  }
}
