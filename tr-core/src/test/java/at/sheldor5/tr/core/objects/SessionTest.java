package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import at.sheldor5.tr.core.utils.TimeUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 22.01.2017.
 */
public class SessionTest {

  //TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

  @Test(expected = IllegalArgumentException.class)
  public void test_period_initialization_invalid_duration() {
    final LocalDate date = LocalDate.now();
    final LocalTime beginTime = LocalTime.of(1, 0);
    final LocalTime endTime = LocalTime.of(0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);

    new Session(1.0D, begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_period_initialization_begin_checkout() {
    final LocalDate date = LocalDate.now();
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKOUT);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);

    new Session(1.0D, begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_period_initialization_end_checkin() {
    final LocalDate date = LocalDate.now();
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKIN);

    new Session(1.0D, begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_period_initialization_different_dates() {
    final LocalDate beginDate = LocalDate.of(2017, 1, 1);
    final LocalDate endDate = LocalDate.of(2017, 1, 2);
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, beginDate, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, endDate, endTime, RecordType.CHECKOUT);

    new Session(1.0D, begin, end);
  }

  @Test
  public void test_period_duration() {
    final LocalDate date = LocalDate.now();
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);

    final Session session = new Session(1.0D, begin, end);
    Assert.assertEquals(4L * TimeUtils.HOUR_IN_MILLIS, session.getDuration());
  }

  @Test
  public void test_period_contains() {
    final LocalDate date = LocalDate.now();
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(1.0D, begin, end);

    LocalTime time;

    // 3 false
    time = LocalTime.of(0, 0);
    Assert.assertFalse("Period should not contain test time", session.contains(time));

    time = LocalTime.of(7, 59, 59, 999);
    Assert.assertFalse("Period should not contain test time", session.contains(time));

    time = LocalTime.of(8, 0);
    Assert.assertFalse("Period should not contain test time", session.contains(time));

    // 3 true
    time = LocalTime.of(8, 0, 0, 1);
    Assert.assertTrue("Period should contain test time", session.contains(time));

    time = LocalTime.of(10, 0);
    Assert.assertTrue("Period should contain test time", session.contains(time));

    time = LocalTime.of(11, 59, 59, 999);
    Assert.assertTrue("Period should contain test time", session.contains(time));

    // 3 false
    time = LocalTime.of(12, 0);
    Assert.assertFalse("Period should not contain test time", session.contains(time));

    time = LocalTime.of(0, 0);
    Assert.assertFalse("Period should not contain test time", session.contains(time));

    time = LocalTime.of(23, 59, 59, 999);
    Assert.assertFalse("Period should not contain test time", session.contains(time));
  }

  @Test
  public void test_period_split() {
    final LocalDate date = LocalDate.now();
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(1.0D, begin, end);
    final LocalTime time = LocalTime.of(10, 0);

    final Session actual = session.split(time);
    Assert.assertEquals("Duration should be updated to 2 hours", 2L * TimeUtils.HOUR_IN_MILLIS, session.getDuration());
    Assert.assertEquals("Duration should be 2 hours", 2L * TimeUtils.HOUR_IN_MILLIS, actual.getDuration());
  }
}