package at.sheldor5.tr.persistence.identifier;

public abstract class AbstractIdentifier<Entity, Identifier> {

  private final Class<Entity> entityClass;
  private final Class<Identifier> entityIdentifierType;
  private final String identifierFieldName;

  public AbstractIdentifier(final Class<Entity> entityClass, final Class<Identifier> entityIdentifierType, final String identifierFieldName) {
    this.entityClass = entityClass;
    this.entityIdentifierType = entityIdentifierType;
    this.identifierFieldName = identifierFieldName;
  }

  public Class<Entity> getEntityClass() {
    return entityClass;
  }

  public Class<Identifier> getEntityIdentifierType() {
    return entityIdentifierType;
  }

  public String getIdentifierFieldName() {
    return identifierFieldName;
  }

  public abstract Identifier getIdentifier(Entity entity);

}
