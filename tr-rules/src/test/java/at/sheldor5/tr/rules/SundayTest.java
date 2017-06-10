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
    Session session = new Session(nosundaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

  }

  @Test
  public void test_should_apply() throws GeneralSecurityException, IOException {
    Session session = new Session(sundaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));

  }

  @Test
  public void test_should_not_apply_on_other_days() throws GeneralSecurityException, IOException {
    Session session;

    LocalDate monday = LocalDate.of(2017,1,2);
    LocalDate tuesday= LocalDate.of(2017,1,3);
    LocalDate wednesday= LocalDate.of(2017,1,4);
    LocalDate thursday= LocalDate.of(2017,1,5);
    LocalDate friday= LocalDate.of(2017,1,6);
    LocalDate saturday= LocalDate.of(2017,1,7);

    session = new Session(monday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    session = new Session(tuesday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    session = new Session(wednesday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    session = new Session(thursday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    session = new Session(friday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    session = new Session(saturday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));
  }

  @Test
  public void test_applies_to_sessions() throws GeneralSecurityException, IOException {
    Session session;

    session = new Session(nosundaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

    session = new Session(nosundaydate, LocalTime.MIN, LocalTime.of(12, 0));
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

    session = new Session(nosundaydate, LocalTime.of(12,0), LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

    session = new Session(nosundaydate, LocalTime.of(8,0), LocalTime.of(12,0));
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

    session = new Session(sundaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));

    session = new Session(sundaydate, LocalTime.MIN, LocalTime.of(12,0));
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));

    session = new Session(sundaydate, LocalTime.of(12,0), LocalTime.MAX);
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));

    session = new Session(sundaydate, LocalTime.of(8,0), LocalTime.of(16,0));
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));
  }
}
