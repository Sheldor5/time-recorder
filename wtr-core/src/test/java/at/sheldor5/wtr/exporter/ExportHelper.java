package at.sheldor5.wtr.exporter;

import at.sheldor5.wtr.records.Day__;
import at.sheldor5.wtr.records.Month__;
import at.sheldor5.wtr.records.Year__;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class ExportHelper {

  private static final int BUF_LEN = 1024;

  public static void toFile(final InputStream in, final File destination) {
    try (FileOutputStream out = new FileOutputStream(destination)) {
      byte buffer[] = new byte[BUF_LEN];
      int i;
      while ((i = in.read(buffer, 0, BUF_LEN)) > 0) {
        out.write(buffer, 0, i);
      }
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void fillYear(final Year__ year) {
    for (int m = 1; m < 13; m++) {
      Month__ month = new Month__(m);
      for (int d = 1; d < 32; d++) {
        Day__ day = new Day__(d);
        month.addDay(day);
      }
      year.addMonth(month);
    }
  }
}
