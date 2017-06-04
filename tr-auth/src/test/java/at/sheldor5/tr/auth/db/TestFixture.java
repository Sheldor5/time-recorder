package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.persistence.EntityManagerHelper;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;

public class TestFixture {

  private static final String PROPERTIES = "db.properties";

  @BeforeClass
  public static void init() throws IOException {
    GlobalProperties.load(new File(PROPERTIES));
    EntityManagerHelper.setupGlobalProperties();
  }

}
