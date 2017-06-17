package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.project.Project;
import at.sheldor5.tr.api.time.Account;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import at.sheldor5.tr.persistence.identifier.AbstractIdentifier;
import at.sheldor5.tr.persistence.utils.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Palata
 * @date 17.06.2017
 */
public class AccountProvider extends GenericProvider<Account, Integer> {

  private static final Class<Account> entityClass = Account.class;
  private static final Class<Integer> entityIdentifierClass = Integer.class;
  private static final String entityIdentifierName = "id";

  private static final AbstractIdentifier<Account, Integer> IDENTIFIER =
      new AbstractIdentifier<Account, Integer>(entityClass, entityIdentifierClass, entityIdentifierName) {
        @Override
        public Integer getIdentifier(final Account account) {
          return account == null ? -1 : account.getId();
        }
      };

  public AccountProvider() {
    this(EntityManagerHelper.createEntityManager());
  }

  public AccountProvider(final EntityManager entityManager) {
    super(entityManager, IDENTIFIER);
  }

  @Override
  public boolean exists(final Account entity) {
    if (entity.getUserMapping() == null || entity.getDate() == null) {
      return false;
    }
    TypedQuery<Long> countQuery =
        QueryUtils.countByFields(entityManager, Account.class,
            "userMapping", UserMapping.class, entity.getUserMapping(),
            "date", LocalDate.class, entity.getDate(), true);
    return countQuery.getSingleResult() > 0;
  }

  public Account get(final UserMapping userMapping, final LocalDate date) {
    if (userMapping == null || date == null) {
      return null;
    }

    Account account = null;

    TypedQuery<Account> findByFields =
        QueryUtils.findByFields(entityManager, Account.class,
            "userMapping", UserMapping.class, userMapping,
            "date", LocalDate.class, date, true);
    findByFields.setMaxResults(1);

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      final List<Account> resultList = findByFields.getResultList();
      if (resultList.size() > 0) {
        account = resultList.get(0);
      }
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return account;
  }

  public List<Account> get(final UserMapping userMapping, final LocalDate from, final LocalDate to) {
    List<Account> accounts = new ArrayList<>();

    if (userMapping == null || from == null || to == null) {
      return accounts;
    }

    TypedQuery<Account> query = QueryUtils.findByFields(entityManager, Account.class,
        "userMapping", UserMapping.class, userMapping, "date", from, to);

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();

    try {
      accounts = query.getResultList();
      transaction.commit();
    } catch (final Exception e) {
      transaction.rollback();
      e.printStackTrace();
    }

    return accounts;
  }
}
