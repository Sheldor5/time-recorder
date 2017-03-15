package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.persistence.database.Database;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseAuthenticationTest {

  private static final File PROPERTIES = new File("db.properties");

  private static final String USER_PREFIX = "testuser_";
  private static final String FORENAME = "Test";
  private static final String SURNAME = "User";
  private static final String PASSWORD = "password";

  private static Database database;
  private static DatabaseAuthentication auth;

  @BeforeClass
  public static void init() throws IOException, SQLException {
    //GlobalProperties.load(PROPERTIES);

    /*Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "sa");
    database = new Database(connection);*/

    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    dataSource.setUser("sa");
    dataSource.setPassword("sa");

    auth = new DatabaseAuthentication();
    auth.setDataSource(dataSource);
    auth.initialize();
  }

  @Test
  public void test_add_user() {
    final User user = new User(User.getRandomUsername(USER_PREFIX), FORENAME, SURNAME);
    auth.addUser(user, PASSWORD);
    Assert.assertNotNull(user.getUuid());
  }

  @Test
  public void test_get_user() {
    final User expected = new User(User.getRandomUsername(USER_PREFIX), FORENAME, SURNAME);
    auth.addUser(expected, PASSWORD);
    Assert.assertNotNull(expected.getUuid());

    final User actual = auth.getUser(expected.getUsername(), PASSWORD);
    Assert.assertNotNull(actual);
    Assert.assertNotNull(actual.getUuid());
    Assert.assertEquals(expected, actual);
  }
}