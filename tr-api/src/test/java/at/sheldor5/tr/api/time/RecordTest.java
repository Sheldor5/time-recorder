package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class RecordTest {

  private static final LocalDate date = LocalDate.of(2017, 1, 1);
  private static final LocalTime time = LocalTime.of(8, 0);

  @Test(expected = IllegalArgumentException.class)
  public void should_throw_on_setting_invalid_id() {
    final Record record = new Record();
    record.setId(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_throw_on_setting_invalid_date() {
    final Record record = new Record();
    record.setDate(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_throw_on_setting_invalid_time() {
    final Record record = new Record();
    record.setTime(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_throw_on_setting_invalid_type() {
    final Record record = new Record();
    record.setType(null);
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
  public void should_return_negative_comparison_value() {
    final Record record = new Record(date, time, RecordType.CHECKIN);

    final Record nextHour = new Record(date, time.plusHours(1), RecordType.CHECKOUT);
    Assert.assertEquals(-1, record.compareTo(nextHour));

    final Record nextDay = new Record(date.plusDays(1), time, RecordType.CHECKOUT);
    Assert.assertEquals(-1, record.compareTo(nextDay));
  }

  @Test
  public void should_return_negative_positive_value() {
    final Record record = new Record(date, time, RecordType.CHECKIN);

    final Record previousHour = new Record(date, time.plusHours(1), RecordType.CHECKOUT);
    Assert.assertEquals(1, previousHour.compareTo(record));

    final Record previousDay = new Record(date.plusDays(1), time, RecordType.CHECKOUT);
    Assert.assertEquals(1, previousDay.compareTo(record));
  }

  @Test
  public void should_be_equal() {
    final Record record = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);
    final Record other = new Record(LocalDate.of(2017, 1, 1), LocalTime.of(8, 0), RecordType.CHECKIN);

    Assert.assertTrue(record.equals(other));
  }

  @Test
  public void should_not_be_equal() {
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