package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalProperties;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class UserMappingEngineTest {

  private static final String PROPERTIES = "test.properties";
  private static final UserMappingEngine USER_MAPPING_ENGINE = new UserMappingEngine();

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
    DatabaseManager.init();
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

  @Test(expected = ConstraintViolationException.class)
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

    final StringBuilder sb = new StringBuilder();
    UserMapping userMapping;
    for (int i = 0; i < uuids.length; i++) {
      userMapping = USER_MAPPING_ENGINE.read(uuids[i]);
      sb.append(userMapping);
      sb.append("\n");
    }
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(sb.toString());
  }
}
