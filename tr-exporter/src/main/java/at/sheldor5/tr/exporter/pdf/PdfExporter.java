package at.sheldor5.tr.exporter.pdf;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Year;
import at.sheldor5.tr.api.user.User;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.InputStream;
import java.io.OutputStream;
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

  private User user;
  private OutputStream outputStream;

  @Override
  public void initialize(User user, OutputStream outputStream) {
    this.user = user;
    this.outputStream = outputStream;
  }

  @Override
  public String displayNamePropertyIdentifier() {
    // time-recorder PDF
    return  "plugin.exporter.tr.pdf";
  }

  @Override
  public MimeType getMimeType() {
    return MIMETYPE;
  }

  @Override
  public void export(Day day) {

  }

  @Override
  public void export(Month month) {

  }

  @Override
  public void export(Year year) {

  }

  @Override
  public void fullExport(Year year) {

  }

  @Override
  public String getName() {
    return NAME;
  }
}