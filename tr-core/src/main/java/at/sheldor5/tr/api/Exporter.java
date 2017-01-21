package at.sheldor5.tr.api;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 20.01.2017.
 */
public @interface Exporter {

  /**
   * Short name of this Exporter.
   *
   * @return The name of this exporter.
   */
  String name();

  /**
   * The property identifier for the
   * displayed name, required for language packs.
   *
   * @return The identifier for property files.
   */
  String displayNamePropertyIdentifier();
}