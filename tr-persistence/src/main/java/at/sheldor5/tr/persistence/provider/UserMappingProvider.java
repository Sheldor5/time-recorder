package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.DuplicationException;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.UUID;

public class UserMappingProvider extends GenericProvider<UserMapping, Integer> {

  private static final Class<UserMapping> entityClass = UserMapping.class;
  private static final Class<Integer> entityIdentifierClass = Integer.class;
  private static final String entityIdentifierName = "id";

  private static final AbstractIdentifier<UserMapping, Integer> IDENTIFIER =
      new AbstractIdentifier<UserMapping, Integer>(entityClass, entityIdentifierClass, entityIdentifierName) {
        @Override
        public Integer getIdentifier(final UserMapping userMapping) {
          return userMapping == null ? null : userMapping.getId();
        }
      };

  public UserMappingProvider() {
    this(EntityManagerHelper.createEntityManager());
  }

  public UserMappingProvider(final EntityManager entityManager) {
    super(entityManager, IDENTIFIER);
  }

  @Override
  public boolean exists(UserMapping userMapping) {
    long count = count("uuid", UUID.class, userMapping.getUuid());
    if (count != 0) {
      throw new DuplicationException(userMapping.getUuid());
    }
    return false;
  }

  public UserMapping get(final UUID uuid) {
    if (uuid == null) {
      return null;
    }

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    UserMapping userMapping = null;

    TypedQuery<UserMapping> findByUuid = QueryUtils.findByField(entityManager,
        UserMapping.class, "uuid", UUID.class, uuid);

    try {
      userMapping = findByUuid.getSingleResult();
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return userMapping;
  }
}
