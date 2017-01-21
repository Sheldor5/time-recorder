package at.sheldor5.tr.core.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 20.01.2017.
 */
public class RuntimeUtilsTest {

  @Before
  public void init() throws IOException {
    GlobalProperties.load(new File("global.properties"));
  }

  @Test(expected = IOException.class)
  public void testRuntimeUtils_shouldThrowException() throws IOException {
    RuntimeUtils.getClassesImplementing("non_existing_folder", RuntimeUtils.class);
  }

  // TODO
  /*@Test
  public void testRuntimeUtils_shouldReturnNoPlugin() throws IOException {
    Assert.assertEquals("Should return no plugin", 0,
            RuntimeUtils.getClassesImplementing(GlobalProperties.getProperty("plugins.dir"), ExporterPluginInterface.class).size());
  }

  @Test
  public void testRuntimeUtils_shouldReturnPlugin() throws IOException {
    Assert.assertEquals("Should return exactly 1 plugin", 1,
            RuntimeUtils.getClassesImplementing(GlobalProperties.getProperty("plugins.dir"), ExporterPluginInterface.class).size());
  }*/
}