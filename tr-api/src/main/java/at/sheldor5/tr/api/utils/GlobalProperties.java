package at.sheldor5.tr.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class GlobalProperties {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = Logger.getLogger(GlobalProperties.class.getName());

  /**
   * Object fields.
   */
  private static final Properties properties = new Properties();

  /**
   * Default Constructor to ensure Singleton.
   */
  protected GlobalProperties() {
    // defeat instantiation
  }

  /**
   * Load properties from file.
   *
   * @param file The file to load.
   */
  public static void load(final InputStream inputStream) throws IOException {
    if (inputStream == null) {
      return;
    }
    properties.load(inputStream);
    inputStream.close();
  }

  /**
   * Get a property by its key.
   *
   * @param key The property's key.
   * @return Value of the key, null if not found.
   */
  public static String getProperty(final String key) {
    return properties.getProperty(key);
  }

  public static int getInteger(final String key) {
    return Integer.parseInt(properties.getProperty(key));
  }

  public static boolean getBoolean(final String key) {
    return "true".equals(properties.getProperty(key));
  }

  /**
   * Set a key-value property.
   *
   * @param key The property's key.
   * @param value The property's value for this key.
   */
  public static void setProperty(final String key, final String value) {
    properties.setProperty(key, value);
  }
}