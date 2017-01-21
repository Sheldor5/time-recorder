package at.sheldor5.tr.exporter.pdf;

import at.sheldor5.tr.api.ExporterPluginInterface;
import at.sheldor5.tr.core.records.Day;
import at.sheldor5.tr.core.records.Month;
import at.sheldor5.tr.core.records.Year;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.InputStream;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class PdfExporter implements ExporterPluginInterface {

  private static final MimeType MIMETYPE;

  static {
    try {
      MIMETYPE = new MimeType("application/pdf");
    } catch (final MimeTypeParseException e) {
      e.printStackTrace();
      throw new RuntimeException(PdfExporter.class.getName() + ": Unexpected Error!");
    }
  }

  @Override
  public MimeType getMimeType() {
    return MIMETYPE;
  }

  @Override
  public InputStream export(Day day) {
    return null;
  }

  @Override
  public InputStream export(Month month) {
    return null;
  }

  @Override
  public InputStream export(Year year) {
    return null;
  }

  @Override
  public InputStream fullExport(Year year) {
    return null;
  }
}