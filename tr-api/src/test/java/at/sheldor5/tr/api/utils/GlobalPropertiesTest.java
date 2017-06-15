package at.sheldor5.tr.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

public class GlobalPropertiesTest {

  private static final String TEST_CP_PROPERTIES = "/test_cp.properties";
  private static final String TEST_F_PROPERTIES = "/test_f.properties";

  @Test
  public void test_set_property() {
    GlobalProperties.setProperty("key", "value");
    Assert.assertEquals("value", GlobalProperties.getProperty("key"));
  }

  @Test
  public void test_load_file_from_classpath() throws IOException {
    GlobalProperties.load(GlobalPropertiesTest.class.getResourceAsStream(TEST_CP_PROPERTIES));
    Assert.assertEquals("1", GlobalProperties.getProperty("test.cp.property"));
  }

  @Test
  public void test_load_file_from_filesystem() throws IOException {
    GlobalProperties.load(GlobalPropertiesTest.class.getResourceAsStream(TEST_F_PROPERTIES));
    Assert.assertEquals("1", GlobalProperties.getProperty("test.f.property"));
  }
}
