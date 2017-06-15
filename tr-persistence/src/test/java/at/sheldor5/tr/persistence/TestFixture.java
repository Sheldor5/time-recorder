package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.utils.GlobalProperties;
import org.junit.BeforeClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestFixture {

  private static final String PROPERTIES = "/test.properties";

  @BeforeClass
  public static void init() throws IOException {
    GlobalProperties.load(TestFixture.class.getResourceAsStream(PROPERTIES));
    EntityManagerHelper.setupGlobalProperties();
  }

}
