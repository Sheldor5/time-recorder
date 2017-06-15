package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Session;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HolidayTest {

  private static final LocalDate holidaydate = LocalDate.of(2017, 11, 1);
  private static final LocalDate noholidaydate = LocalDate.of(2017,1,2);
  private static final LocalTime time = LocalTime.of(19, 0);
  private static final Holiday holiday = new Holiday(holidaydate);
  private static final Holiday noholiday = new Holiday(noholidaydate);

  @Test
  public void test_should_not_apply() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;
    Session session;
    LocalDate sundayholiday = LocalDate.of(2017,1,1);

    begin = LocalTime.of(4, 0);
    end = LocalTime.of(5, 0);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(session.getDate()));
  }

  @Test
  public void test_should_apply() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;
    Session session;


    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", holiday.applies(session.getDate()));
  }

  @Test
  public void test_should_not_apply_on_holidays_on_Sunday() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;
    Session session;
    LocalDate sundayholiday = LocalDate.of(2017,1,1);


    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(sundayholiday, begin, end);
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(session.getDate()));
  }

  @Test
  public void test_different_holidays() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;

    Session session;
    LocalDate americanholiday = LocalDate.of(2017,7,4);
    LocalDate austrianholiday = LocalDate.of(2017,12,8);

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", holiday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(americanholiday, begin, end);
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(austrianholiday, begin, end);
    Assert.assertTrue("RuleClass should apply", holiday.applies(session.getDate()));
  }

  @Test
  public void test_list_of_different_holidays_sessions() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;

    List<Session> sessionList= new ArrayList<>();
    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    Session session = new Session(noholidaydate, begin, end);

    begin =  LocalTime.MIN;
    end =  LocalTime.of(12, 0);
    Session session1 = new Session(noholidaydate, begin, end);

    begin = LocalTime.of(12,0);
    end = LocalTime.MAX;
    Session session2 = new Session(noholidaydate, begin, end);

    begin = LocalTime.of(8,0);
    end =  LocalTime.of(12,0);
    Session session3 = new Session(noholidaydate, begin, end);

    begin =  LocalTime.MIN;
    end =  LocalTime.MAX;
    Session session4 = new Session(holidaydate, begin, end);

    begin =  LocalTime.MIN;
    end =  LocalTime.of(12, 0);
    Session session5 = new Session(holidaydate, begin, end);

    begin =  LocalTime.of(12,0);
    end = LocalTime.MAX;
    Session session6 = new Session(holidaydate, begin, end);

    begin = LocalTime.of(8,0);
    end = LocalTime.of(16,0);
    Session session7 = new Session(holidaydate, begin, end);

    sessionList.add(session);
    sessionList.add(session1);
    sessionList.add(session2);
    sessionList.add(session3);
    sessionList.add(session4);
    sessionList.add(session5);
    sessionList.add(session6);
    sessionList.add(session7);

    Assert.assertNotNull("RuleClass should not apply", noholiday.applies(sessionList));

    Assert.assertFalse("RuleClass should not apply", noholiday.applies(sessionList).get(session));
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(sessionList).get(session1));
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(sessionList).get(session2));
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(sessionList).get(session3));
    Assert.assertTrue("RuleClass should not apply", noholiday.applies(sessionList).get(session4));
    Assert.assertTrue("RuleClass should not apply", noholiday.applies(sessionList).get(session5));
    Assert.assertTrue("RuleClass should not apply", noholiday.applies(sessionList).get(session6));
    Assert.assertTrue("RuleClass should not apply", noholiday.applies(sessionList).get(session7));
  }

  @Test
  public void test_applies_to_sessions() throws GeneralSecurityException, IOException {
    LocalTime begin;
    LocalTime end;
    Session session;

    begin =  LocalTime.MIN;
    end = LocalTime.MAX;
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end =  LocalTime.of(12, 0);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(session.getDate()));

    begin = LocalTime.of(12,0);
    end = LocalTime.MAX;
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(session.getDate()));

    begin = LocalTime.of(8,0);
    end =  LocalTime.of(12,0);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("RuleClass should not apply", noholiday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end =  LocalTime.MAX;
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", holiday.applies(session.getDate()));

    begin =  LocalTime.MIN;
    end =  LocalTime.of(12, 0);
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", holiday.applies(session.getDate()));

    begin =  LocalTime.of(12,0);
    end = LocalTime.MAX;
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", holiday.applies(session.getDate()));

    begin = LocalTime.of(8,0);
    end = LocalTime.of(16,0);
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("RuleClass should apply", holiday.applies(session.getDate()));
  }
}