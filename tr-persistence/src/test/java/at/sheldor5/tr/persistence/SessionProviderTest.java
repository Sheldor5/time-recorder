package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import at.sheldor5.tr.persistence.provider.SessionProvider;
import at.sheldor5.tr.persistence.provider.UserMappingProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SessionProviderTest {

  private static final String PROPERTIES = "test.properties";
  private static final UserMappingProvider USER_MAPPING_ENGINE = new UserMappingProvider();
  private static final SessionProvider SESSION_PROVIDER = new SessionProvider();

  private static final LocalDate DATE = LocalDate.of(2017, 1, 1);

  private static UserMapping userMapping;

  @BeforeClass
  public static void setup() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
    UserMapping userMapping = new UserMapping(UUID.randomUUID());
    USER_MAPPING_ENGINE.save(userMapping);
    Assert.assertTrue(userMapping.getId() > 0);
    SessionProviderTest.userMapping = userMapping;
  }

  @Test
  public void should_persist_and_return_session() {
    final Session expected = new Session(DATE, LocalTime.of(8, 0), LocalTime.of(12, 0));
    expected.setUserMapping(userMapping);

    SESSION_PROVIDER.save(expected);

    Assert.assertTrue(expected.getId() > 0);

    final Session actual = SESSION_PROVIDER.get(expected.getId());

    Assert.assertNotNull(actual);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void should_update_session() {
    final Session expected = new Session(DATE, LocalTime.of(8, 0), LocalTime.of(12, 0));
    expected.setUserMapping(userMapping);

    SESSION_PROVIDER.save(expected);

    Assert.assertTrue(expected.getId() > 0);

    final Session actual = SESSION_PROVIDER.get(expected.getId());

    Assert.assertNotNull(actual);
    Assert.assertEquals(expected, actual);

    actual.setEnd(LocalTime.of(13, 0));

    SESSION_PROVIDER.save(actual);

    final Session updated = SESSION_PROVIDER.get(actual.getId());

    Assert.assertEquals(LocalTime.of(13, 0), updated.getEnd());
  }

  @AfterClass
  public static void teardown() {
    SESSION_PROVIDER.close();
  }
}
