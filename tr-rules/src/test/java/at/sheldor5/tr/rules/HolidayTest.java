package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.RecordType;
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
    Record begin;
    Record end;
    Session session;
    LocalDate sundayholiday = LocalDate.of(2017,1,1);


    begin = new Record(noholidaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(noholidaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

  }

  @Test
  public void test_should_apply() throws GeneralSecurityException, IOException {
    Record begin;
    Record end;
    Session session;


    begin = new Record(holidaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(holidaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

  }

  @Test
  public void test_should_not_apply_on_holidays_on_Sunday() throws GeneralSecurityException, IOException {
    Record begin;
    Record end;
    Session session;
    LocalDate sundayholiday = LocalDate.of(2017,1,1);


    begin = new Record(sundayholiday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(sundayholiday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(sundayholiday, begin, end);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

  }

  @Test
  public void test_different_holidays() throws GeneralSecurityException, IOException {

    Record begin;
    Record end;
    Session session;
    LocalDate americanholiday = LocalDate.of(2017,7,4);
    LocalDate austrianholiday = LocalDate.of(2017,11,15);
    LocalDate austrianholiday2 = LocalDate.of(2017,12,8);

    begin = new Record(noholidaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(noholidaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    begin = new Record(holidaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(holidaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    begin = new Record(americanholiday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(americanholiday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(americanholiday, begin, end);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    begin = new Record(austrianholiday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(austrianholiday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(austrianholiday, begin, end);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    begin = new Record(austrianholiday2, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(austrianholiday2, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(austrianholiday2, begin, end);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));
  }

  @Test
  public void test_applies_to_sessions() throws GeneralSecurityException, IOException {
    Record begin;
    Record end;
    Session session;

    begin = new Record(noholidaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(noholidaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    begin = new Record(noholidaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(noholidaydate, LocalTime.of(12, 0), RecordType.CHECKOUT);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    begin = new Record(noholidaydate, LocalTime.of(12,0), RecordType.CHECKIN);
    end = new Record(noholidaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    begin = new Record(noholidaydate, LocalTime.of(8,0), RecordType.CHECKIN);
    end = new Record(noholidaydate, LocalTime.of(12,0), RecordType.CHECKOUT);
    session = new Session(noholidaydate, begin, end);
    Assert.assertFalse("Rule should not apply", noholiday.applies(session.getDate()));

    begin = new Record(holidaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(holidaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    begin = new Record(holidaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(holidaydate, LocalTime.of(12,0), RecordType.CHECKOUT);
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    begin = new Record(holidaydate, LocalTime.of(12,0), RecordType.CHECKIN);
    end = new Record(holidaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));

    begin = new Record(holidaydate, LocalTime.of(8,0), RecordType.CHECKIN);
    end = new Record(holidaydate, LocalTime.of(16,0), RecordType.CHECKOUT);
    session = new Session(holidaydate, begin, end);
    Assert.assertTrue("Rule should apply", holiday.applies(session.getDate()));
  }



}