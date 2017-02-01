package at.sheldor5.tr.api.objects;

import at.sheldor5.tr.api.utils.TimeUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class SessionBuilderTest {

  @Test
  public void test_session_builder_same_day() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));

    final List<Session> sessions = SessionBuilder.buildSessions(list);

    Assert.assertEquals("List should contain 4 sessions", 2, sessions.size());

    long expected = TimeUtils.hoursToSeconds(4);

    Assert.assertEquals("Duration should be 4 hours", expected, sessions.get(0).getSummary());
    Assert.assertEquals("Duration should be 4 hours", expected, sessions.get(1).getSummary());
  }

  @Test
  public void test_session_builder_multiple_days() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));
    list.add(new Record(LocalDate.of(2017, 1, 2), LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 2), LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(LocalDate.of(2017, 1, 2), LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 2), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));
    list.add(new Record(LocalDate.of(2017, 1, 3), LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 3), LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(LocalDate.of(2017, 1, 3), LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 3), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));

    final List<Session> sessions = SessionBuilder.buildSessions(list);

    Assert.assertEquals("List should contain 6 sessions", 6, sessions.size());

    long expected = TimeUtils.hoursToSeconds(4);

    Assert.assertEquals("Duration should be 4 hours", expected, sessions.get(0).getSummary());
    Assert.assertEquals("Duration should be 4 hours", expected, sessions.get(1).getSummary());

    Assert.assertEquals("Duration should be 4 hours", expected, sessions.get(2).getSummary());
    Assert.assertEquals("Duration should be 4 hours", expected, sessions.get(3).getSummary());

    Assert.assertEquals("Duration should be 4 hours", expected, sessions.get(4).getSummary());
    Assert.assertEquals("Duration should be 4 hours", expected, sessions.get(5).getSummary());
  }

  @Test
  public void test_build_day() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));
    final List<Session> sessions = SessionBuilder.buildSessions(list);

    final Day day = SessionBuilder.buildDay(sessions);
    long expected = TimeUtils.hoursToSeconds(8);

    Assert.assertEquals("Summary should be 8 hours", expected, day.getSummary());
  }

  @Test
  public void test_build_day_from_different_session_days() {
    final List<Record> list = new ArrayList<>();
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 0, 0), RecordType.CHECKOUT));
    list.add(new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 30, 0), RecordType.CHECKIN));
    list.add(new Record(LocalDate.of(2017, 1, 2), LocalTime.of(16, 30, 0), RecordType.CHECKOUT));

    final List<Session> sessions = SessionBuilder.buildSessions(list);

    final Day day = SessionBuilder.buildDay(sessions);
    Assert.assertEquals("Summary should be 55799 seconds", 55799, day.getSummary());
  }

  @Test(expected = IllegalStateException.class)
  public void test_build_invalid_day() {
    final List<Session> sessions = new ArrayList<>();
    Session session;
    Record begin;
    Record end;

    session = new Session(1);
    begin = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0, 0), RecordType.CHECKIN);
    end = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(12, 0, 0), RecordType.CHECKOUT);
    session.build(begin, end);
    sessions.add(session);

    session = new Session(2);
    begin = new Record(LocalDate.of(2017, 1, 2), LocalTime.of(12, 0, 0), RecordType.CHECKIN);
    end = new Record(LocalDate.of(2017, 1, 2), LocalTime.of(16, 30, 0), RecordType.CHECKOUT);
    session.build(begin, end);
    sessions.add(session);

    SessionBuilder.buildDay(sessions);
  }
}