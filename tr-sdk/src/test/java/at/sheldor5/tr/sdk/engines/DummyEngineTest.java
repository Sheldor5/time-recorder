package at.sheldor5.tr.sdk.engines;

import at.sheldor5.tr.api.AuthenticationPlugin;
import at.sheldor5.tr.api.RecordEngine;
import at.sheldor5.tr.api.objects.GlobalConfiguration;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.RecordType;
import at.sheldor5.tr.api.utils.TimeUtils;
import at.sheldor5.tr.sdk.auth.DummyAuthentication;
import helper.TestUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class DummyEngineTest {

  private static final String USER_NAME_PREFIX = "testuser_";
  private static final String USER_PASS = "testpass";
  private static final String USER_FORE = "Sheldor";
  private static final String USER_SUR = "5";
  private static final LocalDate TODAY = LocalDate.now();
  private static final LocalTime NOW = LocalTime.now();

  private final RecordEngine engine = new DummyEngine();
  private final AuthenticationPlugin auth = new DummyAuthentication();

  @Test
  public void test_add_record() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());

    final Record record = new Record(0, TODAY, NOW, RecordType.CHECKIN);
    engine.addRecord(user, record);
    Assert.assertTrue(record.getId() > 0);
  }

  @Test
  public void test_get_record() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());
    final Record expected = new Record(0, TODAY, NOW, RecordType.CHECKIN);
    expected.setTime(expected.getTime().truncatedTo(GlobalConfiguration.MEASURE_UNIT));
    engine.addRecord(user, expected);
    Assert.assertTrue(expected.getId() > 0);

    final Record actual = engine.getRecord(user, expected.getId());
    Assert.assertTrue(actual.getId() > 0);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_get_records_of_day() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());

    int yyyy = 2017;

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0), RecordType.CHECKIN);
        engine.addRecord(user, in1);
        final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0), RecordType.CHECKOUT);
        engine.addRecord(user, out1);
        final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30), RecordType.CHECKIN);
        engine.addRecord(user, in2);
        final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30), RecordType.CHECKOUT);
        engine.addRecord(user, out2);
      }
    }

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final List<Record> records = engine.getDayRecords(user, yyyy, mm , dd);
        Assert.assertNotNull(records);
        Assert.assertEquals(4, records.size());
      }
    }
  }

  @Test
  public void test_get_day_object() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());

    int yyyy = 2017;
    int mm = 1;
    int dd = 1;

    final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0, 0), RecordType.CHECKIN);
    engine.addRecord(user, in1);
    final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0, 0), RecordType.CHECKOUT);
    engine.addRecord(user, out1);
    final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30, 0), RecordType.CHECKIN);
    engine.addRecord(user, in2);
    final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30, 0), RecordType.CHECKOUT);
    engine.addRecord(user, out2);

    final Day day = engine.getDay(user, yyyy, mm, dd);

    Assert.assertNotNull(day);
    Assert.assertEquals(2, day.getItems().size());
    Assert.assertEquals(8 * 3600L, day.getSummary() );

  }
}
