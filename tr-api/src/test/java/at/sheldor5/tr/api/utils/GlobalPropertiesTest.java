package at.sheldor5.tr.api.utils;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class GlobalPropertiesTest {

  private static final String TEST_CP_PROPERTIES = "test_cp.properties";
  private static final String TEST_F_PROPERTIES = "src/test/resources/test_f.properties";

  @Test
  public void test_initialization() {
    final GlobalProperties properties = new GlobalProperties();
    Assert.assertNotNull("Initialization failed", properties);
  }

  @Test
  public void test_set_property() {
    GlobalProperties.setProperty("key", "value");
    Assert.assertEquals("Property should be set", "value", GlobalProperties.getProperty("key"));
  }

  @Test(expected = IOException.class)
  public void test_load_non_existing_file() throws IOException {
    GlobalProperties.load(new File("invalid.properties"));
  }

  @Test
  public void test_load_file_from_classpath() throws IOException {
    GlobalProperties.load(new File(TEST_CP_PROPERTIES));
    Assert.assertEquals("Properties should be loaded", "1", GlobalProperties.getProperty("test.cp.property"));
  }

  @Test
  public void test_load_file_from_filesystem() throws IOException {
    GlobalProperties.load(new File(TEST_F_PROPERTIES));
    Assert.assertEquals("Properties should be loaded", "1", GlobalProperties.getProperty("test.f.property"));
  }
}