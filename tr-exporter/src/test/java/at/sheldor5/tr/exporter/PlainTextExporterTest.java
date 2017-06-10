package at.sheldor5.tr.exporter;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.exporter.html.HtmlExporter;
import at.sheldor5.tr.exporter.text.PlainTextExporter;
import at.sheldor5.tr.exporter.utils.ExporterUtils;
import at.sheldor5.tr.api.time.Year;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PlainTextExporterTest {

  private static final File FILE = new File("PlainTextExporter.html");
  private static final User USER = new User("Sheldor5", "", "Michael", "Palata");

  @BeforeClass
  public static void setup() throws IOException {
    if (!FILE.exists()) {
      if (!FILE.createNewFile()) {
        Assert.fail();
      }
    }
  }

  @Test
  public void exporter_mime_type_should_return_plain_text() {
    final PlainTextExporter exporter = new PlainTextExporter();
    Assert.assertEquals("text/plain", exporter.getMimeType().toString());
  }

  @Test
  public void testPlainTextExporter() throws IOException {
    Year year = getSimpleTestYear(2016);
    ExporterPlugin exporter = new PlainTextExporter();
    FileOutputStream fileOutputStream = new FileOutputStream(FILE);

    exporter.initialize(USER, fileOutputStream);

    exporter.export(year);

    fileOutputStream.flush();
    fileOutputStream.close();
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

        Session session = new Session(d_, t1, t2);
        day.addItem(session);

        session = new Session(d_, t3, t4);
        day.addItem(session);

        month.addItem(day);
      }
      year.addItem(month);
    }
    return year;
  }

  @AfterClass
  public static void teardown() throws IOException {
    Desktop.getDesktop().open(FILE);
  }

}