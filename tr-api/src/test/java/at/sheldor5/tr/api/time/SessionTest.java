package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class SessionTest {

  private static final LocalDate date = LocalDate.of(2017, 1, 1);

  @Test(expected = NullPointerException.class)
  public void test_session_no_records() {
    final Session session = new Session(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_invalid_records() {
    final Record start = new Record();
    final Record end = new Record();
    new Session(start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_negative_duration() {
    final LocalTime startTime = LocalTime.of(1, 0);
    final LocalTime endTime = LocalTime.of(0, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    new Session(start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_start_checkout() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKOUT);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    new Session(start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_end_checkin() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKIN);
    new Session(start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_different_dates() {
    final LocalDate startDate = LocalDate.of(2017, 1, 1);
    final LocalDate endDate = LocalDate.of(2017, 1, 2);
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, startDate, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, endDate, endTime, RecordType.CHECKOUT);
    new Session(start, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_no_duration() {
    final LocalDate startDate = LocalDate.of(2017, 1, 1);
    final LocalDate endDate = LocalDate.of(2017, 1, 1);
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(8, 0);
    final Record start = new Record(0, startDate, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, endDate, endTime, RecordType.CHECKOUT);
    new Session(start, end);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void test_add_item() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(start, end);
    session.addItem(new Record());
  }

  @Test
  public void test_session_initialization() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    Session session;

    session = new Session(date);
    Assert.assertNull(session.getStart());
    Assert.assertNull(session.getEnd());
    Assert.assertEquals(1.0D, session.getMultiplier(), 0.0D);

    session = new Session(start, end);
    Assert.assertEquals(date, session.getDate());
    Assert.assertEquals(startTime, session.getStart().getTime());
    Assert.assertEquals(endTime, session.getEnd().getTime());
    Assert.assertEquals(1.0D, session.getMultiplier(), 0.0D);
  }

  @Test
  public void test_getter_setter() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final double multiplier = 2.0D;
    final Session session = new Session(start, end);
    session.setMultiplier(multiplier);

    Assert.assertEquals( date, session.getDate());
    Assert.assertEquals(startTime, session.getStart().getTime());
    Assert.assertEquals(endTime, session.getEnd().getTime());
    Assert.assertEquals(multiplier, session.getMultiplier(), 0.0D);

    final List<Record> records = session.getItems();

    Assert.assertNotNull(records);
    Assert.assertEquals(2, records.size());
    Assert.assertEquals(start, records.get(0));
    Assert.assertEquals(end, records.get(1));
  }

  @Test
  public void test_compare() {
    final LocalTime startTime1 = LocalTime.of(8, 0);
    final LocalTime startTime2 = LocalTime.of(8, 0, 1);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start1 = new Record(0, date, startTime1, RecordType.CHECKIN);
    final Record start2 = new Record(0, date, startTime2, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session a = new Session(start1, end);
    final Session b = new Session(start2, end);

    Assert.assertEquals(-1, a.compareTo(b));
    Assert.assertEquals(1, b.compareTo(a));

    Assert.assertEquals(-1, a.compareTo(null));

    Assert.assertEquals(-1, a.compareTo(new Day(LocalDate.of(2017, 1, 2))));
    Assert.assertEquals(1, a.compareTo(new Day(LocalDate.of(2016, 12, 31))));
  }

  @Test
  public void test_item_validation() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(start, end);
    Assert.assertTrue(session.validateItem(null));
  }

  @Test
  public void test_session_duration() {
    Session session;
    Record start;
    Record end;

    // 1 second
    start = new Record(0, date, LocalTime.of(8, 0), RecordType.CHECKIN);
    end = new Record(0, date, LocalTime.of(8, 0, 1), RecordType.CHECKOUT);
    session = new Session(start, end);
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(0, 0, 1), session.getSummary());

    // 4 hours
    start = new Record(0, date, LocalTime.of(8, 0), RecordType.CHECKIN);
    end = new Record(0, date, LocalTime.of(12, 0), RecordType.CHECKOUT);
    session = new Session(start, end);
    Assert.assertEquals(TestUtils.getTimeInConfiguredUnit(4), session.getSummary());
  }

  @Test
  public void test_session_contains() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(start, end);

    LocalTime time;

    // 3 false
    time = LocalTime.of(0, 0);
    Assert.assertFalse(session.contains(time));

    time = LocalTime.of(7, 59, 59);
    Assert.assertFalse(session.contains(time));

    time = LocalTime.of(8, 0);
    Assert.assertFalse(session.contains(time));

    // 3 true
    time = LocalTime.of(8, 0, 1);
    Assert.assertTrue(session.contains(time));

    time = LocalTime.of(10, 0);
    Assert.assertTrue(session.contains(time));

    time = LocalTime.of(11, 59, 59);
    Assert.assertTrue(session.contains(time));

    // 3 false
    time = LocalTime.of(12, 0);
    Assert.assertFalse(session.contains(time));

    time = LocalTime.of(0, 0);
    Assert.assertFalse(session.contains(time));

    time = LocalTime.of(23, 59, 59);
    Assert.assertFalse(session.contains(time));
  }

  @Test
  public void test_session_split() {
    final LocalTime startTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record start = new Record(0, date, startTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(start, end);

    Session actual;

    actual = session.split(LocalTime.of(10, 0));
    Assert.assertEquals("7200 seconds (2 hours)", TestUtils.getTimeInConfiguredUnit(2), session.getSummary());
    Assert.assertEquals("7200 seconds (2 hours)", TestUtils.getTimeInConfiguredUnit(2), actual.getSummary());

    actual = session.split(LocalTime.MIN);
    Assert.assertNull(actual);

    actual = session.split(null);
    Assert.assertNull(actual);
  }

  @Test
  public void test_to_string() {
    Session session;

    session = TestUtils.getDefaultSessionAnteMeridiem(date);
    Assert.assertEquals("2017-01-01: 08:00 - 12:00 = 4:00:00 (100% = 4:00:00)", session.toString());

    session = TestUtils.getDefaultSessionAnteMeridiem(date);
    session.setMultiplier(1.5D);
    Assert.assertEquals("2017-01-01: 08:00 - 12:00 = 4:00:00 (150% = 6:00:00)", session.toString());
  }

  @Test
  public void test_session_builder_same_day() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(date, LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(date, LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(date, LocalTime.of(16, 30, 0), RecordType.CHECKOUT));

    final List<Session> sessions = SessionUtils.buildSessions(list);

    Assert.assertEquals(2, sessions.size());

    Assert.assertEquals("14400 seconds (4 hours)", TestUtils.getTimeInConfiguredUnit(4), sessions.get(0).getSummary());
    Assert.assertEquals("14400 seconds (4 hours)", TestUtils.getTimeInConfiguredUnit(4), sessions.get(1).getSummary());
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

    final List<Session> sessions = SessionUtils.buildSessions(list);

    Assert.assertEquals(6, sessions.size());

    Assert.assertEquals("14400 seconds (4 hours)", TestUtils.getTimeInConfiguredUnit(4), sessions.get(0).getSummary());
    Assert.assertEquals("14400 seconds (4 hours)", TestUtils.getTimeInConfiguredUnit(4), sessions.get(1).getSummary());

    Assert.assertEquals("14400 seconds (4 hours)", TestUtils.getTimeInConfiguredUnit(4), sessions.get(2).getSummary());
    Assert.assertEquals("14400 seconds (4 hours)", TestUtils.getTimeInConfiguredUnit(4), sessions.get(3).getSummary());

    Assert.assertEquals("14400 seconds (4 hours)", TestUtils.getTimeInConfiguredUnit(4), sessions.get(4).getSummary());
    Assert.assertEquals("14400 seconds (4 hours)", TestUtils.getTimeInConfiguredUnit(4), sessions.get(5).getSummary());
  }
}