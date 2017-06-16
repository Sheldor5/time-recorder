package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.utils.GlobalProperties;
import org.junit.BeforeClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestFixture {

  private static boolean loaded = false;

  private static final String PROPERTIES = "/test.properties";

  @BeforeClass
  public static void init() throws IOException {
    if (loaded) {
      return;
    }
    GlobalProperties.load(TestFixture.class.getResourceAsStream(PROPERTIES));
    EntityManagerHelper.setupGlobalProperties();
    loaded = true;
  }

}
