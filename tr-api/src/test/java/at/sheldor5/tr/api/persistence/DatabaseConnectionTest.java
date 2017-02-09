package at.sheldor5.tr.api.persistence;

import at.sheldor5.tr.api.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseConnectionTest {

  private static final File PROPERTIES = new File("test.properties");

  private static Connection connection;
  private static DatabaseConnection databaseConnection;

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(PROPERTIES);
    connection = DatabaseConnection.getConnection();
    databaseConnection = new DatabaseConnection(connection);
    databaseConnection.initialize();
  }

  @Test
  public void test_tables_exist() throws SQLException {
    Assert.assertTrue(databaseConnection.tableExists("time-recorder"));
    Assert.assertTrue(databaseConnection.tableExists("records"));
  }

  @AfterClass
  public static void cleanup() throws SQLException {
    connection.close();
  }
}