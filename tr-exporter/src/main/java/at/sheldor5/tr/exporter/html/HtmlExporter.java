package at.sheldor5.tr.exporter.html;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Year;
import at.sheldor5.tr.api.user.User;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

/**
 * @author Michael Palata
 * @date 10.06.2017
 */
public class HtmlExporter implements ExporterPlugin {

  private static final Logger LOGGER = Logger.getLogger(HtmlExporter.class.getName());

  private static final String NAME = "tr-html";
  private static final String DISPLAY_NAME_PROPERTY_IDENTIFIER = "plugin.exporter.tr.html";

  private static final MimeType MIMETYPE;

  static {
    try {
      MIMETYPE = new MimeType("text/html");
    } catch (final MimeTypeParseException e) {
      LOGGER.severe(e.getMessage());
      throw new RuntimeException("Unexpected Error!");
    }
  }

  private User user;
  private BufferedWriter writer;

  @Override
  public void initialize(User user, OutputStream outputStream) {
    this.user = user;
    writer = new BufferedWriter(new OutputStreamWriter(outputStream));
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String displayNamePropertyIdentifier() {
    return DISPLAY_NAME_PROPERTY_IDENTIFIER;
  }

  @Override
  public MimeType getMimeType() {
    return MIMETYPE;
  }

  private void begin() throws IOException {
    writer.write("<!DOCTYPE html><html><body>");
    BootstrapHtmlExporterHelper.header(writer, user);
  }

  private void end() throws IOException {
    BootstrapHtmlExporterHelper.footer(writer);
    writer.write("</body></html>");
    writer.flush();
  }

  @Override
  public void export(Day day) {
    try {
      begin();
      BootstrapHtmlExporterHelper.day(writer, day, true);
      end();
    } catch (final IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @Override
  public void export(Month month) {
    try {
      begin();
      BootstrapHtmlExporterHelper.month(writer, month, true);
      end();
    } catch (final IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @Override
  public void export(Year year) {
    return;
  }

  @Override
  public void fullExport(Year year) {
    return;
  }
}
