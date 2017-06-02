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

public class SundayTest {

  private static final LocalDate sundaydate = LocalDate.of(2017, 1, 1);
  private static final LocalDate nosundaydate = LocalDate.of(2017,1,2);
  private static final LocalTime time = LocalTime.of(19, 0);
  private static final Sunday sunday = new Sunday(sundaydate);
  private static final Sunday nosunday = new Sunday(nosundaydate);

  @Test
  public void test_should_not_apply() throws GeneralSecurityException, IOException {
    Record begin;
    Record end;
    Session session;


    begin = new Record(nosundaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(nosundaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

  }

  @Test
  public void test_should_apply() throws GeneralSecurityException, IOException {
    Record begin;
    Record end;
    Session session;


    begin = new Record(sundaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(sundaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));

  }

  @Test
  public void test_should_not_apply_on_other_days() throws GeneralSecurityException, IOException {

    Record begin;
    Record end;
    Session session;
    LocalDate monday = LocalDate.of(2017,1,2);
    LocalDate tuesday= LocalDate.of(2017,1,3);
    LocalDate wednesday= LocalDate.of(2017,1,4);
    LocalDate thursday= LocalDate.of(2017,1,5);
    LocalDate friday= LocalDate.of(2017,1,6);
    LocalDate saturday= LocalDate.of(2017,1,7);

    begin = new Record(monday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(monday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(monday, begin, end);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    begin = new Record(tuesday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(tuesday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(tuesday, begin, end);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    begin = new Record(wednesday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(wednesday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(wednesday, begin, end);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    begin = new Record(thursday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(thursday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(thursday, begin, end);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    begin = new Record(friday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(friday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(friday, begin, end);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));

    begin = new Record(saturday, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(saturday, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(saturday, begin, end);
    Assert.assertFalse("Rule should not apply", sunday.applies(session.getDate()));
  }

  @Test
  public void test_applies_to_sessions() throws GeneralSecurityException, IOException {
    Record begin;
    Record end;
    Session session;

    begin = new Record(nosundaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(nosundaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

    begin = new Record(nosundaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(nosundaydate, LocalTime.of(12, 0), RecordType.CHECKOUT);
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

    begin = new Record(nosundaydate, LocalTime.of(12,0), RecordType.CHECKIN);
    end = new Record(nosundaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

    begin = new Record(nosundaydate, LocalTime.of(8,0), RecordType.CHECKIN);
    end = new Record(nosundaydate, LocalTime.of(12,0), RecordType.CHECKOUT);
    session = new Session(nosundaydate, begin, end);
    Assert.assertFalse("Rule should not apply", nosunday.applies(session.getDate()));

    begin = new Record(sundaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(sundaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));

    begin = new Record(sundaydate, LocalTime.MIN, RecordType.CHECKIN);
    end = new Record(sundaydate, LocalTime.of(12,0), RecordType.CHECKOUT);
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));

    begin = new Record(sundaydate, LocalTime.of(12,0), RecordType.CHECKIN);
    end = new Record(sundaydate, LocalTime.MAX, RecordType.CHECKOUT);
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));

    begin = new Record(sundaydate, LocalTime.of(8,0), RecordType.CHECKIN);
    end = new Record(sundaydate, LocalTime.of(16,0), RecordType.CHECKOUT);
    session = new Session(sundaydate, begin, end);
    Assert.assertTrue("Rule should apply", sunday.applies(session.getDate()));
  }



}