package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.RecordType;
import at.sheldor5.tr.core.utils.GlobalProperties;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 20.01.2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseMapperTest {

  private static final File GLOBAL_PROPERTIES = new File("global.properties");
  private static final String USER_NAME = "testuser";
  private static final String USER_PASS = "testpass";
  private static final String USER_FORE = "Michael";
  private static final String USER_SUR = "Palata";

  private static final User user = new User(0, USER_NAME, USER_FORE, USER_SUR);
  private static final Record record = new Record(-1, Record.now(), RecordType.CHECKIN);

  private Connection connection;
  private DatabaseMapper storage;
  private int userId;
  private int recordId;

  @Before
  public void init() throws IOException, SQLException {
    GlobalProperties.load(GLOBAL_PROPERTIES);
    GlobalProperties.setProperty("db.jdbc.url", "jdbc:sqlserver://127.0.0.1;database=TimeRecorder;user=user;password=pass;CharacterSet=UTF-8");
    connection = DatabaseConnection.getConnection();
    Assume.assumeFalse("Connection could not be established", connection == null);
    storage = new DatabaseMapper(connection);
    if (!storage.tablesExist()) {
      storage.createTables();
    }
    Assume.assumeTrue("Tables could not be created", storage.tablesExist());
  }

  @Test
  public void a_test_string_to_md5() throws NoSuchAlgorithmException {
    Assert.assertEquals("MD5 calculation failed", "179ad45c6ce2cb97cf1029e212046e81", DatabaseMapper.getMD5(USER_PASS));
  }

  @Test
  public void b_test_add_user() throws SQLException, NoSuchAlgorithmException {
    userId = storage.addUser(user, USER_PASS);
    Assert.assertFalse("User ID must not be negative", user.getUserId() < 0);
  }

  @Test(expected = SQLException.class)
  public void c_test_user_uniqueness() throws SQLException, NoSuchAlgorithmException {
    try {
      storage.addUser(user, USER_PASS);
    } catch (final SQLException sqle) {
      Assert.assertTrue("Exception should be thrown by UNIQUE KEY violation", sqle.getMessage().contains("UNIQUE KEY"));
      throw sqle;
    }
  }

  @Test
  public void d_test_get_user() throws SQLException, NoSuchAlgorithmException, InstantiationException {
    final User actual = storage.getUser(USER_NAME, USER_PASS);
    Assert.assertEquals("Previously inserted test user should be returned", user, actual);
  }

  @Test
  public void e_test_add_record() throws SQLException {
    recordId = storage.addRecord(user, record);
    Assert.assertTrue("Record ID should be positiv", record.getId() > 0);
  }

  @Test
  public void f_test_get_record() throws SQLException {
    final Record actual = storage.getRecord(record.getId());
    Assert.assertTrue("Previously inserted record should be returned", record.equals(actual));
  }

}