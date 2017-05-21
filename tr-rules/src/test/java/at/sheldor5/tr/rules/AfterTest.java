package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Session;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class AfterTest {

  private static final LocalDate date = LocalDate.of(2017, 1, 1);
  private static final LocalTime time = LocalTime.of(19, 0);

  @Test(expected = IllegalArgumentException.class)
  public void test_unreachable() {
    new After(LocalTime.MAX, 1.5D);
  }

  @Test
  public void test_applies_to_session() {
    final After before = new After(time, 1.5D, 1, 2, 3, 4, 5, 6, 7);
    LocalTime begin;
    LocalTime end;
    Session session;

    begin = LocalTime.MIN;
    end = LocalTime.of(18, 59, 59, 999999999);
    session = new Session(date, begin, end);
    Assert.assertFalse("Rule should not apply", before.applies(session));

    begin = LocalTime.MIN;
    end = LocalTime.of(19, 0);
    session = new Session(date, begin, end);
    Assert.assertFalse("Rule should not apply", before.applies(session));

    begin = LocalTime.MIN;
    end = LocalTime.of(19, 0, 0, 1);
    session = new Session(date, begin, end);
    Assert.assertTrue("Rule should apply", before.applies(session));

    begin = LocalTime.of(19, 0);
    end = LocalTime.of(20, 0);
    session = new Session(date, begin, end);
    Assert.assertTrue("Rule should apply", before.applies(session));

    begin = LocalTime.of(20, 0);
    end = LocalTime.of(21, 0);
    session = new Session(date, begin, end);
    Assert.assertTrue("Rule should apply", before.applies(session));

    begin = LocalTime.of(20, 0);
    end = LocalTime.MAX;
    session = new Session(date, begin, end);
    Assert.assertTrue("Rule should apply", before.applies(session));
  }

  @Test
  public void test_split() {
    final LocalDate monday = LocalDate.of(2017, 1, 2);
    final LocalTime begin = LocalTime.of(18, 0);
    final LocalTime end = LocalTime.of(20, 0);
    final After before = new After(time, 1.5D, 1, 2, 3, 4, 5, 6);

    final Session session = new Session(monday, begin, end);

    Assert.assertTrue("Session should apply", before.applies(session));

    final Session actual = before.split(session);

    Assert.assertEquals("Session start time should not be updated by rule", begin, session.getStart());
    Assert.assertEquals("Session end time should be updated by rule", time, session.getEnd());

    Assert.assertEquals("Session start time should be updated by rule", time, actual.getStart());
    Assert.assertEquals("Session end time should not be updated by rule", end, actual.getEnd());

    Assert.assertEquals("Session summary should be updated", 3600L, session.getSummary());
    Assert.assertEquals("Session valued summary should not be update", 3600L, session.getValuedSummary());

    Assert.assertEquals("Session summary should be set", 3600L, actual.getSummary());
    Assert.assertEquals("Session valued summary should be set", 5400L, actual.getValuedSummary());
  }

  @Test
  public void test_split_if_not_apply() {
    final LocalDate monday = LocalDate.of(2017, 1, 2);
    final LocalTime begin = LocalTime.of(17, 0);
    final LocalTime end = LocalTime.of(18, 0);
    final After before = new After(time, 1.5D, 1, 2, 3, 4, 5, 6);


    final Session session = new Session(monday, begin, end);

    Assert.assertFalse("Session should not apply", before.applies(session));

    final Session actual = before.split(session);

    Assert.assertNull("Session should not be split", actual);

    Assert.assertEquals("Session start time should not be updated by rule", begin, session.getStart());
    Assert.assertEquals("Session end time should not be updated by rule", end, session.getEnd());

    Assert.assertEquals("Session summary should not be updated", 3600L, session.getSummary());
    Assert.assertEquals("Session valued summary should not be updated", 3600L, session.getValuedSummary());
  }

  @Test
  public void test_update_only() {
    final LocalDate monday = LocalDate.of(2017, 1, 2);
    final LocalTime begin = LocalTime.of(20, 0);
    final LocalTime end = LocalTime.of(21, 0);
    final After before = new After(time, 1.5D, 1, 2, 3, 4, 5, 6);

    final Session session = new Session(monday, begin, end);

    Assert.assertTrue("Session should apply", before.applies(session));

    final Session actual = before.split(session);

    Assert.assertNull("Session should be updated only", actual);

    Assert.assertEquals("Session start time should not be updated by rule", begin, session.getStart());
    Assert.assertEquals("Session end time should not be updated by rule", end, session.getEnd());

    Assert.assertEquals("Session summary should not be updated", 3600L, session.getSummary());
    Assert.assertEquals("Session valued summary should be updated", 5400L, session.getValuedSummary());
  }
}