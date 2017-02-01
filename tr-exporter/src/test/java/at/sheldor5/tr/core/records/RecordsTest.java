package at.sheldor5.tr.core.records;

import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Month;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.RecordType;
import at.sheldor5.tr.api.objects.Session;
import at.sheldor5.tr.api.objects.Year;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class RecordsTest {

  /*@Test
  public void testTimestampConversion() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    System.out.println("Current time:\t" + timestamp);
    System.out.println("Day started at:\t" + Record.getStartOfDay(timestamp));
    System.out.println("Day ends at:\t" + Record.getEndOfDay(timestamp));

    long millis = Record.getEndOfDay(timestamp).getTime() - timestamp.getTime();
    long second = (millis / 1000) % 60;
    long minute = (millis / (1000 * 60)) % 60;
    long hour = (millis / (1000 * 60 * 60)) % 24;
    String time = String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);

    System.out.println("Day ends in:\t" + time);
  }

  @Test
  public void testRecords() {
    Year year = getSimpleTestYear(2016);

    long millis = year.getSummary();
    long second = (millis / 1000) % 60;
    long minute = (millis / (1000 * 60)) % 60;
    long hour = (millis / (1000 * 60 * 60));
    String time = String.format("%02dh %02dm %02ds", hour, minute, second);
    Assert.assertEquals((8 * 28 * 12), hour);
    System.out.println("Time worked this year:\t" + time);
  }*/

  public static Year getSimpleTestYear(int y) {
    Year year = new Year(y);
    LocalTime t1 = LocalTime.of(8, 0);
    LocalTime t2 = LocalTime.of(12, 0);
    LocalTime t3 = LocalTime.of(12, 30);
    LocalTime t4 = LocalTime.of(16, 30);
    for (int m = 12; m > 0; m--) {
      Month month = new Month(m);
      for (int d = 28; d > 0; d--) {
        Day day = new Day(d);
        LocalDate date = LocalDate.of(y, m, d);

        Session session = new Session(d);
        session.build(new Record(date, t1, RecordType.CHECKIN),
                new Record(date, t2, RecordType.CHECKOUT));
        day.addItem(session);

        session = new Session(d);
        session.build(new Record(date, t3, RecordType.CHECKIN),
                new Record(date, t4, RecordType.CHECKOUT));
        day.addItem(session);

        month.addItem(day);
      }
      year.addItem(month);
    }
    return year;
  }

}