package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.RecordType;
import at.sheldor5.tr.api.user.UserMapping;
import at.sheldor5.tr.api.utils.GlobalProperties;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class RecordEngineTest {

  private static final String PROPERTIES = "test.properties";
  private static final UserMappingEngine USER_MAPPING_ENGINE = new UserMappingEngine();
  private static final RecordEngine RECORD_ENGINE = new RecordEngine();

  private static final LocalDate DATE = LocalDate.of(2017, 1, 1);

  private static UserMapping user;

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(new File(PROPERTIES));
    DatabaseManager.init();
    UserMapping userMapping = new UserMapping(UUID.randomUUID());
    USER_MAPPING_ENGINE.create(userMapping);
    Assert.assertTrue(userMapping.getId() > 0);
    user = userMapping;
  }

  @Test
  public void should_persist_and_return_record() {
    final Record expected = new Record(DATE, LocalTime.of(8, 0), RecordType.CHECKIN);
    expected.setUser(user);

    RECORD_ENGINE.create(expected);

    Assert.assertTrue(expected.getId() > 0);

    final Record actual = RECORD_ENGINE.read(expected.getId());

    Assert.assertNotNull(actual);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void should_update_record() {
    final Record expected = new Record(DATE, LocalTime.of(8, 0), RecordType.CHECKIN);
    expected.setUser(user);

    RECORD_ENGINE.create(expected);

    Assert.assertTrue(expected.getId() > 0);

    final Record actual = RECORD_ENGINE.read(expected.getId());

    Assert.assertNotNull(actual);
    Assert.assertEquals(expected, actual);

    actual.setRecordType(RecordType.CHECKOUT);
    actual.setTime(LocalTime.of(9, 0));

    RECORD_ENGINE.update(actual);
    final Record updated = RECORD_ENGINE.read(actual.getId());

    Assert.assertEquals(LocalTime.of(9, 0), updated.getTime());
    Assert.assertEquals(RecordType.CHECKOUT, updated.getRecordType());
  }
}
