package at.sheldor5.wtr.records;

import at.sheldor5.wtr.exporter.PlainTextExporter;
import at.sheldor5.wtr.exporter.ReportExporter;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class RecordsTest {

  @Test
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
  }

  public static Year getSimpleTestYear(int y) {
    Year year = new Year(y);
    for (int m = 12; m > 0; m--) {
      Month month = new Month(m);
      for (int d = 28; d > 0; d--) {
        Day day = new Day(d);
        day.addItem(new Record(Timestamp.valueOf(LocalDateTime.of(2016, m, d, 8, 0)), RecordType.CHECKIN));
        day.addItem(new Record(Timestamp.valueOf(LocalDateTime.of(2016, m, d, 12, 0)), RecordType.CHECKOUT));
        day.addItem(new Record(Timestamp.valueOf(LocalDateTime.of(2016, m, d, 12, 30)), RecordType.CHECKIN));
        day.addItem(new Record(Timestamp.valueOf(LocalDateTime.of(2016, m, d, 16, 30)), RecordType.CHECKOUT));
        month.addItem(day);
      }
      year.addItem(month);
    }
    return year;
  }

}