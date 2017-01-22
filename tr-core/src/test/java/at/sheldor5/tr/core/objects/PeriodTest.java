package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import at.sheldor5.tr.core.utils.TimeUtils;
import java.sql.Date;
import java.sql.Time;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 22.01.2017.
 */
public class PeriodTest {

  //TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

  @Test(expected = IllegalArgumentException.class)
  public void test_period_initialization_invalid_duration() {
    final Date date = new Date(System.currentTimeMillis());
    final Time beginTime = TimeUtils.getTime(1, 0, 0, 0);
    final Time endTime = TimeUtils.getTime(0, 0, 0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);

    new Period(1.0D, begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_period_initialization_begin_checkout() {
    final Date date = new Date(System.currentTimeMillis());
    final Time beginTime = TimeUtils.getTime(8, 0, 0, 0);
    final Time endTime = TimeUtils.getTime(12, 0, 0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKOUT);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);

    new Period(1.0D, begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_period_initialization_end_checkin() {
    final Date date = new Date(System.currentTimeMillis());
    final Time beginTime = TimeUtils.getTime(8, 0, 0, 0);
    final Time endTime = TimeUtils.getTime(12, 0, 0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKIN);

    new Period(1.0D, begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_period_initialization_different_dates() {
    final Date beginDate = new Date(System.currentTimeMillis());
    final Date endDate = new Date(System.currentTimeMillis() + TimeUtils.HOUR_IN_MILLIS * 21L);
    final Time beginTime = TimeUtils.getTime(8, 0, 0, 0);
    final Time endTime = TimeUtils.getTime(12, 0, 0, 0);
    final Record begin = new Record(0, beginDate, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, endDate, endTime, RecordType.CHECKOUT);

    new Period(1.0D, begin, end);
  }

  @Test
  public void test_period_duration() {
    final Date date = new Date(System.currentTimeMillis());
    final Time beginTime = TimeUtils.getTime(8, 0, 0, 0);
    final Time endTime = TimeUtils.getTime(12, 0, 0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);

    final Period period = new Period(1.0D, begin, end);
    Assert.assertEquals(4L * TimeUtils.HOUR_IN_MILLIS, period.getDuration());
  }

  @Test
  public void test_period_contains() {
    final Date date = new Date(System.currentTimeMillis());
    final Time beginTime = TimeUtils.getTime(8, 0, 0, 0);
    final Time endTime = TimeUtils.getTime(12, 0, 0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Period period = new Period(1.0D, begin, end);

    Time time;

    // 3 false
    time = TimeUtils.getTime(0, 0, 0, 0);
    Assert.assertFalse("Period should not contain test time", period.contains(time));

    time = TimeUtils.getTime(7, 59, 59, 999);
    Assert.assertFalse("Period should not contain test time", period.contains(time));

    time = TimeUtils.getTime(8, 0, 0, 0);
    Assert.assertFalse("Period should not contain test time", period.contains(time));

    // 3 true
    time = TimeUtils.getTime(8, 0, 0, 1);
    Assert.assertTrue("Period should contain test time", period.contains(time));

    time = TimeUtils.getTime(10, 0, 0, 0);
    Assert.assertTrue("Period should contain test time", period.contains(time));

    time = TimeUtils.getTime(11, 59, 59, 999);
    Assert.assertTrue("Period should contain test time", period.contains(time));

    // 3 false
    time = TimeUtils.getTime(12, 0, 0, 0);
    Assert.assertFalse("Period should not contain test time", period.contains(time));

    time = TimeUtils.getTime(12, 0, 0, 1);
    Assert.assertFalse("Period should not contain test time", period.contains(time));

    time = TimeUtils.getTime(23, 59, 59, 999);
    Assert.assertFalse("Period should not contain test time", period.contains(time));
  }

  @Test
  public void test_period_split() {
    final Date date = new Date(System.currentTimeMillis());
    final Time beginTime = TimeUtils.getTime(8, 0, 0, 0);
    final Time endTime = TimeUtils.getTime(12, 0, 0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Period period = new Period(1.0D, begin, end);
    final Time time = TimeUtils.getTime(10, 0, 0, 0);

    final Period actual = period.split(time);
    Assert.assertEquals(2L * TimeUtils.HOUR_IN_MILLIS, period.getDuration());
    Assert.assertEquals(2L * TimeUtils.HOUR_IN_MILLIS, actual.getDuration());
  }
}