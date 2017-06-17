package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.time.Account;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.persistence.TestFixture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Michael Palata
 * @date 17.06.2017
 */
public class AccountProviderTest extends TestFixture {

  private static final UserMappingProvider USER_MAPPING_PROVIDER = new UserMappingProvider();
  private static final AccountProvider ACCOUNT_ENGINE = new AccountProvider();

  private static UserMapping userMapping;

  @Before
  public void setup() throws IOException, SQLException {
    UserMapping userMapping = new UserMapping(UUID.randomUUID());
    USER_MAPPING_PROVIDER.save(userMapping);
    Assert.assertTrue(userMapping.getId() > 0);
    AccountProviderTest.userMapping = userMapping;
  }

  @Test
  public void test_persist_account() {
    final LocalDate date = LocalDate.of(2017, 1, 31);
    String name;
    final Account account = new Account(date);
    account.setUserMapping(userMapping);

    ACCOUNT_ENGINE.save(account);
    Assert.assertTrue(account.getId() > 0);

    final Account actual = ACCOUNT_ENGINE.get(userMapping, date);
    Assert.assertNotNull(actual);
    Assert.assertEquals(account, actual);

    actual.setTime(1337L);

    ACCOUNT_ENGINE.save(account);

    Assert.assertTrue(account.getId() > 0);

    final Account actual2 = ACCOUNT_ENGINE.get(userMapping, date);
    Assert.assertNotNull(actual2);
    Assert.assertEquals(1337L, actual2.getTime());
  }
}
