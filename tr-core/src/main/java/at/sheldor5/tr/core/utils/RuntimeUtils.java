package at.sheldor5.tr.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * RuntimeUtils Class for Java Reflection Operations.
 * Usage: {@code List<Class<Plugin>> classes =
 * RuntimeUtils.getClassesImplementing(pluginsPath, Plugin.class);}.
 */
public class RuntimeUtils {

  /**
   * Class Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(RuntimeUtils.class);

  private static String executionPath = null;

  /**
   * Get a list of classes implementing a given Interface from JARs in a specific directory.
   *
   * @param directory The directory with the JARs containing the classes to search.
   * @param interfaceClazz The desired interface to search.
   * @param <T> The interface the classes should implement.
   * @return List of classes implementing the given interface.
   * @throws IOException On IO errors.
   * @throws IllegalArgumentException On class loading errors.
   */
  public static <T> List<Class<T>> getClassesImplementing(
      final String directory, final Class<T> interfaceClazz)
      throws IOException, IllegalArgumentException {
    // check arguments
    if (directory == null) {
      LOGGER.error("Directory is null!");
      throw new IllegalArgumentException("null");
    }

    final File folder = new File(directory);

    // check directory
    if (!folder.exists() || !folder.isDirectory()) {
      String executionPath;
      try {
        final File executionDirectory = new File(
            RuntimeUtils.class.getProtectionDomain().getCodeSource()
                .getLocation().toURI().getPath());
        executionPath = executionDirectory.getAbsolutePath();
      } catch (final URISyntaxException use) {
        executionPath = use.getMessage();
      }
      throw new IOException("Directory \"" + directory + "\" does not exist or is not a directory! "
          + "Current path is: " + executionPath);
    }

    final List<Class<T>> classes = new ArrayList<>();

    LOGGER.debug("Searching for classes implementing <"
        + interfaceClazz.getName() + "> in "
        + folder.getAbsolutePath());

    final File[] files = folder.listFiles();
    if (files == null) {
      LOGGER.info("Directory \"" + folder.getAbsolutePath() + "\" returned no files.");
      return classes;
    }

    // examine each jar in the directory
    for (final File file : files) {
      // search only in JAR and ZIP files
      if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
        // open as JAR
        final JarFile jarFile = new JarFile(file.getAbsolutePath());
        LOGGER.debug("Scanning \"" + file.getName() + "\"");
        final Enumeration<JarEntry> entries = jarFile.entries();
        final URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
        final URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls);

        // iterate through all files in the JAR
        while (entries.hasMoreElements()) {
          final JarEntry jarEntry = entries.nextElement();

          // only .class files
          if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
            continue;
          }

          // -6 because of ".class"
          final String className = jarEntry.getName().substring(
              0, jarEntry.getName().length() - 6).replace('/', '.');

          try {
            // load class into JVM
            final Class loadedClass = urlClassLoader.loadClass(className);
            LOGGER.debug("Testing class <" + className + ">");
            if (interfaceClazz.isAssignableFrom(loadedClass) && !loadedClass.isInterface()) {
              // only add classes to the returning list which implement the given class
              classes.add((Class<T>) loadedClass);
              LOGGER.debug("Found Match: <" + loadedClass.getName() + ">");
            }
          } catch (final ClassNotFoundException | NoClassDefFoundError ce) {
            LOGGER.error("Could not load class <" + jarEntry.getName() + ">: " + ce.getMessage());
          }
        }
      }
    }
    return classes;
  }
}