package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.RecordType;
import at.sheldor5.tr.api.time.Session;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class TimeOperationTest {
  
  class TestTimeOperation extends TimeOperation {
    public TestTimeOperation(final LocalTime time, double multiplier, final Integer... days) {
      super(time, multiplier, days);
    }
    @Override
    public boolean applies(LocalTime time) {
      return false;
    }
    @Override
    public boolean applies(Session session) {
      return false;
    }
    @Override
    public Session split(Session session) {
      return null;
    }
  }
  
  private static final LocalTime time = LocalTime.of(12, 0);

  @Test(expected = NullPointerException.class)
  public void test_invalid_initialization_no_time() {
    new TestTimeOperation(null, 1.0D);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_initialization_invalid_day_1() {
    new TestTimeOperation(time, 1.0D, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_invalid_initialization_invalid_day_2() {
    new TestTimeOperation(time, 1.0D, 8);
  }

  @Test
  public void test_initialization() {
    final TestTimeOperation rule = new TestTimeOperation(time, 1.5D, 2, 4, 6);

    Assert.assertEquals("Time should be set", time, rule.time);
    Assert.assertEquals("Multiplier should be set", 1.5D, rule.multiplier, 0.0D);
    Assert.assertTrue("Day should apply", rule.days[1]);
    Assert.assertTrue("Day should apply", rule.days[3]);
    Assert.assertTrue("Day should apply", rule.days[5]);
    Assert.assertFalse("Day should not apply", rule.days[0]);
    Assert.assertFalse("Day should not apply", rule.days[2]);
    Assert.assertFalse("Day should not apply", rule.days[4]);
    Assert.assertFalse("Day should not apply", rule.days[6]);
  }

  @Test
  public void test_applies_to_day() {
    final TestTimeOperation rule = new TestTimeOperation(time, 1.5D, 1, 2, 3, 4, 5, 6);

    final Day monday = new Day(LocalDate.of(2017, 1, 2));
    Assert.assertTrue("Rule should apply", rule.applies(monday));

    final Day sunday = new Day(LocalDate.of(2017, 1, 1));
    Assert.assertFalse("Rule should not apply", rule.applies(sunday));
  }

  @Test
  public void test_update_session() {
    final LocalDate monday = LocalDate.of(2017, 1, 2);
    final LocalTime start_t = LocalTime.of(11, 0);
    final LocalTime end_t = LocalTime.of(13, 0);
    final Record begin = new Record(0, monday, start_t, RecordType.CHECKIN);
    final Record end = new Record(0, monday, end_t, RecordType.CHECKOUT);
    final TestTimeOperation rule = new TestTimeOperation(time, 1.5D, 1, 2, 3, 4, 5, 6);

    final Session session = new Session(monday, begin, end);

    rule.update(session);

    Assert.assertEquals("Session multiplier should be updated", 1.5D, session.getMultiplier(), 0.0D);
    Assert.assertEquals("Session summary should not be updated", 7200L, session.getSummary());
    Assert.assertEquals("Session valued summary should be updated", 10800L, session.getValuedSummary());
  }
  
}