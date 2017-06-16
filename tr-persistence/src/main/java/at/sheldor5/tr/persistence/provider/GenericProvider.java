package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.io.Closeable;

public class GenericProvider<Entity, Identifier> implements EntityProvider<Entity, Identifier>, Closeable {

  final EntityManager entityManager;

  private final AbstractIdentifier<Entity, Identifier> identifier;

  public GenericProvider(final EntityManager entityManager, AbstractIdentifier<Entity, Identifier> identifier) {
    this.entityManager = entityManager;
    this.identifier = identifier;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public <T> long count(String fieldName, Class<T> fieldType, T fieldValue) {
    TypedQuery<Long> countQuery =
        QueryUtils.countByField(entityManager,
            identifier.getEntityClass(),
            fieldName,
            fieldType,
            fieldValue);
    return countQuery.getSingleResult();
  }

  @Override
  public boolean exists(Entity entity) {
    long count = count(identifier.getIdentifierFieldName(), identifier.getEntityIdentifierType(), identifier.getIdentifier(entity));
    return count > 0;
  }

  @Override
  public Entity save(Entity entity) {
    if (entity == null) {
      return null;
    }

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    Entity result = null;

    if (exists(entity)) {
      // update
      try {
        entityManager.merge(entity);
        transaction.commit();
        result = entity;
      } catch (final Exception e) {
        transaction.rollback();
        e.printStackTrace();
      }
    } else {
      // insert
      try {
        entityManager.persist(entity);
        transaction.commit();
        result = entity;
      } catch (Exception e) {
        e.printStackTrace();
        transaction.rollback();
      }
    }

    return result;
  }

  @Override
  public Entity get(Identifier id) {
    if (identifier == null) {
      return null;
    }

    Entity entity = null;

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      entity = entityManager.find(identifier.getEntityClass(), id);
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return entity;
  }

  @Override
  public void delete(Entity entity) {
    if (entity == null) {
      return;
    }

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      entityManager.remove(entity);
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }
  }

  public void close() {
    entityManager.close();
  }
}
