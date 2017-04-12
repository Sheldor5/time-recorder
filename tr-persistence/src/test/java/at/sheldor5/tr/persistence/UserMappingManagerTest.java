package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.api.utils.UuidUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserMappingManagerTest {

  private static final String PROPERTIES = "test.properties";

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
  }

  @Test
  public void test_configuration() {
    final Configuration configuration = PersistenceManager.getConfiguration();
    Assert.assertNotNull(configuration);
  }

  @Test
  public void should_create_user_mapping() {
    final UUID expected = UUID.randomUUID();

    final UserMapping actual = UserMappingManager.createUserMapping(expected);

    Assert.assertNotNull(actual);

    Assert.assertEquals(expected, actual.getUuid());
    Assert.assertTrue(actual.getId() > 0);
  }

  @Test
  public void should_return_user_mapping() {
    final UUID uuid = UUID.randomUUID();

    final UserMapping expected = UserMappingManager.createUserMapping(uuid);
    Assert.assertNotNull(expected);
    Assert.assertEquals(uuid, expected.getUuid());
    Assert.assertTrue(expected.getId() > 0);

    final UserMapping actual = UserMappingManager.getUserMapping(uuid);

    Assert.assertNotNull(actual);

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void bulk() {
    UUID[] uuids = new UUID[10];
    for (int i = 0; i < uuids.length; i++) {
      uuids[i] = UUID.randomUUID();
      UserMappingManager.createUserMapping(uuids[i]);
    }

    UserMapping userMapping;
    for (int i = 0; i < uuids.length; i++) {
      userMapping = UserMappingManager.getUserMapping(uuids[i]);
      System.out.println(userMapping);
    }
  }

}
