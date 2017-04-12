package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.utils.GlobalProperties;
import at.sheldor5.tr.persistence.PersistenceManager;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PersistenceManagerTest {

  private static final String PROPERTIES = "test.properties";

  @BeforeClass
  public static void init() throws IOException {
    GlobalProperties.load(new File(PROPERTIES));
  }

  @Test
  public void test() {
    final Configuration configuration = PersistenceManager.getConfiguration();
    Assert.assertNotNull(configuration);
  }
}
