package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

public class UserProvider extends GenericProvider<User, UUID> {

  private static final Class<User> entityClass = User.class;
  private static final Class<UUID> entityIdentifierClass = UUID.class;
  private static final String entityIdentifierName = "uuid";

  private static final AbstractIdentifier<User, UUID> IDENTIFIER =
      new AbstractIdentifier<User, UUID>(entityClass, entityIdentifierClass, entityIdentifierName) {
        @Override
        public UUID getIdentifier(final User user) {
          return user == null ? null : user.getUuid();
        }
      };

  public UserProvider() {
    this(EntityManagerHelper.createEntityManager());
  }

  public UserProvider(final EntityManager entityManager) {
    super(entityManager, IDENTIFIER);
  }

  public User get(final String username) {
    if (username == null || username.isEmpty()) {
      return null;
    }

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    User user = null;

    TypedQuery<User> findByUsername = QueryUtils.findByField(entityManager,
        User.class, "username", String.class, username);

    try {
      List<User> users = findByUsername.getResultList();
      if (users.size() == 1) {
        user = users.get(0);
      }
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return user;
  }
}
