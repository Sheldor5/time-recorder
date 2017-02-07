package at.sheldor5.tr.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  protected GlobalProperties() {
    // defeat instantiation
  }

  /**
   * Load properties from file.
   *
   * @param file The file to load.
   */
  public static void load(final File file) throws IOException {
    InputStream is;
    if (!file.exists()) {
      is = GlobalProperties.class.getResourceAsStream( "/" + file.getName());
    } else {
      is = new FileInputStream(file);
    }
    if (is == null) {
      final String executionPath = new File("").getAbsolutePath();
      final String errorMsg = "File \"" + file.getName() + "\" does not exist in: "+ executionPath;
      LOGGER.error(errorMsg);
      throw new IOException(errorMsg);
    }
    properties.load(is);
    is.close();
    LOGGER.debug("Loaded file: " + file.getName());
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