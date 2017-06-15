package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Session;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;

public class SundayTest {

  private static final LocalDate sundaydate = LocalDate.of(2017, 1, 1);
  private static final LocalDate nosundaydate = LocalDate.of(2017,1,2);
  private static final LocalTime time = LocalTime.of(19, 0);
  private static final Sunday sunday = new Sunday(sundaydate);
  private static final Sunday nosunday = new Sunday(nosundaydate);

  @Test
  public void test_should_not_apply() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;
    Session session;

    begin = LocalTime.MIN;
    end = LocalTime.of(4, 0);

    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", nosunday.applies(session.getDate()));
  }

  @Test
  public void test_should_apply() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;
    Session session;

    begin = LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", sunday.applies(session.getDate()));
  }

  @Test
  public void test_should_not_apply_on_other_days() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;
    Session session;

    LocalDate monday = LocalDate.of(2017,1,2);
    LocalDate tuesday= LocalDate.of(2017,1,3);
    LocalDate wednesday= LocalDate.of(2017,1,4);
    LocalDate thursday= LocalDate.of(2017,1,5);
    LocalDate friday= LocalDate.of(2017,1,6);
    LocalDate saturday= LocalDate.of(2017,1,7);

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(monday, begin, end);
    Assert.assertFalse("RuleClass should not apply", sunday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(tuesday, begin, end);
    Assert.assertFalse("RuleClass should not apply", sunday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(wednesday, begin, end);
    Assert.assertFalse("RuleClass should not apply", sunday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(thursday, begin, end);
    Assert.assertFalse("RuleClass should not apply", sunday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(friday, begin, end);
    Assert.assertFalse("RuleClass should not apply", sunday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(saturday, begin, end);
    Assert.assertFalse("RuleClass should not apply", sunday.applies(session.getDate()));
  }

  @Test
  public void test_applies_to_sessions() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;
    Session session;

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", nosunday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end =  LocalTime.of(12, 0);
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", nosunday.applies(session.getDate()));

    begin =  LocalTime.of(12,0);
    end = LocalTime.MAX;
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", nosunday.applies(session.getDate()));

    begin =  LocalTime.of(8,0);
    end =  LocalTime.of(12,0);
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", nosunday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", sunday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.of(12,0);
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", sunday.applies(session.getDate()));

    begin =  LocalTime.of(12,0);
    end = LocalTime.MAX;
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", sunday.applies(session.getDate()));

    begin =  LocalTime.of(8,0);
    end = LocalTime.of(16,0);
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", sunday.applies(session.getDate()));
  }
}
