package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import utils.TestUtils;
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

    TestUtils.getDefaultSessionAnteMeridiem(date);

    Session session;
    Day day;

    session = TestUtils.getDefaultSessionAnteMeridiem(date);
    day = new Day(date);
    day.addItem(session);
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), day.getSummary());
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), day.getValuedSummary());

    session = TestUtils.getDefaultSessionAnteMeridiem(date);
    session.setMultiplier(1.5D);
    day = new Day(date);
    day.addItem(session);
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), day.getSummary());
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(6), day.getValuedSummary());
  }

  @Test
  public void test_day_multiple_session() {
    final Record start1 = new Record(date, LocalTime.of(4, 0), RecordType.CHECKIN);
    final Record end1 = new Record(date, LocalTime.of(10, 0), RecordType.CHECKOUT);
    final Record start2 = new Record(date, LocalTime.of(11, 0), RecordType.CHECKIN);
    final Record end2 = new Record(date, LocalTime.of(15, 0), RecordType.CHECKOUT);
    final Record start3 = new Record(date, LocalTime.of(15, 30), RecordType.CHECKIN);
    final Record end3 = new Record(date, LocalTime.of(21, 30), RecordType.CHECKOUT);
    final Session session1 = new Session(start1, end1, 1.5D);
    final Session session2 = new Session(start2, end2, 1.0D);
    final Session session3 = new Session(start3, end3, 2.0D);

    final Day day = new Day(date);
    day.addItem(session1);
    day.addItem(session2);
    day.addItem(session3);

    Assert.assertEquals("57600 seconds (16 hours)", TestUtils.getTimeInConfiguredUnit(16), day.getSummary());
    Assert.assertEquals("90000 seconds (25 hours)", TestUtils.getTimeInConfiguredUnit(22) + TestUtils.getTimeInConfiguredUnit(3), day.getValuedSummary());
  }

  @Test
  public void test_day_sessions_order() {
    final Record start1 = new Record(date, LocalTime.of(4, 0), RecordType.CHECKIN);
    final Record end1 = new Record(date, LocalTime.of(10, 0), RecordType.CHECKOUT);
    final Record start2 = new Record(date, LocalTime.of(11, 0), RecordType.CHECKIN);
    final Record end2 = new Record(date, LocalTime.of(15, 0), RecordType.CHECKOUT);
    final Record start3 = new Record(date, LocalTime.of(15, 30), RecordType.CHECKIN);
    final Record end3 = new Record(date, LocalTime.of(21, 30), RecordType.CHECKOUT);
    final Session session1 = new Session(start1, end1, 1.5D);
    final Session session2 = new Session(start2, end2, 1.0D);
    final Session session3 = new Session(start3, end3, 2.0D);

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
    final List<Session> sessions = SessionFactory.buildSessions(list);

    final Day day = DayFactory.buildDay(sessions);

    Assert.assertEquals("28800 seconds (8 hours)", TestUtils.getTimeInConfiguredUnit(8), day.getSummary());
  }

  @Test
  public void test_build_day_from_different_session_days() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(date, LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(date, LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(date.plusDays(1), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));

    final List<Session> sessions = SessionFactory.buildSessions(list);

    final Day day = DayFactory.buildDay(sessions);
    Assert.assertEquals("55799 seconds", TestUtils.getTimeInConfiguredUnit(15, 29, 59, 999999999), day.getSummary());
  }

  @Test(expected = IllegalStateException.class)
  public void test_build_invalid_day() {
    final List<Session> sessions = new ArrayList<>();
    Session session;
    Record begin;
    Record end;

    session = TestUtils.getDefaultSessionAnteMeridiem(date);
    sessions.add(session);

    begin = new Record(date.plusDays(1), LocalTime.of(12, 0, 0), RecordType.CHECKIN);
    end = new Record(date.plusDays(1), LocalTime.of(16, 30, 0), RecordType.CHECKOUT);
    session = new Session(begin, end);
    sessions.add(session);

    DayFactory.buildDay(sessions);
  }

  @Test
  public void test_build_invalid_day_2() {
    Assert.assertNull(DayFactory.buildDay(null));
    Assert.assertNull(DayFactory.buildDay(new ArrayList<>()));
  }
}