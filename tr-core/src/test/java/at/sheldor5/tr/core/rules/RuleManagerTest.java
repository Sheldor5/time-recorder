package at.sheldor5.tr.core.rules;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.RecordType;
import at.sheldor5.tr.api.objects.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RuleManagerTest {

  private static final File rules = new File("src/test/resources/rules");
  private static RuleManager manager;

  @Before
  public void test() throws Exception {
    manager = RuleManager.getInstance();
    manager.load(rules);
  }

  @Test
  public void test_apply_on_day() {
    final LocalDate monday = LocalDate.of(2017, 1, 2);
    final Day day = new Day(monday);
    Record begin;
    Record end;
    Session session;

    begin = new Record(monday, LocalTime.of(3, 0), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(4, 0), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    begin = new Record(monday, LocalTime.of(4, 30), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(8, 0), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    begin = new Record(monday, LocalTime.of(9, 0), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(16, 0), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    begin = new Record(monday, LocalTime.of(17, 0), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(19, 1), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    begin = new Record(monday, LocalTime.of(19, 16), RecordType.CHECKIN);
    end = new Record(monday, LocalTime.of(22, 0), RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    day.addItem(session);

    Assert.assertTrue("Rule should apply", manager.applies(day));

    manager.apply(day);
    final List<Session> sessions = day.getItems();

    Assert.assertEquals("Rule should split session into 7 sessions", 7, sessions.size());

    Assert.assertEquals("Session summary should be 3600 seconds (1 hour)", 3600L, sessions.get(0).getSummary());
    Assert.assertEquals("Session valued summary should be 5400 seconds (1 hour, 30 minutes)", 5400L, sessions.get(0).getValuedSummary());

    Assert.assertEquals("Session summary should be 1800 seconds (30 minutes)", 1800L, sessions.get(1).getSummary());
    Assert.assertEquals("Session valued summary should be 2700 seconds (45 minutes)", 2700L, sessions.get(1).getValuedSummary());

    Assert.assertEquals("Session summary should be 10800 seconds (3 hours)", 10800L, sessions.get(2).getSummary());
    Assert.assertEquals("Session valued summary should be 10800 seconds (3 hours)", 10800L, sessions.get(2).getValuedSummary());

    Assert.assertEquals("Session summary should be 25200 seconds (7 hours)", 25200L, sessions.get(3).getSummary());
    Assert.assertEquals("Session valued summary should be 25200 seconds (7 hours)", 25200L, sessions.get(3).getValuedSummary());

    Assert.assertEquals("Session summary should be 7200 seconds (2 hours)", 7200L, sessions.get(4).getSummary());
    Assert.assertEquals("Session valued summary should be 7200 seconds (2 hours)", 7200L, sessions.get(4).getValuedSummary());

    Assert.assertEquals("Session summary should be 60 seconds (1 minute)", 60L, sessions.get(5).getSummary());
    Assert.assertEquals("Session valued summary should be 90 seconds (1 minute, 30 seconds)", 90L, sessions.get(5).getValuedSummary());

    Assert.assertEquals("Session summary should be 9800L seconds (2 hours, 44 minutes)", 9840L, sessions.get(6).getSummary());
    Assert.assertEquals("Session valued summary should be 14700 seconds (4 hours, 6 minutes)", 14760L, sessions.get(6).getValuedSummary());

    Assert.assertEquals("Day summary should be 58500L seconds (16 hours, 15 minutes)", 58500L, day.getSummary());
    Assert.assertEquals("Day valued summary should be 3960 seconds (18 hours, 22 minutes, 30 seconds)", 66150L, day.getValuedSummary());

    System.out.println(day);
  }
}