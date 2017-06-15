package at.sheldor5.tr.exporter;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.exporter.html.HtmlExporter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Michael Palata
 * @date 10.06.2017
 */
public class HtmlExporterTest {

  private static final File FILE = new File("HtmlExporter.html");
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
  public void test() throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(FILE);

    HtmlExporter exporter = new HtmlExporter();
    exporter.initialize(USER, fileOutputStream);

    Day day = TestUtils.getDefaultDay();
    Month month = TestUtils.getDefaultMonth();
    exporter.export(month);

    fileOutputStream.close();
  }

  @AfterClass
  public static void teardown() throws IOException {
    //Desktop.getDesktop().open(FILE);
  }

}
