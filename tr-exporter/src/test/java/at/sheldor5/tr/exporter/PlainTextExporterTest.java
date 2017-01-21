package at.sheldor5.tr.exporter;

import at.sheldor5.tr.api.ExporterPlugin;
import at.sheldor5.tr.exporter.utils.ExporterUtils;
import at.sheldor5.tr.core.records.RecordsTest;
import at.sheldor5.tr.core.records.Year;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class PlainTextExporterTest extends ExporterUtils {

  private static final File OUTPUT = new File("target/export.txt");

  @Before
  public void init() {

  }

  @Test
  public void exporter_mime_type_should_return_plain_text() {
    final PlainTextExporter exporter = new PlainTextExporter();
    Assert.assertEquals("text/plain", exporter.getMimeType().toString());
  }

  @Test
  public void testPlainTextExporter() {
    Year year = RecordsTest.getSimpleTestYear(2016);
    ExporterPlugin exporter = new PlainTextExporter();
    InputStream inputStream = exporter.export(year);
    ExporterUtils.toFile(inputStream, OUTPUT);
  }

}