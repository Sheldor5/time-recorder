package at.sheldor5.tr.core.persistence;

import at.sheldor5.tr.api.AuthenticationPlugin;
import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.RecordType;
import at.sheldor5.tr.api.objects.Session;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.persistence.DatabaseConnection;
import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.auth.db.DatabaseAuthentication;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class PersistenceManagerTest {

  private static final Random RANDOM = new Random();
  private static final String USER_PREFIX = "testuser_";
  private static final String PASSWORD = "passwort";
  private static final String FORENAME = "Vorname";
  private static final String SURNAME = "Nachname";

  private static User user;

  private AuthenticationPlugin auth;
  private PersistenceManager manager;

  @Before
  public void init() {
    GlobalProperties.setProperty("db.jdbc.class", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    GlobalProperties.setProperty("db.jdbc.url", "jdbc:sqlserver://127.0.0.1;database=TimeRecorder;user=user;password=pass;CharacterSet=UTF-8");
    manager = PersistenceManager.getInstance();
    Assume.assumeNotNull(manager);

    auth = new DatabaseAuthentication(DatabaseConnection.getInstance());
    Assume.assumeNotNull(auth);

    user = new User(getSecureRandomUser(), FORENAME, SURNAME);
    auth.addUser(user, PASSWORD);

    Assume.assumeNotNull(auth.getUser(user.getUsername(), PASSWORD));
  }

  @Test
  public void test_record() {
    final LocalDate date = LocalDate.of(2017, 1, 2);
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record checkin = new Record(date, startTime, RecordType.CHECKIN);
    final Record checkout = new Record(date, endTime, RecordType.CHECKOUT);

    manager.addRecord(user, checkin);
    manager.addRecord(user, checkout);

    final List<Record> records = manager.getDayRecords(user, 2017, 1, 2);
    Assert.assertNotNull(records);
    Assert.assertEquals(2, records.size());

    for (final Record record : records) {
      System.out.println(record);
    }

    final Day day = manager.getDay(user, 2017, 1, 2);
    Assert.assertNotNull(day);

    final List<Session> sessions = day.getItems();
    Assert.assertNotNull(sessions);
    Assert.assertEquals(1, sessions.size());

    for (final Session session : day.getItems()) {
      System.out.println(session);
    }
  }

  private static String getSecureRandomUser() {
    long value;
    SecureRandom random;
    try {
      random = SecureRandom.getInstance("SHA1PRNG");
      value = random.nextLong();
      if (value < 0) {
        value  *= -1;
      }
    } catch (NoSuchAlgorithmException nsae) {
      System.out.println(nsae.getMessage());
      return USER_PREFIX + getRandomLong();
    }
    value = Math.abs(value);
    return USER_PREFIX + Long.toString(value);
  }

  private static synchronized long getRandomLong() {
    long next = RANDOM.nextLong();
    if (next < 0) {
      next  *= -1;
    }
    return next;
  }
}