package at.sheldor5.tr.sdk.engines;

import at.sheldor5.tr.api.RecordEngine;
import at.sheldor5.tr.api.objects.GlobalConfiguration;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.RecordType;
import at.sheldor5.tr.api.utils.TimeUtils;
import helper.TestUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

  @Test
  public void test_add_user() {
    final User user = new User(0, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);

    engine.addUser(user, USER_PASS);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);
  }

  @Test
  public void test_user_uniqueness() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);

    engine.addUser(user, USER_PASS);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);

    int expected = user.getId();
    engine.addUser(user, USER_PASS);
    Assert.assertEquals("User ID must not change", expected, user.getId());
  }

  @Test
  public void test_get_user() {
    final User expected = new User(0, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    engine.addUser(expected, USER_PASS);
    Assert.assertTrue("User ID should be greater than 0", expected.getId() > 0);

    final User actual = engine.getUser(expected.getUsername(), USER_PASS);
    Assert.assertEquals("Java <User> Object and Engine [User] Object should be equal", expected, actual);
  }

  @Test
  public void test_add_record() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    engine.addUser(user, USER_PASS);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);

    final Record record = new Record(0, TODAY, NOW, RecordType.CHECKIN);
    engine.addRecord(user, record);
    Assert.assertTrue("Record ID should be greater than 0", record.getId() > 0);
  }

  @Test
  public void test_get_record() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    engine.addUser(user, USER_PASS);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);
    final Record expected = new Record(0, TODAY, NOW, RecordType.CHECKIN);
    expected.setTime(expected.getTime().truncatedTo(GlobalConfiguration.MEASURE_UNIT));
    engine.addRecord(user, expected);
    Assert.assertTrue("Record ID should be greater than 0", expected.getId() > 0);

    final Record actual = engine.getRecord(user, expected.getId());
    Assert.assertTrue("Record ID should be greater than 0", actual.getId() > 0);
    Assert.assertEquals("Java <Record> Object and Database [Record] Object should be equal", expected, actual);
  }

  @Test
  public void test_get_records_of_day() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    engine.addUser(user, USER_PASS);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);

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
        Assert.assertNotNull("Engine should return list", records);
        Assert.assertEquals("Day should have 4 records", 4, records.size());
      }
    }
  }

  @Test
  public void test_get_day_object() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    engine.addUser(user, USER_PASS);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);

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

    Assert.assertNotNull("Engine should return day", day);
    Assert.assertEquals("Day should have 2 sessions", 2, day.getItems().size());
    Assert.assertEquals("Day should count 28800 seconds (8 hours) of work", 8 * 3600L, day.getSummary() );

  }
}