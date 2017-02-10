package at.sheldor5.tr.exporter;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Month;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.RecordType;
import at.sheldor5.tr.api.objects.Session;
import at.sheldor5.tr.exporter.text.PlainTextExporter;
import at.sheldor5.tr.exporter.utils.ExporterUtils;
import at.sheldor5.tr.api.objects.Year;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

public class PlainTextExporterTest {

  private static final File OUTPUT = new File("target/export.txt");

  @Test
  public void exporter_mime_type_should_return_plain_text() {
    final PlainTextExporter exporter = new PlainTextExporter();
    Assert.assertEquals("text/plain", exporter.getMimeType().toString());
  }

  @Test
  public void testPlainTextExporter() throws IOException {
    Year year = getSimpleTestYear(2016);
    ExporterPlugin exporter = new PlainTextExporter();
    InputStream inputStream = exporter.export(year);
    ExporterUtils.toFile(inputStream, OUTPUT);
  }



  public static Year getSimpleTestYear(int y) {
    final LocalDate date = LocalDate.of(y, 1, 1);
    Year year = new Year(date);
    LocalTime t1 = LocalTime.of(8, 0);
    LocalTime t2 = LocalTime.of(12, 0);
    LocalTime t3 = LocalTime.of(12, 30);
    LocalTime t4 = LocalTime.of(16, 30);
    for (int m = 12; m > 0; m--) {
      final LocalDate m_ = LocalDate.of(y, m, 1);
      Month month = new Month(m_);
      for (int d = 28; d > 0; d--) {
        final LocalDate d_ = LocalDate.of(y, m, d);
        Day day = new Day(d_);

        Session session = new Session(
                d_,
                new Record(d_, t1, RecordType.CHECKIN),
                new Record(d_, t2, RecordType.CHECKOUT));
        day.addItem(session);

        session = new Session(
                d_,
                new Record(d_, t3, RecordType.CHECKIN),
                new Record(d_, t4, RecordType.CHECKOUT));
        day.addItem(session);

        month.addItem(day);
      }
      year.addItem(month);
    }
    return year;
  }

}