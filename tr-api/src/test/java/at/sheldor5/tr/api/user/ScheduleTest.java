package at.sheldor5.tr.api.user;

import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.RecordType;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.utils.GlobalConfiguration;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleTest {

  @Test
  public void test_setter() {
    Schedule schedule = new Schedule();
    LocalTime amount = LocalTime.of(8, 0);
    schedule.setTime(DayOfWeek.MONDAY, amount);
    Assert.assertEquals(LocalTime.MIN.until(amount, GlobalConfiguration.MEASURE_UNIT), schedule.getTime(DayOfWeek.MONDAY));
  }

  @Test
  public void test_summary() {
    LocalDate date = LocalDate.of(2017, 1, 1);
    LocalTime start = LocalTime.of(8, 0);
    LocalTime end = LocalTime.of(12, 0);
    Record s = new Record(date, start, RecordType.CHECKIN);
    Record e = new Record(date, end, RecordType.CHECKOUT);
    Session session = new Session(s, e);

    Schedule schedule = new Schedule();
    schedule.setTime(date.getDayOfWeek(), 4, 0, 0);

    Assert.assertEquals(0, session.getValuedSummary() - schedule.getTime(session.getDate().getDayOfWeek()));
  }
}