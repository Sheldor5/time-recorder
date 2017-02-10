package at.sheldor5.tr.core.persistence;

import at.sheldor5.tr.api.plugins.AuthenticationPlugin;
import at.sheldor5.tr.api.RecordEngine;
import at.sheldor5.tr.api.objects.GlobalConfiguration;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.RecordType;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.persistence.DatabaseConnection;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.api.utils.TimeUtils;
import at.sheldor5.tr.auth.db.DatabaseAuthentication;
import helper.TestUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseEngineTest {

  private static final File PROPERTIES = new File("test.properties");

  private static final String USER_NAME_PREFIX = "testuser_";
  private static final String USER_PASS = "testpass";
  private static final String USER_FORE = "Sheldor";
  private static final String USER_SUR = "5";
  private static final LocalDate DATE = LocalDate.of(2017, 1, 1);
  private static final LocalTime TIME = LocalTime.of(8, 0);

  private static Connection connection;
  private static DatabaseConnection databaseConnection;
  private static RecordEngine recordEngine;
  private static AuthenticationPlugin auth;

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(PROPERTIES);
    connection = DatabaseConnection.getConnection();
    databaseConnection = new DatabaseConnection(connection);
    databaseConnection.initialize();
    recordEngine = new DatabaseEngine(connection);
    auth = new DatabaseAuthentication();
    auth.initialize();
  }

  @Test
  public void test_add_user() {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);

    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());
  }

  @Test
  public void test_user_uniqueness() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);

    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());

    final UUID expected = user.getUUID();
    auth.addUser(user, USER_PASS);
    Assert.assertEquals(expected, user.getUUID());
  }

  @Test
  public void test_get_user() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User expected = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(expected, USER_PASS);
    Assert.assertNotNull(expected.getUUID());

    final User actual = auth.getUser(expected.getUsername(), USER_PASS);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_add_record() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());

    final Record record = new Record(0, DATE, TIME, RecordType.CHECKIN);
    recordEngine.addRecord(user, record);
    Assert.assertTrue(record.getId() > 0);
  }

  @Test
  public void test_get_record() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);

    final Record expected = new Record(0, DATE, TIME, RecordType.CHECKIN);
    expected.setTime(expected.getTime().truncatedTo(GlobalConfiguration.MEASURE_UNIT));
    recordEngine.addRecord(user, expected);

    Assert.assertTrue(expected.getId() > 0);

    final Record actual = recordEngine.getRecord(user, expected.getId());
    Assert.assertNotNull(actual);
    Assert.assertTrue(actual.getId() > 0);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_get_records_of_day() throws SQLException {
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());

    /**
     * @see java.util.Date#Date(int, int, int) !
     */
    int yyyy = 2017;

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0), RecordType.CHECKIN);
        recordEngine.addRecord(user, in1);
        final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0), RecordType.CHECKOUT);
        recordEngine.addRecord(user, out1);
        final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30), RecordType.CHECKIN);
        recordEngine.addRecord(user, in2);
        final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30), RecordType.CHECKOUT);
        recordEngine.addRecord(user, out2);
      }
    }

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final List<Record> records = recordEngine.getDayRecords(user, yyyy, mm , dd);
        Assert.assertEquals(4, records.size());
      }
    }
  }

  @Test
  public void test_get_records_of_day_2() throws SQLException {
    boolean ignore = true;
    if (ignore) {
      return;
    }
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, "time-recorder", "Time", "Recorder");
    auth.addUser(user, "password");

    int yyyy = 2017;

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0), RecordType.CHECKIN);
        recordEngine.addRecord(user, in1);
        final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0), RecordType.CHECKOUT);
        recordEngine.addRecord(user, out1);
        final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30), RecordType.CHECKIN);
        recordEngine.addRecord(user, in2);
        final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30), RecordType.CHECKOUT);
        recordEngine.addRecord(user, out2);
      }
    }

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final List<Record> records = recordEngine.getDayRecords(user, yyyy, mm , dd);
        Assert.assertEquals(4, records.size());
      }
    }
  }

  @Test
  public void test_get_day_object() {
    boolean b = true;
    if (b) {
      return;
    }
    final UUID uuid = UUID.randomUUID();
    final User user = new User(uuid, USER_NAME_PREFIX + TestUtils.getRandomLong(), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUUID());

    int yyyy = 2017;
    int mm = 1;
    int dd = 1;

    final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0, 0), RecordType.CHECKIN);
    recordEngine.addRecord(user, in1);
    final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0, 0), RecordType.CHECKOUT);
    recordEngine.addRecord(user, out1);
    final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30, 0), RecordType.CHECKIN);
    recordEngine.addRecord(user, in2);
    final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30, 0), RecordType.CHECKOUT);
    recordEngine.addRecord(user, out2);

    final Day day = recordEngine.getDay(user, yyyy, mm, dd);

    Assert.assertNotNull(day);
    Assert.assertEquals(4, day.getItems().size());
    Assert.assertEquals("28800 seconds (8 hours)", 28800L, day.getSummary());
    Assert.assertEquals("8:00:00", TimeUtils.getHumanReadableSummary(day.getSummary()));

  }

  @AfterClass
  public static void cleanup() throws SQLException {
    if (connection != null) {
      connection.close();
    }
  }

}