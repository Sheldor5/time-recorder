package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.Day;
import at.sheldor5.tr.core.records.RecordType;
import at.sheldor5.tr.core.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import at.sheldor5.tr.core.utils.StringUtils;
import at.sheldor5.tr.core.utils.TimeUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 20.01.2017.
 */
public class PersistenceManagerTest {

  private static final Logger LOGGER = LogManager.getLogger(PersistenceManagerTest.class);

  private static final File GLOBAL_PROPERTIES = new File("global.properties");
  private static final String USER_NAME_PREFIX = "testuser_";
  private static final String USER_PASS = "testpass";
  private static final String USER_FORE = "Michael";
  private static final String USER_SUR = "Palata";
  private static final Random RANDOM = new Random();
  private static final LocalDate TODAY = LocalDate.now();
  private static final LocalTime NOW = LocalTime.now();

  private static Connection connection;
  private static PersistenceManager persistenceManager;

  @Before
  public void init() throws IOException, SQLException {
    GlobalProperties.load(GLOBAL_PROPERTIES);
    GlobalProperties.setProperty("db.jdbc.url", "jdbc:sqlserver://127.0.0.1;database=TimeRecorder;user=user;password=pass;CharacterSet=UTF-8");
    connection = DatabaseConnection.getConnection();
    Assume.assumeNotNull("Connection could not be established", connection);
    persistenceManager = new PersistenceManager(connection);
    if (!DatabaseConnection.tablesExist()) {
      DatabaseConnection.createTables();
    }
    Assume.assumeTrue("Tables could not be created", DatabaseConnection.tablesExist());
  }

  private static synchronized long getRandomLong() {
    long next = RANDOM.nextLong();
    if (next < 0) {
      next  *= -1;
    }
    return next;
  }

  @Test
  public void test_string_to_md5()  {
    Assert.assertEquals("MD5 calculation failed", "179ad45c6ce2cb97cf1029e212046e81", StringUtils.getMD5(USER_PASS));
  }

  @Test
  public void test_add_user() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + getRandomLong(), USER_FORE, USER_SUR);
    int userId = persistenceManager.addUser(user, USER_PASS);
    Assert.assertTrue("Returned User ID from INSERT should be greater than 0", userId > 0);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);
  }

  @Test(expected = SQLException.class)
  public void test_user_uniqueness() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + getRandomLong(), USER_FORE, USER_SUR);
    int userId = persistenceManager.addUser(user, USER_PASS);
    Assert.assertTrue("Returned User ID from INSERT should be greater than 0", userId > 0);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);

    try {
    persistenceManager.addUser(user, USER_PASS);
    } catch (final SQLException sqle) {
      Assert.assertTrue("Exception should be thrown by UNIQUE KEY violation", sqle.getMessage().contains("UNIQUE KEY"));
      throw sqle;
    }
  }

  @Test
  public void test_get_user() throws SQLException {
    final User expected = new User(0, USER_NAME_PREFIX + getRandomLong(), USER_FORE, USER_SUR);
    int userId = persistenceManager.addUser(expected, USER_PASS);
    Assert.assertTrue("Returned User ID from INSERT should be greater than 0", userId > 0);
    Assert.assertTrue("User ID should be greater than 0", expected.getId() > 0);

    final User actual = persistenceManager.getUser(expected.getUsername(), USER_PASS);
    Assert.assertEquals("Java <User> Object and Database [User] Object should be equal", expected, actual);
  }

  @Test
  public void test_add_record() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + getRandomLong(), USER_FORE, USER_SUR);
    int userId = persistenceManager.addUser(user, USER_PASS);
    Assert.assertTrue("Returned User ID from INSERT should be greater than 0", userId > 0);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);

    final Record record = new Record(0, TODAY, NOW, RecordType.CHECKIN);
    int recordId = persistenceManager.addRecord(user, record);
    Assert.assertTrue("Returned Record ID from INSERT should be greater than 0", recordId > 0);
    Assert.assertTrue("Record ID should be greater than 0", record.getId() > 0);
  }

  @Test
  public void test_get_record() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + getRandomLong(), USER_FORE, USER_SUR);
    int userId = persistenceManager.addUser(user, USER_PASS);
    Assert.assertTrue("Returned User ID from INSERT should be greater than 0", userId > 0);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);
    final Record expected = new Record(0, TODAY, NOW, RecordType.CHECKIN);
    expected.time = expected.time.truncatedTo(ChronoUnit.SECONDS);
    //TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    int recordId = persistenceManager.addRecord(user, expected);
    Assert.assertTrue("Returned Record ID from INSERT should be greater than 0", recordId > 0);
    Assert.assertTrue("Record ID should be greater than 0", expected.getId() > 0);

    final Record actual = persistenceManager.getRecord(expected.getId());
    Assert.assertTrue("Record ID should be greater than 0", actual.getId() > 0);
    Assert.assertEquals("Java <Record> Object and Database [Record] Object should be equal", expected, actual);
  }

  @Test
  public void test_get_records_of_day() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + getRandomLong(), USER_FORE, USER_SUR);
    int userId = persistenceManager.addUser(user, USER_PASS);
    Assert.assertTrue("Returned User ID from INSERT should be greater than 0", userId > 0);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);

    /**
     * @see java.util.Date#Date(int, int, int) !
     */
    int yyyy = 2017;

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0), RecordType.CHECKIN);
        persistenceManager.addRecord(user, in1);
        final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0), RecordType.CHECKOUT);
        persistenceManager.addRecord(user, out1);
        final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30), RecordType.CHECKIN);
        persistenceManager.addRecord(user, in2);
        final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30), RecordType.CHECKOUT);
        persistenceManager.addRecord(user, out2);
      }
    }

    for (int mm = 1; mm <= 12; mm++) {
      int lastDayOfMonth = TimeUtils.getLastDayOfMonth(yyyy, mm);
      for (int dd = 1; dd <= lastDayOfMonth; dd++) {
        final List<Record> records = persistenceManager.getRecordsOfDay(user, dd, mm , yyyy);
        Assert.assertEquals("Day should have 4 records", 4, records.size());
      }
    }
  }

  @Test
  public void test_get_day_object() throws SQLException {
    final User user = new User(0, USER_NAME_PREFIX + getRandomLong(), USER_FORE, USER_SUR);
    int userId = persistenceManager.addUser(user, USER_PASS);
    Assert.assertTrue("Returned User ID from INSERT should be greater than 0", userId > 0);
    Assert.assertTrue("User ID should be greater than 0", user.getId() > 0);

    /**
     * @see java.util.Date#Date(int, int, int) !
     */
    int yyyy = 2017;
    int mm = 1;
    int dd = 1;

    final Record in1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(8, 0, 0), RecordType.CHECKIN);
    persistenceManager.addRecord(user, in1);
    final Record out1 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 0, 0), RecordType.CHECKOUT);
    persistenceManager.addRecord(user, out1);
    final Record in2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(12, 30, 0), RecordType.CHECKIN);
    persistenceManager.addRecord(user, in2);
    final Record out2 = new Record(0, LocalDate.of(yyyy, mm, dd), LocalTime.of(16, 30, 0), RecordType.CHECKOUT);
    persistenceManager.addRecord(user, out2);

    final Day day = persistenceManager.getDay(user, dd, mm, yyyy);

    Assert.assertEquals("Day should have 4 records", 4, day.getItems().size());
    Assert.assertEquals("Day should count 8 hours of work", 8, day.getSummary() / (3600000L));
    Assert.assertEquals("Day should count 8 hours of work", "8:00:00", TimeUtils.getHumanReadableSummary(day.getSummary()));

  }

}