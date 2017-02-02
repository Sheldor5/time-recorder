package at.sheldor5.tr.api.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.junit.Assert;
import org.junit.Test;

public class TimeUtilsTest {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

  @Test
  public void test_millis_to_summary() {
    String actual;

    actual = TimeUtils.getHumanReadableSummary(1L * 3600L);
    Assert.assertEquals("Formatting milliseconds to human readable summary format failed", "1:00:00", actual);

    actual = TimeUtils.getHumanReadableSummary(
            23L * 3600L
                    + 59L * 60L
                    + 59L);
    Assert.assertEquals("Formatting milliseconds to human readable summary format failed", "23:59:59", actual);

    actual = TimeUtils.getHumanReadableSummary(24L * 3600L);
    Assert.assertEquals("Formatting milliseconds to human readable summary format failed", "24:00:00", actual);

    actual = TimeUtils.getHumanReadableSummary(100L * 3600L);
    Assert.assertEquals("Formatting milliseconds to human readable summary format failed", "100:00:00", actual);
  }

  @Test
  public void test_string_to_time() {
    Assert.assertEquals("Conversion from time to millis failes", 1L * TimeUtils.HOUR_IN_MILLIS, TimeUtils.getMillis("1:00:00"));
    Assert.assertEquals("Conversion from time to millis failes",
            12L * TimeUtils.HOUR_IN_MILLIS
                    + 34L * TimeUtils.MINUTE_IN_MILLIS
                    + 56L * TimeUtils.SECOND_IN_MILLIS,
            TimeUtils.getMillis("12:34:56"));
    Assert.assertEquals("Conversion from time to millis failes", 100L * TimeUtils.HOUR_IN_MILLIS, TimeUtils.getMillis("100:00:00"));
  }
}