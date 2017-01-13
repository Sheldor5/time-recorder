package at.sheldor5.wtr.exporter;

import at.sheldor5.wtr.records.RecordsTest;
import at.sheldor5.wtr.records.Year;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class PlainTextExporterTest extends ExportHelper {

  private static final File OUTPUT = new File("target/export.txt");

  @Test
  public void testPlainTextExporter() {
    Year year = RecordsTest.getSimpleTestYear(2016);
    ReportExporter exporter = new PlainTextExporter();
    InputStream inputStream = exporter.export(year);
    ExportHelper.toFile(inputStream, OUTPUT);
  }

}