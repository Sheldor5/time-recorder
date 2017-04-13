package at.sheldor5.tr.api.user;

import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.RecordType;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.utils.GlobalConfiguration;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ScheduleTest {

  private static final Object LOCK = new Object();

  @Test
  public void should_return_amount_in_configured_units() {
    final LocalTime monday = LocalTime.of(8, 0);
    final Schedule schedule = new Schedule();
    schedule.setMonday(monday);

    synchronized (LOCK) {
      try {
        GlobalConfiguration.MEASURE_UNIT = ChronoUnit.SECONDS;

        Assert.assertEquals(8L * 60L * 60L, schedule.getTime(DayOfWeek.MONDAY));

        GlobalConfiguration.MEASURE_UNIT = ChronoUnit.MILLIS;
        schedule.update();

        Assert.assertEquals(8L * 60L * 60L * 1000L, schedule.getTime(DayOfWeek.MONDAY));
      } finally {
        GlobalConfiguration.MEASURE_UNIT = ChronoUnit.SECONDS;
      }
    }

  }

  @Test
  public void test_summary() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    Session session = TestUtils.getDefaultSessionAnteMeridiem(date);

    synchronized (LOCK) {
      Schedule schedule = new Schedule();
      schedule.setTime(date.getDayOfWeek(), 4, 0, 0);

      Assert.assertEquals(0, session.getValuedSummary() - schedule.getTime(session.getDate().getDayOfWeek()));
    }
  }
}