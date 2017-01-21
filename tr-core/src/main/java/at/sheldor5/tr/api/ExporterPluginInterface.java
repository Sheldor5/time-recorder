package at.sheldor5.tr.api;

import at.sheldor5.tr.core.records.Day;
import at.sheldor5.tr.core.records.Month;
import at.sheldor5.tr.core.records.Year;

import javax.activation.MimeType;
import java.io.InputStream;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public interface ExporterPluginInterface {

  /**
   * Get this {@link ExporterPluginInterface}'s MIME-Type.
   * Used for the header field "Content-Type"
   * of HTTP responses.
   *
   * @return  MIME-Type for the HTTP Header "Content-Type"
   */
  MimeType getMimeType();

  /**
   * Export all records of a day in a human readable manner.
   *
   * @param day The day to report.
   * @return
   */
  InputStream export(final Day day);

  /**
   * all days of a month (only hours of the days,
   * no single records)
   *
   * @param month
   * @return
   */
  InputStream export(final Month month);

  /**
   * all months of a year (only hours of the months,
   * no single days or records)
   *
   * @param year
   * @return
   */
  InputStream export(final Year year);

  /**
   * every single record of the whole year
   *
   * @param year
   * @return
   */
  InputStream fullExport(final Year year);

}