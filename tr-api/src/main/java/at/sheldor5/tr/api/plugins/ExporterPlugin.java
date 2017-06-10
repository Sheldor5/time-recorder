package at.sheldor5.tr.api.plugins;

import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Year;
import at.sheldor5.tr.api.user.User;

import java.io.OutputStream;
import javax.activation.MimeType;

public interface ExporterPlugin extends Plugin {

  /**
   * Initialize the Plugin by passing it the {@link OutputStream} of the HTTP Response.
   *
   * @param outputStream the {@link OutputStream} of the HTTP Response to write to.
   */
  void initialize(User user, OutputStream outputStream);

  /**
   * The property identifier for the
   * displayed name, required for language packs.
   *
   * @return the identifier for property files.
   */
  String displayNamePropertyIdentifier();

  /**
   * Get this Exporter's MIME-Type.
   * Used for the header field "Content-Type"
   * of HTTP responses.
   *
   * @return  the MIME-Type of the generated content.
   */
  MimeType getMimeType();

  /**
   * Export all records of a day in a human readable manner.
   *
   * @param day The day to report.
   * @return The stream which holds the content of this exporter.
   */
  void export(final Day day);

  /**
   * Export all records of a month in a human readable manner.
   * All records of a day should get summarized.
   *
   * @param month The month to report.
   * @return The stream which holds the content of this exporter.
   */
  void export(final Month month);

  /**
   * Export all records of a year in a human readable manner.
   * All records of a months should get summarized.
   *
   * @param year The year to report.
   * @return The stream which holds the content of this exporter.
   */
  void export(final Year year);

  /**
   * Export all records of a year in a human readable manner.
   * This type should report each single record.
   *
   * @param year The year to report.
   * @return The stream which holds the content of this exporter.
   */
  void fullExport(final Year year);

}
