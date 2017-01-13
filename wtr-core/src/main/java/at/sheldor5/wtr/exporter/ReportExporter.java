package at.sheldor5.wtr.exporter;

import at.sheldor5.wtr.records.Day;
import at.sheldor5.wtr.records.Month;
import at.sheldor5.wtr.records.Year;

import java.io.InputStream;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public interface ReportExporter {

  /**
   * Get this {@link ReportExporter}'s MIME-Type.
   * Used for the header field "Content-Type"
   * of HTTP responses.
   *
   * @return  MIME-Type for the HTTP Header "Content-Type"
   */
  String getContentType();

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