package at.sheldor5.tr.exporter.pdf;

import at.sheldor5.tr.api.Exporter;
import at.sheldor5.tr.api.ExporterPlugin;
import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Month;
import at.sheldor5.tr.api.objects.Year;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.InputStream;

@Exporter(name = "defaultPDF", displayNamePropertyIdentifier = "time-recorder PDF")
public class PdfExporter implements ExporterPlugin {

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