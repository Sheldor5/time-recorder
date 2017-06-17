package at.sheldor5.tr.exporter.text;

import at.sheldor5.tr.api.plugins.ExporterPlugin;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Year;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.TimeUtils;
import at.sheldor5.tr.exporter.pdf.PdfExporter;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.*;
import java.util.logging.Logger;

public class PlainTextExporter implements ExporterPlugin {

  private static final Logger LOGGER = Logger.getLogger(PdfExporter.class.getName());

  private static final String NAME = "tr-text";

  private static final String MONTH_SUMMARY_FORMAT = "%02dh %02dm %02ds";
  private static final MimeType MIMETYPE;

  static {
    try {
      MIMETYPE = new MimeType("text/plain");
    } catch (final MimeTypeParseException e) {
      e.printStackTrace();
      throw new RuntimeException(PlainTextExporter.class.getName() + ": Unexpected Error!");
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
    // time-recorder Text
    return  "plugin.exporter.tr.text";
  }

  @Override
  public MimeType getMimeType() {
    return MIMETYPE;
  }

  @Override
  public void export(final Day day) {

  }

  @Override
  public void export(final Month month) {

  }

  @Override
  public void export(final Year year) {
    final StringBuilder sb = new StringBuilder();

    sb.append("Report for ").append(year.getDate().getYear()).append("\n\n");

    long millis, sum = 0;
    for (final Month month : year.getItems()) {
      millis = month.getSummary();
      sb.append(String.format("%-12s%s\n", getMonthString(month.getDate().getMonthValue()), String.format("%25s", TimeUtils.getHumanReadableSummary(millis))));
      sum += millis;
    }
    sb.append(String.format("%37s\n", "").replace(' ', '_'));
    sb.append(String.format("%-12s%s", "Summe", String.format("%25s", TimeUtils.getHumanReadableSummary(sum))));

    try {
      outputStream.write(sb.toString().getBytes("UTF-8"));
    } catch (final IOException e) {
      // TODO LOGGING
      e.printStackTrace();
    }
  }

  @Override
  public void fullExport(final Year year) {

  }

  private static String getMonthString(int i) {
    switch (i) {
      case 1:
        return "Jänner";
      case 2:
        return "Februar";
      case 3:
        return "März";
      case 4:
        return "April";
      case 5:
        return "Mai";
      case 6:
        return "Juni";
      case 7:
        return "Juli";
      case 8:
        return "August";
      case 9:
        return "September";
      case 10:
        return "Oktober";
      case 11:
        return "November";
      case 12:
        return "Dezember";
      default:
        return "unknown";
    }
  }

  @Override
  public String getName() {
    return NAME;
  }
}