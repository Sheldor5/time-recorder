package at.sheldor5.tr.core.records;

import org.junit.Assert;
import org.junit.Test;

public class SummaryTest {

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
