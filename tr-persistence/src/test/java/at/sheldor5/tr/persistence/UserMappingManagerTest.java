package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalProperties;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class UserMappingManagerTest {

  private static final String PROPERTIES = "test.properties";
  private static final UserMappingManager USER_MAPPING_ENGINE = new UserMappingManager();

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
  }

  @Test
  public void should_persist_and_return_user_mapping() {
    final UUID uuid = UUID.randomUUID();

    final UserMapping expected = new UserMapping(uuid);

    USER_MAPPING_ENGINE.create(expected);

    Assert.assertTrue(expected.getId() > 0);

    final UserMapping actual = USER_MAPPING_ENGINE.read(uuid);

    Assert.assertNotNull(actual);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = PersistenceException.class)
  public void should_throw_on_duplication() {
    final UUID uuid = UUID.randomUUID();

    final UserMapping original = new UserMapping(uuid);

    USER_MAPPING_ENGINE.create(original);

    Assert.assertTrue(original.getId() > 0);

    final UserMapping duplicate = new UserMapping(uuid);

    USER_MAPPING_ENGINE.create(duplicate);
  }

  @Test
  public void bulk() {
    UUID[] uuids = new UUID[10];
    for (int i = 0; i < uuids.length; i++) {
      uuids[i] = UUID.randomUUID();
      USER_MAPPING_ENGINE.create(new UserMapping(uuids[i]));
    }
    UserMapping userMapping;
    int c = 0;
    for (final UUID uuid : uuids) {
      userMapping = USER_MAPPING_ENGINE.read(uuid);
      if (userMapping != null) {
        c++;
      }
    }
    Assert.assertEquals(10, c);
  }
}
