package at.sheldor5.tr.core.utils;

import at.sheldor5.tr.core.records.Summary;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class TimeUtilsTest {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

  @Test
  public void test_date() {
    final Date date = new Date(System.currentTimeMillis());
    final String expected = DATE_FORMAT.format(date);
    final Date actual = TimeUtils.truncateTime(date);
    Assert.assertEquals(expected, actual.toString());
  }

  @Test
  public void test_time() {
    final Time time = new Time(System.currentTimeMillis());
    final String expected = TIME_FORMAT.format(time);
    final Time actual = TimeUtils.truncateDate(time);
    Assert.assertEquals(expected, actual.toString());
  }

  @Test
  public void test_summary_simple() {
    long millis = 360000000L;
    final String actual = TimeUtils.getHumanReadableTime(millis);
    Assert.assertEquals("Formatting milliseconds to human readable format failed", "100:00:00", actual);
  }

  @Test
  public void test_summary_object() {
    final Summary summary = new Summary(100, 0, 0);
    Assert.assertEquals("Summary for 100 hours failed", "100:00:00", summary.toString());

    summary.addMinutes(58);
    summary.addSeconds(59);
    Assert.assertEquals("Adding 58m and 59s failed", "100:58:59", summary.toString());

    summary.addSeconds(1);
    Assert.assertEquals("Adding 1s from :59 to :00 and auto increasing minute failed", "100:59:00", summary.toString());

    summary.addSeconds(59);
    Assert.assertEquals("Adding 59s failed", "100:59:59", summary.toString());

    summary.addSeconds(1);
    Assert.assertEquals("Adding 1s from :59 to :00 and auto increasing minute and hour failed", "101:00:00", summary.toString());

    summary.addSeconds(60 * 60);
    Assert.assertEquals("Adding 3600s and auto increasing hours failed", "102:00:00", summary.toString());

    summary.addMillis(1000L);
    Assert.assertEquals("Adding 1000ms and auto increasing second failed", "102:00:01", summary.toString());

    summary.addMillis(60000L);
    Assert.assertEquals("Adding 60000ms and auto increasing minute failed", "102:01:01", summary.toString());

    summary.addMillis(3600000L);
    Assert.assertEquals("Adding 1000ms and auto increasing hour failed", "103:01:01", summary.toString());

    summary.addMillis(1);
    Assert.assertEquals("Adding 1ms failed", "103:01:01:001", summary.toStringMillis());

    summary.addMillis(998);
    Assert.assertEquals("Adding 998ms failed", "103:01:01:999", summary.toStringMillis());

    summary.addMillis(1);
    Assert.assertEquals("Adding 1ms and auto increasing second failed", "103:01:02:000", summary.toStringMillis());

    summary.addMillis(3600000000L);
    Assert.assertEquals("Adding 3600000000ms and auto increasing hour failed", "1103:01:02:000", summary.toStringMillis());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_summary_object_remove_millis() {
    final Summary summary = new Summary();
    summary.addMillis(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_summary_object_remove_seconds() {
    final Summary summary = new Summary();
    summary.addSeconds(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_summary_object_remove_minutes() {
    final Summary summary = new Summary();
    summary.addMinutes(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_summary_object_remove_hours() {
    final Summary summary = new Summary();
    summary.addHours(-1);
  }
}