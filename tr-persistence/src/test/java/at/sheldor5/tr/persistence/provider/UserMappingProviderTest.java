package at.sheldor5.tr.persistence.provider;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.persistence.DuplicationException;
import at.sheldor5.tr.persistence.TestFixture;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class UserMappingProviderTest extends TestFixture {

  private static final UserMappingProvider USER_MAPPING_PROVIDER = new UserMappingProvider();

  @Test
  public void should_persist_and_return_user_mapping() {
    final UUID uuid = UUID.randomUUID();

    final UserMapping expected = new UserMapping(uuid);

    USER_MAPPING_PROVIDER.save(expected);

    Assert.assertTrue(expected.getId() > 0);

    final UserMapping actual = USER_MAPPING_PROVIDER.get(uuid);

    Assert.assertNotNull(actual);
    Assert.assertEquals(expected, actual);
  }

  /*@Test(expected = DuplicationException.class)
  public void should_throw_on_duplication() {
    final UUID uuid = UUID.randomUUID();

    final UserMapping original = new UserMapping(uuid);

    USER_MAPPING_PROVIDER.save(original);

    Assert.assertTrue(original.getId() > 0);

    final UserMapping duplicate = new UserMapping(uuid);

    USER_MAPPING_PROVIDER.save(duplicate);
  }*/

  @Test
  public void bulk() {
    UUID[] uuids = new UUID[10];
    for (int i = 0; i < uuids.length; i++) {
      uuids[i] = UUID.randomUUID();
      USER_MAPPING_PROVIDER.save(new UserMapping(uuids[i]));
    }
    UserMapping userMapping;
    int c = 0;
    for (final UUID uuid : uuids) {
      userMapping = USER_MAPPING_PROVIDER.get(uuid);
      if (userMapping != null) {
        c++;
      }
    }
    Assert.assertEquals(10, c);
  }

  @AfterClass
  public static void teardown() {
    USER_MAPPING_PROVIDER.close();
  }
}
