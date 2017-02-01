package at.sheldor5.tr.api.objects;

import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.RecordType;

import java.time.LocalDate;
import java.time.LocalTime;

import at.sheldor5.tr.api.objects.Session;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 22.01.2017.
 */
public class SessionTest {

  @Test(expected = NullPointerException.class)
  public void test_session_no_records() {
    final Session session = new Session(1);

    session.build(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_invalid_records() {
    final Record begin = new Record();
    final Record end = new Record();
    final Session session = new Session(1);

    session.build(begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_negative_duration() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    final LocalTime beginTime = LocalTime.of(1, 0);
    final LocalTime endTime = LocalTime.of(0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date.getDayOfMonth());

    session.build(begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_begin_checkout() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKOUT);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date.getDayOfMonth());

    session.build(begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_end_checkin() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKIN);
    final Session session = new Session(date.getDayOfMonth());

    session.build(begin, end);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_session_different_dates() {
    final LocalDate beginDate = LocalDate.of(2017, 1, 1);
    final LocalDate endDate = LocalDate.of(2017, 1, 2);
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, beginDate, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, endDate, endTime, RecordType.CHECKOUT);
    final Session session = new Session(beginDate.getDayOfMonth());

    session.build(begin, end);
  }

  @Test
  public void test_session_duration() {
    final LocalDate date = LocalDate.of(2017, 1, 1);

    Session session;
    Record begin;
    Record end;

    // 1 second
    begin = new Record(0, date, LocalTime.of(8, 0), RecordType.CHECKIN);
    end = new Record(0, date, LocalTime.of(8, 0, 1), RecordType.CHECKOUT);
    session = new Session(date.getDayOfMonth());
    session.build(begin, end);
    Assert.assertEquals(1, session.getSummary());

    // 4 hours
    begin = new Record(0, date, LocalTime.of(8, 0), RecordType.CHECKIN);
    end = new Record(0, date, LocalTime.of(12, 0), RecordType.CHECKOUT);
    session = new Session(date.getDayOfMonth());
    session.build(begin, end);
    Assert.assertEquals(4 * 60 * 60, session.getSummary());
  }

  @Test
  public void test_session_contains() {
    final LocalDate date = LocalDate.now();
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date.getDayOfMonth());
    session.build(begin, end);

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
    final LocalDate date = LocalDate.of(2017, 1, 1);
    final LocalTime beginTime = LocalTime.of(8, 0);
    final LocalTime endTime = LocalTime.of(12, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);
    final Session session = new Session(date.getDayOfMonth());
    session.build(begin, end);
    final LocalTime time = LocalTime.of(10, 0);

    final Session actual = session.split(time);
    Assert.assertEquals("Duration should be updated to 2 hours", 2 * 60 * 60, session.getSummary());
    Assert.assertEquals("Duration should be 2 hours", 2 * 60 * 60, actual.getSummary());
  }
}