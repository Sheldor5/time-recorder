package at.sheldor5.tr.persistence.utils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

/**
 * TODO.
 */
public class QueryUtils {

  public static <E, T> TypedQuery<E> findByField(final EntityManager entityManager, Class<E> entityClass, String fieldName, Class<T> fieldType, T fieldValue) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);

    // SELECT Object FROM ObjectTable
    Root<E> entity = criteriaQuery.from(entityClass);
    criteriaQuery.select(entity);

    // WHERE fieldName = fieldValue
    ParameterExpression<T> parameter = criteriaBuilder.parameter(fieldType);
    Path path = entity.get(fieldName);
    Predicate predicate = criteriaBuilder.equal(path, parameter);

    criteriaQuery.where(predicate);

    TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(parameter, fieldValue);

    return query;
  }

  public static <E> TypedQuery<E> findLikeField(final EntityManager entityManager, Class<E> entityClass, String fieldName, String fieldPart) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);

    // SELECT Object FROM ObjectTable
    Root<E> entity = criteriaQuery.from(entityClass);
    criteriaQuery.select(entity);

    // WHERE fieldName = fieldValue
    ParameterExpression<String> parameter = criteriaBuilder.parameter(String.class);
    Path<String> path = entity.get(fieldName);
    Predicate predicate = criteriaBuilder.like(path, parameter);

    criteriaQuery.where(predicate);

    TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(parameter, String.format("%%%s%%", fieldPart));

    return query;
  }

  public static <E, F1, F2> TypedQuery<E> findByFields(
      final EntityManager entityManager, final Class<E> entityClass,
      final String field1Name, final Class<F1> field1Type, final F1 field1Value,
      final String field2Name, final Class<F2> field2Type, final F2 field2Value,
      boolean andRestriction) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);

    // SELECT Object FROM ObjectTable
    Root<E> entity = criteriaQuery.from(entityClass);
    criteriaQuery.select(entity);

    // field1Name = field1Value
    ParameterExpression<F1> parameter1 = criteriaBuilder.parameter(field1Type);
    Path path1 = entity.get(field1Name);
    Predicate predicate1 = criteriaBuilder.equal(path1, parameter1);

    // field2Name = field2Value
    ParameterExpression<F2> parameter2 = criteriaBuilder.parameter(field2Type);
    Path path2 = entity.get(field2Name);
    Predicate predicate2 = criteriaBuilder.equal(path2, parameter2);

    Predicate predicate;

    if (andRestriction) {
      // AND
      predicate = criteriaBuilder.and(predicate1, predicate2);
    } else {
      // OR
      predicate = criteriaBuilder.or(predicate1, predicate2);
    }

    // WHERE
    criteriaQuery.where(predicate);

    TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(parameter1, field1Value);
    query.setParameter(parameter2, field2Value);

    return query;
  }

  public static <E, T> TypedQuery<Long> count(final EntityManager entityManager, Class<E> entityClass, String fieldName, Class<T> fieldType, T fieldValue) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

    // SELECT COUNT(Object) FROM ObjectTable
    Root<E> entity = criteriaQuery.from(entityClass);
    criteriaQuery.select(criteriaBuilder.count(entity));

    // WHERE fieldName = fieldValue
    ParameterExpression<T> parameter = criteriaBuilder.parameter(fieldType);
    Path path = entity.get(fieldName);
    Predicate predicate = criteriaBuilder.equal(path, parameter);
    criteriaQuery.where(predicate);

    TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(parameter, fieldValue);

    return query;
  }

  public static <E, J, T> TypedQuery<E> findByMapping(final EntityManager entityManager, final Class<E> entityClass,
                                                   final Class<J> joinOnClass, final String joinOnFieldName,
                                                   final String fieldName, final Class<T> fieldType, final T fieldValue) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);

    // SELECT Object FROM ObjectTable
    Root<E> entity = criteriaQuery.from(entityClass);

    // RIGHT JOIN X ON Y
    Join<E, J> on = entity.join(joinOnFieldName);

    // WHERE fieldName = fieldValue
    ParameterExpression<T> parameter = criteriaBuilder.parameter(fieldType);
    Path path = entity.get(fieldName);
    Predicate predicate = criteriaBuilder.equal(path, parameter);
    criteriaQuery.where(predicate);

    TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(parameter, fieldValue);

    return null;
  }
}
