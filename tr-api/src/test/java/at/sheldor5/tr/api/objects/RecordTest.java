package at.sheldor5.tr.api.objects;

import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.RecordType;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 27.01.2017.
 */
public class RecordTest {

  @Test
  public void test_constructor() {
    final LocalDate date1 = LocalDate.of(2017, 1, 1);
    final LocalTime time1 = LocalTime.of(1, 2, 3);
    final Record record1 = new Record(42, date1, time1, RecordType.CHECKIN);

    Assert.assertEquals(42, record1.getId());
    Assert.assertEquals(date1, record1.getDate());
    Assert.assertEquals(time1, record1.getTime());
    Assert.assertEquals(RecordType.CHECKIN, record1.getType());

    final LocalDate date2 = LocalDate.of(2016, 12, 31);
    final LocalTime time2 = LocalTime.of(23, 24, 25);
    final Record record2 = new Record(date2, time2, RecordType.CHECKOUT);

    Assert.assertEquals(-1, record2.getId());
    Assert.assertEquals(date2, record2.getDate());
    Assert.assertEquals(time2, record2.getTime());
    Assert.assertEquals(RecordType.CHECKOUT, record2.getType());
  }

  @Test
  public void test_getters() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    final LocalTime time = LocalTime.of(1, 2, 3);
    final Record record = new Record(42, date, time, RecordType.CHECKIN);

    Assert.assertEquals(42, record.getId());
    Assert.assertEquals(date, record.getDate());
    Assert.assertEquals(time, record.getTime());
    Assert.assertEquals(RecordType.CHECKIN, record.getType());
  }

  @Test
  public void test_setters() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    final LocalTime time = LocalTime.of(1, 2, 3);
    final Record record = new Record();

    record.setId(42);
    record.setDate(date);
    record.setTime(time);
    record.setType(RecordType.CHECKIN);

    Assert.assertEquals(42, record.getId());
    Assert.assertEquals(date, record.getDate());
    Assert.assertEquals(time, record.getTime());
    Assert.assertEquals(RecordType.CHECKIN, record.getType());
  }

  @Test
  public void test_valid() {
    final LocalDate date = LocalDate.of(2017, 1, 1);
    final LocalTime time = LocalTime.of(1, 2, 3);
    final Record record = new Record(42, date, time, RecordType.CHECKIN);

    Assert.assertTrue(record.isValid());
  }

  @Test
  public void test_record_comparison() {
    final LocalDate date = LocalDate.now();
    final LocalTime in = LocalTime.of(8, 0);
    final LocalTime out = LocalTime.of(12, 0);

    Record checkin = new Record(date, in, RecordType.CHECKIN);
    Record checkout = new Record(date, out, RecordType.CHECKOUT);
    Assert.assertEquals("Checkin should be before checkout", -1, checkin.compareTo(checkout));
    Assert.assertEquals("Checkout should be after checkin", 1, checkout.compareTo(checkin));
  }

  @Test
  public void test_record_equality() {
    final Record r1 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    final Record r2 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKOUT);

    Assert.assertTrue("Records should be equal", r1.equals(r2));
    Assert.assertTrue("Records should be equal", r2.equals(r1));
  }

  @Test
  public void test_record_inequality() {
    Record r1;
    Record r2;

    // different year
    r1 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    r2 = new Record(LocalDate.of(2016, 1, 1), LocalTime.of(8, 0), RecordType.CHECKOUT);
    Assert.assertFalse("Records should not be equal", r1.equals(r2));
    Assert.assertFalse("Records should not be equal", r2.equals(r1));

    // different month
    r1 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    r2 = new Record(LocalDate.of(2017, 2, 1), LocalTime.of(8, 0), RecordType.CHECKOUT);
    Assert.assertFalse("Records should not be equal", r1.equals(r2));
    Assert.assertFalse("Records should not be equal", r2.equals(r1));

    // different day
    r1 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    r2 = new Record(LocalDate.of(2017, 1, 2), LocalTime.of(8, 0), RecordType.CHECKOUT);
    Assert.assertFalse("Records should not be equal", r1.equals(r2));
    Assert.assertFalse("Records should not be equal", r2.equals(r1));

    // different hour
    r1 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    r2 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(9, 0), RecordType.CHECKOUT);
    Assert.assertFalse("Records should not be equal", r1.equals(r2));
    Assert.assertFalse("Records should not be equal", r2.equals(r1));

    // different minute
    r1 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    r2 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 1), RecordType.CHECKOUT);
    Assert.assertFalse("Records should not be equal", r1.equals(r2));
    Assert.assertFalse("Records should not be equal", r2.equals(r1));

    // different seconds
    r1 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    r2 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0, 1), RecordType.CHECKOUT);
    Assert.assertFalse("Records should not be equal", r1.equals(r2));
    Assert.assertFalse("Records should not be equal", r2.equals(r1));

    // different nanosecond
    r1 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    r2 = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0, 0, 1), RecordType.CHECKOUT);
    Assert.assertFalse("Records should not be equal", r1.equals(r2));
    Assert.assertFalse("Records should not be equal", r2.equals(r1));
  }
}