package at.sheldor5.tr.api;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 20.01.2017.
 */
public @interface ExporterPlugin {
  String name();
  long id();
  String displayNamePropertyIdentifier();
}