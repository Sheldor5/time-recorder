package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.Session;
import at.sheldor5.tr.api.objects.RecordType;
import at.sheldor5.tr.api.utils.TimeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 23.01.2017.
 */
public class BeforeTest {

  private static final List<String> workingDays = new ArrayList<>();

  @org.junit.Before
  public void init() {
    //TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    workingDays.add("monday");
    workingDays.add("tuesday");
    workingDays.add("wednesday");
    workingDays.add("thursday");
    workingDays.add("friday");
    workingDays.add("saturday");
  }

  @Test
  public void test() {
    Time time = new Time(1000 * 60); // 1 minute
    System.out.println(time.getTime());
  }

  @Test
  public void test_before() {
    final LocalDate date = LocalDate.now();
    final LocalTime beginTime = LocalTime.of(4, 0);
    final LocalTime endTime = LocalTime.of(23, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date.getDayOfMonth());
    session.build(begin, end);

    final LocalTime time = LocalTime.of(5, 0);
    final Before before = new Before(time, 1.5D, workingDays);
    final Session actual = before.split(session);

    Assert.assertTrue(session.getEnd().equals(time));
    Assert.assertTrue(actual.getStart().equals(time));

    Assert.assertEquals("Failed to update session", 1L * 3600L, session.getSummary());
    Assert.assertEquals("Failed to update session", (long) (1.5D * 3600L), session.getValuedSummary());

    Assert.assertEquals("Failed to update session", 18L * 3600L, actual.getSummary());
    Assert.assertEquals("Failed to update session", 18L * 3600L, actual.getValuedSummary());
  }
}