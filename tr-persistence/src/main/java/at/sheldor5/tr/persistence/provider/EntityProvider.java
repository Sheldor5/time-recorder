package at.sheldor5.tr.persistence.provider;

/**
 *
 * @param <Entity> Type.
 * @param <Identifier> Identifier.
 */
public interface EntityProvider<Entity, Identifier> {

  Entity save(Entity entity);

  Entity get(Identifier identifier);

  void delete(Entity entity);

  boolean exists(Entity entity);

  <T> long count(String fieldName, Class<T> fieldType, T fieldValue);

}
