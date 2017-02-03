package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class SessionTest {

  private static final LocalDate date = LocalDate.of(2017, 1, 1);

  @Test(expected = NullPointerException.class)
  public void test_session_no_records() {
    final Session session = new Session(date, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_invalid_records() {
    final Record start = new Record();
    final Record end = new Record();
    final Session session = new Session(date, start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_negative_duration() {
    final LocalTime startTime = LocalTime.of(1, 0);
    final LocalTime endTime = LocalTime.of(0, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date, start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_start_checkout() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKOUT);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date, start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_end_checkin() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKIN);
    final Session session = new Session(date, start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_different_dates() {
    final LocalDate startDate = LocalDate.of(2017, 1, 1);
    final LocalDate endDate = LocalDate.of(2017, 1, 2);
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, startDate, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, endDate, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date, start, end);
  }

  @Test
  public void test_session_duration() {
    Session session;
    Record start;
    Record end;

    // 1 second
    start = new Record(0, date, LocalTime.of(8, 0), RecordType.CHECKIN);
    end = new Record(0, date, LocalTime.of(8, 0, 1), RecordType.CHECKOUT);
    session = new Session(date, start, end);
    Assert.assertEquals(1, session.getSummary());

    // 4 hours
    start = new Record(0, date, LocalTime.of(8, 0), RecordType.CHECKIN);
    end = new Record(0, date, LocalTime.of(12, 0), RecordType.CHECKOUT);
    session = new Session(date, start, end);
    Assert.assertEquals(4 * 60 * 60, session.getSummary());
  }

  @Test
  public void test_session_contains() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date, start, end);

    LocalTime time;

    // 3 false
    time = LocalTime.of(0, 0);
    Assert.assertFalse("Session should not contain test time", session.contains(time));

    time = LocalTime.of(7, 59, 59);
    Assert.assertFalse("Session should not contain test time", session.contains(time));

    time = LocalTime.of(8, 0);
    Assert.assertFalse("Session should not contain test time", session.contains(time));

    // 3 true
    time = LocalTime.of(8, 0, 1);
    Assert.assertTrue("Session should contain test time", session.contains(time));

    time = LocalTime.of(10, 0);
    Assert.assertTrue("Session should contain test time", session.contains(time));

    time = LocalTime.of(11, 59, 59);
    Assert.assertTrue("Session should contain test time", session.contains(time));

    // 3 false
    time = LocalTime.of(12, 0);
    Assert.assertFalse("Session should not contain test time", session.contains(time));

    time = LocalTime.of(0, 0);
    Assert.assertFalse("Session should not contain test time", session.contains(time));

    time = LocalTime.of(23, 59, 59);
    Assert.assertFalse("Session should not contain test time", session.contains(time));
  }

  @Test
  public void test_session_split() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date, start, end);
    final LocalTime time = LocalTime.of(10, 0);

    final Session actual = session.split(time);
    Assert.assertEquals("Duration should be updated to 7200 seconds (2 hours)", 7200L, session.getSummary());
    Assert.assertEquals("Duration should be 2 hours", 7200L, actual.getSummary());
  }

  @Test
  public void test_session_builder_same_day() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(date, LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(date, LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(16, 30, 0), RecordType.CHECKOUT));

    final List<Session> sessions = Session.buildSessions(list);

    Assert.assertEquals("List should contain 4 sessions", 2, sessions.size());

    Assert.assertEquals("Duration should be 14400 seconds (4 hours)", 14400L, sessions.get(0).getSummary());
    Assert.assertEquals("Duration should be 14400 seconds (4 hours)", 14400L, sessions.get(1).getSummary());
  }

  @Test
  public void test_session_builder_multiple_days() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(date, LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(date, LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(16, 30, 0), RecordType.CHECKOUT));
    list.add(new Record(date.plusDays(1), LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(date.plusDays(1), LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(date.plusDays(1), LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(date.plusDays(1), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));
    list.add(new Record(date.plusDays(2), LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(date.plusDays(2), LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(date.plusDays(2), LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(date.plusDays(2), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));

    final List<Session> sessions = Session.buildSessions(list);

    Assert.assertEquals("List should contain 6 sessions", 6, sessions.size());

    Assert.assertEquals("Duration should be 14400 seconds (4 hours)", 14400L, sessions.get(0).getSummary());
    Assert.assertEquals("Duration should be 14400 seconds (4 hours)", 14400L, sessions.get(1).getSummary());

    Assert.assertEquals("Duration should be 14400 seconds (4 hours)", 14400L, sessions.get(2).getSummary());
    Assert.assertEquals("Duration should be 14400 seconds (4 hours)", 14400L, sessions.get(3).getSummary());

    Assert.assertEquals("Duration should be 14400 seconds (4 hours)", 14400L, sessions.get(4).getSummary());
    Assert.assertEquals("Duration should be 14400 seconds (4 hours)", 14400L, sessions.get(5).getSummary());
  }
}