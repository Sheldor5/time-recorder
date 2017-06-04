package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Closeable;

public class GenericProvider<Entity, Identifier> implements EntityProvider<Entity, Identifier>, Closeable {

  @PersistenceContext(unitName = "time-recorder")
  protected final EntityManager entityManager;

  protected final AbstractIdentifier<Entity, Identifier> identifier;

  public GenericProvider(final AbstractIdentifier<Entity, Identifier> identifier) {
    this(EntityManagerHelper.getEntityManager(), identifier);
  }

  public GenericProvider(final EntityManager entityManager, AbstractIdentifier<Entity, Identifier> identifier) {
    this.entityManager = entityManager;
    this.identifier = identifier;
  }

  @Override
  public <T> long count(String fieldName, Class<T> fieldType, T fieldValue) {
    TypedQuery<Long> countQuery =
        QueryUtils.count(entityManager,
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

    EntityManagerHelper.beginTransaction();

    Entity result = null;

    if (exists(entity)) {
      // update
      try {
        entityManager.merge(entity);
        EntityManagerHelper.commit();
        result = entity;
      } catch (final Exception e) {
        EntityManagerHelper.rollback();
        e.printStackTrace();
      }
    } else {
      // insert
      try {
        entityManager.persist(entity);
        EntityManagerHelper.commit();
        result = entity;
      } catch (Exception e) {
        e.printStackTrace();
        EntityManagerHelper.rollback();
      }
    }

    return result;
  }

  @Override
  public Entity get(Identifier id) {
    if (identifier == null) {
      return null;
    }

    EntityManagerHelper.beginTransaction();

    Entity entity = null;

    try {
      entity = entityManager.find(identifier.getEntityClass(), id);
      EntityManagerHelper.commit();
    } catch (final Exception e) {
      EntityManagerHelper.rollback();
      e.printStackTrace();
    }

    return entity;
  }

  @Override
  public void delete(Entity entity) {
    if (entity == null) {
      return;
    }

    EntityManagerHelper.beginTransaction();

    try {
      entityManager.remove(entity);
      EntityManagerHelper.commit();
    } catch (final Exception e) {
      EntityManagerHelper.rollback();
      e.printStackTrace();
    }
  }

  public void close() {
    EntityManagerHelper.closeEntityManager();
  }
}
