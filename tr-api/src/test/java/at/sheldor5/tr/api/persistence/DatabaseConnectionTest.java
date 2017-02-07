package at.sheldor5.tr.api.persistence;

import at.sheldor5.tr.api.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class DatabaseConnectionTest {

  private static DatabaseConnection connection;

  @Before
  public void init() throws IOException, SQLException {
    GlobalProperties.load(new File("global.properties"));

    connection = DatabaseConnection.getInstance();
    Assume.assumeNotNull(connection);
  }

  @Test
  public void test_tables_exist() throws SQLException {
    Assert.assertTrue("System table \"time-recorder\" not found", connection.tableExists("time-recorder"));
    Assert.assertTrue("System table \"records\" not found", connection.tableExists("records"));
  }
}