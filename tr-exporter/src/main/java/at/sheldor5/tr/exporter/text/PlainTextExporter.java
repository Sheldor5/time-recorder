package at.sheldor5.tr.exporter.text;

import at.sheldor5.tr.api.ExporterPlugin;
import at.sheldor5.tr.core.records.Day;
import at.sheldor5.tr.core.records.Month;
import at.sheldor5.tr.core.records.Year;
import at.sheldor5.tr.core.utils.TimeUtils;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.*;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class PlainTextExporter implements ExporterPlugin {

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

  @Override
  public MimeType getMimeType() {
    return MIMETYPE;
  }

  @Override
  public InputStream export(final Day day) {
    return null;
  }

  @Override
  public InputStream export(final Month month) {
    return null;
  }

  @Override
  public InputStream export(final Year year) {
    final StringBuilder sb = new StringBuilder();

    sb.append("Report for ").append(year.getValue()).append("\n\n");

    long millis, sum = 0;
    for (final Month month : year.getItems()) {
      millis = month.getSummary();
      sb.append(String.format("%-12s%s\n", getMonthString(month.getValue()), String.format("%25s", TimeUtils.getHumanReadableSummary(millis))));
      sum += millis;
    }
    sb.append(String.format("%37s\n", "").replace(' ', '_'));
    sb.append(String.format("%-12s%s", "Summe", String.format("%25s", TimeUtils.getHumanReadableSummary(sum))));

    try {
      return new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
    } catch (final UnsupportedEncodingException e) {
      // TODO LOGGING
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public InputStream fullExport(final Year year) {
    return null;
  }

  public static String getMonthString(int i) {
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
}