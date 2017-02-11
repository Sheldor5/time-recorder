package at.sheldor5.tr.core.auth;

import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import at.sheldor5.tr.core.persistence.DatabaseConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DatabaseAuthenticationTest {

  private static final File PROPERTIES = new File("db.properties");

  private static final String USER_PREFIX = "testuser_";
  private static final String FORENAME = "Test";
  private static final String SURNAME = "User";
  private static final String PASSWORD = "password";

  private static DatabaseConnection databaseConnection;
  private static DatabaseAuthentication auth;

  @BeforeClass
  public static void init() throws IOException, SQLException {
    GlobalProperties.load(PROPERTIES);

    SQLServerDataSource dataSource = new SQLServerDataSource();
    dataSource.setURL(DatabaseConnection.getJDBCUrl());

    databaseConnection = new DatabaseConnection(dataSource.getConnection());
    databaseConnection.initialize();

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
    Assert.assertNotNull(expected.getUuid());
    Assert.assertEquals(expected, actual);
  }
}