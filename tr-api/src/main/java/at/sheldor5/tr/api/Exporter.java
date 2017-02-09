package at.sheldor5.tr.api;

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