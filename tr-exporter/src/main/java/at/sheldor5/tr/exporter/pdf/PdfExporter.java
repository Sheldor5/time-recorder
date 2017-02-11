package at.sheldor5.tr.exporter.pdf;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Year;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.InputStream;
import java.util.logging.Logger;

public class PdfExporter implements ExporterPlugin {

  private static final Logger LOGGER = Logger.getLogger(PdfExporter.class.getName());

  private static final String NAME = "tr-pdf";

  private static final MimeType MIMETYPE;

  static {
    try {
      MIMETYPE = new MimeType("application/pdf");
    } catch (final MimeTypeParseException e) {
      LOGGER.severe(e.getMessage());
      throw new RuntimeException("Unexpected Error!");
    }
  }

  @Override
  public String displayNamePropertyIdentifier() {
    // time-recorder PDF
    return  "tr.exporter.pdf";
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

  @Override
  public String getName() {
    return NAME;
  }
}