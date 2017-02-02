package at.sheldor5.tr.core.rules;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class RuleTest {

  private static final List<String> workingDays = new ArrayList<>();
  private static final List<String> sunday = new ArrayList<>();

  @org.junit.Before
  public void init() {
    workingDays.add("monday");
    workingDays.add("tuesday");
    workingDays.add("wednesday");
    workingDays.add("thursday");
    workingDays.add("friday");
    workingDays.add("saturday");
    sunday.add("sunday");
  }

  @Test
  public void test_rule() {
    /*final Rule rule = new Rule();
    rule.name = "Test Rule";
    rule.keyDate = LocalDate.of(1900, 01, 01);
    final Before before = new Before(LocalTime.of(4, 0), 1.5D, workingDays);
    final After after = new After(Time.valueOf("22:00:00"), 1.5D, workingDays);
    rule.timeOperations.add(before);
    rule.timeOperations.add(after);

    final Date date = new Date(System.currentTimeMillis());
    final Time beginTime = TimeUtils.getTime(4, 0, 0, 0);
    final Time endTime = TimeUtils.getTime(23, 0, 0, 0);
    final Record begin = new Record(0, date, beginTime, RecordType.CHECKIN);
    final Record end = new Record(0, date, endTime, RecordType.CHECKOUT);

    final Session session = new Session(1.0D, begin, end);
    Assert.assertTrue("Rule failed to apply", rule.applies(session));*/
  }
}