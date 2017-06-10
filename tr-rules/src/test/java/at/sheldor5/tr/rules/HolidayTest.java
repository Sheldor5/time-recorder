package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Session;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;

public class HolidayTest {

  private static final LocalDate holidaydate = LocalDate.of(2017, 11, 1);
  private static final LocalDate noholidaydate = LocalDate.of(2017,1,2);
  private static final LocalTime time = LocalTime.of(19, 0);
  private static final Holiday holiday = new Holiday(holidaydate);
  private static final Holiday noholiday = new Holiday(noholidaydate);

  @Test
  public void test_should_not_apply() throws GeneralSecurityException, IOException {
    Session session = new Session(noholidaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

  }

  @Test
  public void test_should_apply() throws GeneralSecurityException, IOException {
    Session session = new Session(holidaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

  }

  @Test
  public void test_should_not_apply_on_holidays_on_Sunday() throws GeneralSecurityException, IOException {
    LocalDate sundayholiday = LocalDate.of(2017,1,1);
    Session session = new Session(sundayholiday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));
  }

  @Test
  public void test_different_holidays() throws GeneralSecurityException, IOException {
    Session session;
    LocalDate americanholiday = LocalDate.of(2017,7,4);
    LocalDate austrianholiday = LocalDate.of(2017,11,15);
    LocalDate austrianholiday2 = LocalDate.of(2017,12,8);

    session = new Session(noholidaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    session = new Session(holidaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    session = new Session(americanholiday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    session = new Session(austrianholiday, LocalTime.MIN, LocalTime.MAX);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    session = new Session(austrianholiday2, LocalTime.MIN, LocalTime.MAX);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));
  }

  @Test
  public void test_applies_to_sessions() throws GeneralSecurityException, IOException {
    Session session;

    session = new Session(noholidaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    session = new Session(noholidaydate, LocalTime.MIN, LocalTime.of(12, 0));
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    session = new Session(noholidaydate, LocalTime.of(12,0), LocalTime.MAX);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    session = new Session(noholidaydate, LocalTime.of(8,0),  LocalTime.of(12,0));
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    session = new Session(holidaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    session = new Session(holidaydate, LocalTime.MIN, LocalTime.of(12,0));
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    session = new Session(holidaydate, LocalTime.of(12,0), LocalTime.MAX);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    session = new Session(holidaydate, LocalTime.MIN, LocalTime.MAX);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));
  }
}