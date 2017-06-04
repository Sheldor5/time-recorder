package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import at.sheldor5.tr.api.utils.GlobalConfiguration;
import utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class SessionTest {

  private static final LocalDate date = LocalDate.of(2017, 1, 1);

  @Test
  public void test_compare() {
    final LocalTime startTime1 = LocalTime.of(8, 0);
    final LocalTime startTime2 = LocalTime.of(8, 0, 1);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Session a = new Session(date, startTime1, endTime);
    final Session b = new Session(date, startTime2, endTime);

    Assert.assertEquals(-1, a.compareTo(b));
    Assert.assertEquals(1, b.compareTo(a));

    Assert.assertEquals(-1, a.compareTo(null));
  }


  @Test
  public void test_session_duration() {
    Session session;
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime1 = LocalTime.of(8, 0, 1);
    session = new Session(date, startTime, endTime1);

    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(0, 0, 1), session.getSummary());

    final LocalTime endTime2 = LocalTime.of(12, 0);
    session = new Session(date, startTime, endTime2);
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), session.getSummary());
  }

  @Test
  public void test_session_contains() {
    final Session session = TestUtils.getDefaultSessionAnteMeridian(date);

    // before start
    Assert.assertFalse(session.contains(LocalTime.of(7, 59, 59)));

    // at start
    Assert.assertFalse(session.contains(LocalTime.of(8, 0)));

    // after start
    Assert.assertTrue(session.contains(LocalTime.of(8, 0, 1)));

    // in the middle
    Assert.assertTrue(session.contains(LocalTime.of(10, 0)));

    // before end
    Assert.assertTrue(session.contains(LocalTime.of(11, 59, 59)));

    // at end
    Assert.assertFalse(session.contains(LocalTime.of(12, 0)));

    // after end
    Assert.assertFalse(session.contains(LocalTime.of(12, 0, 1)));
  }

  @Test
  public void test_session_split() {
    Session session;
    Session actual;

    GlobalConfiguration.MEASURE_UNIT = ChronoUnit.SECONDS;

    // split before start
    session = TestUtils.getDefaultSessionAnteMeridian(date);
    actual = session.split(LocalTime.of(7, 59, 59));
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), session.getSummary());
    Assert.assertNull(actual);

    // split at start
    session = TestUtils.getDefaultSessionAnteMeridian(date);
    actual = session.split(LocalTime.of(8, 0));
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), session.getSummary());
    Assert.assertNull(actual);

    // split after start
    session = TestUtils.getDefaultSessionAnteMeridian(date);
    actual = session.split(LocalTime.of(8, 0, 1));
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(0, 0, 1), session.getSummary());
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(3, 59, 59), actual.getSummary());

    // split in the middle
    session = TestUtils.getDefaultSessionAnteMeridian(date);
    actual = session.split(LocalTime.of(10, 0));
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(2), session.getSummary());
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(2), actual.getSummary());

    // split before end
    session = TestUtils.getDefaultSessionAnteMeridian(date);
    actual = session.split(LocalTime.of(11, 59, 59));
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(3, 59, 59), session.getSummary());
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(0, 0, 1), actual.getSummary());

    // split at end
    session = TestUtils.getDefaultSessionAnteMeridian(date);
    actual = session.split(LocalTime.of(12, 0));
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), session.getSummary());
    Assert.assertNull(actual);

    // split after end
    session = TestUtils.getDefaultSessionAnteMeridian(date);
    actual = session.split(LocalTime.of(12, 0, 1));
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), session.getSummary());
    Assert.assertNull(actual);
  }

  @Test
  public void test_to_string() {
    final Session session = TestUtils.getDefaultSessionAnteMeridian(date);
    Assert.assertEquals("2017-01-01: 08:00 - 12:00 = 4:00:00 (100% = 4:00:00)", session.toString());
    session.setMultiplier(1.5D);
    Assert.assertEquals("2017-01-01: 08:00 - 12:00 = 4:00:00 (150% = 6:00:00)", session.toString());
  }
}
