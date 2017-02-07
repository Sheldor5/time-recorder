package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class DayTest {

  private static final LocalDate date = LocalDate.of(2017, 01, 01);

  @Test
  public void test_day_empty() {
    final Day day = new Day(date);

    Assert.assertEquals(0L, day.getSummary());
    Assert.assertEquals(0L, day.getValuedSummary());
  }

  @Test
  public void test_day_single_session() {
    final Record start = new Record(date, LocalTime.of(8, 0), RecordType.CHECKIN);
    final Record end = new Record(date, LocalTime.of(12, 0), RecordType.CHECKOUT);

    Session session;
    Day day;

    session = new Session(date, start, end);
    day = new Day(date);
    day.addItem(session);
    Assert.assertEquals(14400L, day.getSummary());
    Assert.assertEquals(14400L, day.getValuedSummary());

    session = new Session(date, start, end, 1.5D);
    day = new Day(date);
    day.addItem(session);
    Assert.assertEquals(14400L, day.getSummary());
    Assert.assertEquals(21600L, day.getValuedSummary());
  }

  @Test
  public void test_day_multiple_session() {
    final Record start1 = new Record(date, LocalTime.of(4, 0), RecordType.CHECKIN);
    final Record end1 = new Record(date, LocalTime.of(10, 0), RecordType.CHECKOUT);
    final Record start2 = new Record(date, LocalTime.of(11, 0), RecordType.CHECKIN);
    final Record end2 = new Record(date, LocalTime.of(15, 0), RecordType.CHECKOUT);
    final Record start3 = new Record(date, LocalTime.of(15, 30), RecordType.CHECKIN);
    final Record end3 = new Record(date, LocalTime.of(21, 30), RecordType.CHECKOUT);
    final Session session1 = new Session(date, start1, end1, 1.5D);
    final Session session2 = new Session(date, start2, end2, 1.0D);
    final Session session3 = new Session(date, start3, end3, 2.0D);

    final Day day = new Day(date);
    day.addItem(session1);
    day.addItem(session2);
    day.addItem(session3);

    Assert.assertEquals("57600 seconds (16 hours)", 57600L, day.getSummary());
    Assert.assertEquals("90000 seconds (25 hours)", 90000L, day.getValuedSummary());
  }

  @Test
  public void test_day_sessions_order() {
    final Record start1 = new Record(date, LocalTime.of(4, 0), RecordType.CHECKIN);
    final Record end1 = new Record(date, LocalTime.of(10, 0), RecordType.CHECKOUT);
    final Record start2 = new Record(date, LocalTime.of(11, 0), RecordType.CHECKIN);
    final Record end2 = new Record(date, LocalTime.of(15, 0), RecordType.CHECKOUT);
    final Record start3 = new Record(date, LocalTime.of(15, 30), RecordType.CHECKIN);
    final Record end3 = new Record(date, LocalTime.of(21, 30), RecordType.CHECKOUT);
    final Session session1 = new Session(date, start1, end1, 1.5D);
    final Session session2 = new Session(date, start2, end2, 1.0D);
    final Session session3 = new Session(date, start3, end3, 2.0D);

    final Day day = new Day(date);
    day.addItem(session3);
    day.addItem(session2);
    day.addItem(session1);
    final List<Session> sessions = day.getItems();

    // test if sorted
    Assert.assertEquals(session1, sessions.get(0));
    Assert.assertEquals(session2, sessions.get(1));
    Assert.assertEquals(session3, sessions.get(2));
  }

  @Test
  public void test_build_day() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(date, LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(date, LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(16, 30, 0), RecordType.CHECKOUT));
    final List<Session> sessions = Session.buildSessions(list);

    final Day day = Day.buildDay(sessions);

    Assert.assertEquals("28800 seconds (8 hours)", 28800L, day.getSummary());
  }

  @Test
  public void test_build_day_from_different_session_days() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(date, LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(date, LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(date.plusDays(1), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));

    final List<Session> sessions = Session.buildSessions(list);

    final Day day = Day.buildDay(sessions);
    Assert.assertEquals("55799 seconds", 55799, day.getSummary());
  }

  @Test(expected = IllegalStateException.class)
  public void test_build_invalid_day() {
    final List<Session> sessions = new ArrayList<>();
    Session session;
    Record begin;
    Record end;

    begin = new Record(date, LocalTime.of(8, 0, 0), RecordType.CHECKIN);
    end = new Record(date, LocalTime.of(12, 0, 0), RecordType.CHECKOUT);
    session = new Session(date, begin, end);
    sessions.add(session);

    begin = new Record(date.plusDays(1), LocalTime.of(12, 0, 0), RecordType.CHECKIN);
    end = new Record(date.plusDays(1), LocalTime.of(16, 30, 0), RecordType.CHECKOUT);
    session = new Session(begin, end);
    sessions.add(session);

    Day.buildDay(sessions);
  }

  @Test
  public void test_build_invalid_day_2() {
    Assert.assertNull(Day.buildDay(null));
    Assert.assertNull(Day.buildDay(new ArrayList<>()));
  }
}