package at.sheldor5.tr.api;

import at.sheldor5.tr.core.records.Day;
import at.sheldor5.tr.core.records.Month;
import at.sheldor5.tr.core.records.Year;
import javax.activation.MimeType;
import java.io.InputStream;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public interface ExporterPlugin {

  /**
   * Get this Exporter's MIME-Type.
   * Used for the header field "Content-Type"
   * of HTTP responses.
   *
   * @return  MIME-Type of the generated content.
   */
  MimeType getMimeType();

  /**
   * Export all records of a day in a human readable manner.
   *
   * @param day The day to report.
   * @return The stream which holds the content of this exporter.
   */
  InputStream export(final Day day);

  /**
   * Export all records of a month in a human readable manner.
   * All records of a day should get summarized.
   *
   * @param month The month to report.
   * @return The stream which holds the content of this exporter.
   */
  InputStream export(final Month month);

  /**
   * Export all records of a year in a human readable manner.
   * All records of a months should get summarized.
   *
   * @param year The year to report.
   * @return The stream which holds the content of this exporter.
   */
  InputStream export(final Year year);

  /**
   * Export all records of a year in a human readable manner.
   * This type should report each single record.
   *
   * @param year The year to report.
   * @return The stream which holds the content of this exporter.
   */
  InputStream fullExport(final Year year);

}