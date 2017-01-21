package at.sheldor5.tr.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Michael Palata on 18.01.2017.
 */
public class GlobalProperties {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(GlobalProperties.class);

  /**
   * Object fields.
   */
  private static final Properties properties = new Properties();

  /**
   * Default Constructor to ensure Singleton.
   */
  private GlobalProperties() {
    // defeat instantiation
  }

  /**
   * Load a ".properties" file.
   * @param file The file to load.
   */
  public static void load(final File file) throws IOException {
    if (!file.exists()) {
      throw new FileNotFoundException("File not found: " + file.getName());
    }
    final FileInputStream inputStream = new FileInputStream(file);
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