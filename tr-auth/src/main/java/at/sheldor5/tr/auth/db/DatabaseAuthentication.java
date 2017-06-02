package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.user.User;
import static at.sheldor5.tr.persistence.EntityManagerHelper.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import at.sheldor5.tr.persistence.provider.UserProvider;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseAuthentication implements AuthenticationPlugin {

  public static final String NAME = "tr-db";

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

    try (final UserProvider userProvider = new UserProvider()) {
      userProvider.save(user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public User getUser(final String username, final String plainTextPassword) {
    System.out.println(username);

    User user = null;

    try (final UserProvider userProvider = new UserProvider()) {
      user = userProvider.get(username);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (user == null || !BCrypt.checkpw(plainTextPassword, user.getPassword())) {
      return null;
    }

    return user;
  }
}
