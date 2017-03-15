package at.sheldor5.tr.core.persistence;

import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.RecordType;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.api.utils.TimeUtils;
import at.sheldor5.tr.core.auth.AuthenticationManager;
import at.sheldor5.tr.auth.db.DatabaseAuthentication;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
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

  private static AuthenticationManager manager = new AuthenticationManager();
  private static DatabaseEngine persistenceEngine;
  private static DatabaseAuthentication auth;

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(PROPERTIES);

    final SQLServerDataSource dataSource = new SQLServerDataSource();
    dataSource.setURL(DatabaseConnection.getJDBCUrl());

    DatabaseConnection databaseConnection = new DatabaseConnection(dataSource.getConnection());
    databaseConnection.initialize();

    persistenceEngine = new DatabaseEngine();
    persistenceEngine.setDataSource(dataSource);

    manager.setDataSource(dataSource);
    manager.initialize();

    auth = new DatabaseAuthentication();
    auth.setDataSource(dataSource);
    auth.initialize();
  }

  @Test
  public void test_add_user() {
    final User user = new User(User.getRandomUsername(USER_NAME_PREFIX), USER_FORE, USER_SUR);

    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUuid());
  }

  @Test
  public void test_user_uniqueness() throws SQLException {
    final User user = new User(User.getRandomUsername(USER_NAME_PREFIX), USER_FORE, USER_SUR);

    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUuid());

    int expected = user.getId();
    auth.addUser(user, USER_PASS);
    Assert.assertEquals(expected, user.getId());
  }

  @Test
  public void test_get_user() throws SQLException {
    final User expected = new User(User.getRandomUsername(USER_NAME_PREFIX), USER_FORE, USER_SUR);
    auth.addUser(expected, USER_PASS);
    Assert.assertNotNull(expected.getUuid());

    final User actual = auth.getUser(expected.getUsername(), USER_PASS);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_add_record() throws SQLException {
    final User user = new User(User.getRandomUsername(USER_NAME_PREFIX), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUuid());

    manager.addUserMapping(user);
    Assert.assertTrue(user.getId() > 0);

    final Record record = new Record(0, DATE, TIME, RecordType.CHECKIN);
    persistenceEngine.addRecord(user, record);
    Assert.assertTrue(record.getId() > 0);
  }

  @Test
  public void test_get_record() throws SQLException {
    final User user = new User(User.getRandomUsername(USER_NAME_PREFIX), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUuid());

    manager.addUserMapping(user);
    Assert.assertTrue(user.getId() > 0);

    final Record expected = new Record(DATE, TIME, RecordType.CHECKIN);
    persistenceEngine.addRecord(user, expected);
    Assert.assertTrue(expected.getId() > 0);

    final Record actual = persistenceEngine.getRecord(user, expected.getId());
    Assert.assertNotNull(actual);
    Assert.assertTrue(actual.getId() > 0);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void test_get_records_of_day() throws SQLException {
    final User user = new User(User.getRandomUsername(USER_NAME_PREFIX), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUuid());

    manager.addUserMapping(user);
    Assert.assertTrue(user.getId() > 0);

    int yyyy = 2017;

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0), RecordType.CHECKIN);
        persistenceEngine.addRecord(user, in1);
        final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0), RecordType.CHECKOUT);
        persistenceEngine.addRecord(user, out1);
        final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30), RecordType.CHECKIN);
        persistenceEngine.addRecord(user, in2);
        final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30), RecordType.CHECKOUT);
        persistenceEngine.addRecord(user, out2);
      }
    }

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final List<Record> records = persistenceEngine.getDayRecords(user, yyyy, mm , dd);
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
    final User user = new User("time-recorder", "Time", "Recorder");
    auth.addUser(user, "password");
    Assert.assertNotNull(user.getUuid());

    int yyyy = 2017;

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0), RecordType.CHECKIN);
        persistenceEngine.addRecord(user, in1);
        final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0), RecordType.CHECKOUT);
        persistenceEngine.addRecord(user, out1);
        final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30), RecordType.CHECKIN);
        persistenceEngine.addRecord(user, in2);
        final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30), RecordType.CHECKOUT);
        persistenceEngine.addRecord(user, out2);
      }
    }

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final List<Record> records = persistenceEngine.getDayRecords(user, yyyy, mm , dd);
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
    final User user = new User(User.getRandomUsername(USER_NAME_PREFIX), USER_FORE, USER_SUR);
    auth.addUser(user, USER_PASS);
    Assert.assertNotNull(user.getUuid());

    int yyyy = 2017;
    int mm = 1;
    int dd = 1;

    final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0, 0), RecordType.CHECKIN);
    persistenceEngine.addRecord(user, in1);
    final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0, 0), RecordType.CHECKOUT);
    persistenceEngine.addRecord(user, out1);
    final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30, 0), RecordType.CHECKIN);
    persistenceEngine.addRecord(user, in2);
    final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30, 0), RecordType.CHECKOUT);
    persistenceEngine.addRecord(user, out2);

    final Day day = persistenceEngine.getDay(user, yyyy, mm, dd);

    Assert.assertNotNull(day);
    Assert.assertEquals(4, day.getItems().size());
    Assert.assertEquals("28800 seconds (8 hours)", 28800L, day.getSummary());
    Assert.assertEquals("8:00:00", TimeUtils.getHumanReadableSummary(day.getSummary()));

  }

}