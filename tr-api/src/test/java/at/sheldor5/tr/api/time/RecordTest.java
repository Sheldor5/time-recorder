package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class RecordTest {

  private static final LocalDate date = LocalDate.of(2017, 1, 1);
  private static final LocalTime time = LocalTime.of(8, 0);

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_id() {
    final Record record = new Record();
    record.setId(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_date() {
    final Record record = new Record();
    record.setDate(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_time() {
    final Record record = new Record();
    record.setTime(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_type() {
    final Record record = new Record();
    record.setType(null);
  }

  @Test
  public void test_initialization() {
    final Record record1 = new Record(42, date, time, RecordType.CHECKIN);

    Assert.assertEquals(42, record1.getId());
    Assert.assertEquals(date, record1.getDate());
    Assert.assertEquals(time, record1.getTime());
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
  public void test_record_validity() {
    final Record record = new Record();

    Assert.assertFalse(record.isValid());

    record.setDate(date);

    Assert.assertFalse(record.isValid());

    record.setTime(time);

    Assert.assertFalse(record.isValid());

    record.setType(RecordType.CHECKIN);

    Assert.assertTrue(record.isValid());
  }

  @Test
  public void test_getters_setters() {
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
  public void test_comparison() {
    final Record checkin = new Record(date, time, RecordType.CHECKIN);
    Record checkout;

    checkout = new Record(date, time.plusHours(1), RecordType.CHECKOUT);
    Assert.assertEquals(-1, checkin.compareTo(checkout));
    Assert.assertEquals(1, checkout.compareTo(checkin));

    checkout = new Record(date.plusDays(1), time, RecordType.CHECKOUT);
    Assert.assertEquals(-1, checkin.compareTo(checkout));
    Assert.assertEquals(1, checkout.compareTo(checkin));
  }

  @Test
  public void test_equality() {
    final Record r1 = new Record(date, time, RecordType.CHECKIN);
    final Record r2 = new Record(date, time, RecordType.CHECKIN);

    Assert.assertTrue(r1.equals(r2));
    Assert.assertTrue(r2.equals(r1));
  }

  @Test
  public void test_inequality() {
    final Record record = new Record(date, time, RecordType.CHECKIN);
    Record diff;

    // different type
    diff = new Record(date, time, RecordType.CHECKOUT);
    Assert.assertFalse(record.equals(diff));

    // different year
    diff = new Record(date.plusYears(1), time, RecordType.CHECKIN);
    Assert.assertFalse(record.equals(diff));

    // different month
    diff = new Record(date.plusMonths(1), time, RecordType.CHECKIN);
    Assert.assertFalse(record.equals(diff));

    // different day
    diff = new Record(date.plusDays(1), time, RecordType.CHECKIN);
    Assert.assertFalse(record.equals(diff));

    // different hour
    diff = new Record(date, time.plusHours(1), RecordType.CHECKIN);
    Assert.assertFalse(record.equals(diff));

    // different minute
    diff = new Record(date, time.plusMinutes(1), RecordType.CHECKIN);
    Assert.assertFalse(record.equals(diff));

    // different seconds
    diff = new Record(date, time.plusSeconds(1), RecordType.CHECKIN);
    Assert.assertFalse(record.equals(diff));

    // different nanosecond
    diff = new Record(date, time.plusNanos(1), RecordType.CHECKIN);
    Assert.assertFalse(record.equals(diff));

    Assert.assertFalse(record.equals(null));
    Assert.assertFalse(record.equals(new Object()));
  }
}