package at.sheldor5.tr.persistence.database;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TableTest {

  @Test
  public void test_test_table() throws SQLException {
    Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "sa");
    Database database = new Database(connection);
    Table test = new TestTable(connection);
    Assert.assertFalse(test.check());
    database.executeScript("/sql/h2/test.sql");
    Assert.assertTrue(test.check());
  }
}
