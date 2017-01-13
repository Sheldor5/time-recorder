package at.sheldor5.wtr.exporter.pdf;

import at.sheldor5.wtr.exporter.ReportExporter;
import at.sheldor5.wtr.records.Day__;
import at.sheldor5.wtr.records.Month__;
import at.sheldor5.wtr.records.Year__;

import java.io.InputStream;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class PdfExporter implements ReportExporter {
  @Override
  public String getContentType() {
    return "application/pdf";
  }

  @Override
  public InputStream export(Day__ day) {
    return null;
  }

  @Override
  public InputStream export(Month__ month) {
    return null;
  }

  @Override
  public InputStream export(Year__ year) {
    return null;
  }

  @Override
  public InputStream fullExport(Year__ year) {
    return null;
  }
}