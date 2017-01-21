package at.sheldor5.tr.core.objects;

import at.sheldor5.tr.core.records.Day;
import at.sheldor5.tr.core.records.RecordType;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.sheldor5.tr.core.utils.StringUtils;
import at.sheldor5.tr.core.utils.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
class PersistenceManager {

  private static final Logger LOGGER = LogManager.getLogger(PersistenceManager.class);

  private static final String INSERT_USER = "INSERT INTO [users] ([username], [password], [forename], [surname]) VALUES (?, ?, ?, ?)";
  private static final String SELECT_USER = "SELECT [pk_user_id], [username], [forename], [surname] FROM [users] WHERE [username] = ? AND [password] = ?";

  private static final String INSERT_RECORD = "INSERT INTO [records] ([fk_user_id], [date], [time], [type]) VALUES (?, ?, ?, ?)";
  private static final String SELECT_RECORD = "SELECT * FROM [records] WHERE [pk_record_id] = ?";

  private static final String SELECT_RECORDS_OF_DAY = "SELECT * FROM [records] WHERE [fk_user_id] = ? AND [date] = ?";
  private static final String SELECT_RECORDS_OF_MONTH = "SELECT * FROM [records] WHERE [fk_user_id] = ? AND [date] >= ? AND [date] <= ?";
  private static final String SELECT_RECORDS_OF_YEAR = "SELECT * FROM [records] WHERE [fk_user_id] = ? AND [date] >= ? AND [date] <= ?";

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  private final Connection connection;

  public PersistenceManager(final Connection connection) {
    this.connection = connection;
  }

  /**
   *
   * @param user
   * @param plainTextPassword
   * @return Primary key (a.k.a. User ID, column [pk_user_id]) of the inserted user.
   * @throws SQLException
   * @throws NoSuchAlgorithmException
   */
  int addUser(final User user, final String plainTextPassword) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(
            INSERT_USER,
            Statement.RETURN_GENERATED_KEYS);

    statement.setString(1, user.getUsername());
    statement.setString(2, StringUtils.getMD5(plainTextPassword));
    statement.setString(3, user.getForename());
    statement.setString(4, user.getSurname());

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Adding user \"{}\" ({} {}) with password \"{}\"",
              user.getUsername(),
              user.getForename(),
              user.getSurname(),
              plainTextPassword);
    }

    if (statement.executeUpdate() == 0) {
      connection.rollback();
      throw new SQLException("Could not create user, insertion failed");
    }

    int userId;
    try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
      if (generatedKeys.next()) {
        userId = generatedKeys.getInt(1);
        LOGGER.debug("Successfully created user with ID {}", userId);
      }
      else {
        connection.rollback();
        throw new SQLException("Creating user failed, no ID obtained");
      }
    }

    connection.commit();
    user.setId(userId);
    return userId;
  }

  User getUser(final String username, final String plainTextPassword) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(
            SELECT_USER,
            Statement.RETURN_GENERATED_KEYS);

    statement.setString(1, username);
    statement.setString(2, StringUtils.getMD5(plainTextPassword));

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Searching for user \"{}\" with password \"{}\"", username, plainTextPassword);
    }

    final ResultSet result = statement.executeQuery();

    final User user = new User();

    if (result.next()) {
      user.setId(result.getInt("pk_user_id"));
      user.setUsername(result.getString("username"));
      user.setForename(result.getString("forename"));
      user.setSurname(result.getString("surname"));
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Found user {} ({} {}) with ID {}",
                user.getUsername(),
                user.getForename(),
                user.getSurname(),
                user.getId());
      }
    } else {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("User \"{}\" with password \"{}\" was not found", username, plainTextPassword);
      }
      return null;
    }

    if (result.next()) {
      // user not unique
      throw new SQLException("Multiple users found!");
    }

    return user;
  }

  int addRecord(final User user, final Record record) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(
            INSERT_RECORD,
            Statement.RETURN_GENERATED_KEYS);

    statement.setInt(1, user.getId());
    statement.setDate(2, record.getDate());
    statement.setTime(3, record.getTime());
    statement.setBoolean(4, record.getType().getBoolean());

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Adding record ({} {}, {}) for user ID {}",
              record.getDate(),
              record.getTime(),
              record.getType(),
              user.getId());
    }

    if (statement.executeUpdate() != 1) {
      connection.rollback();
      throw new SQLException("Could not store record: " + statement.toString());
    }

    int recordId;
    try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {
      if (generatedKeys.next()) {
        recordId = generatedKeys.getInt(1);
      }
      else {
        connection.rollback();
        throw new SQLException("Storing record failed, no ID obtained");
      }
    }

    connection.commit();
    record.setId(recordId);
    return recordId;
  }

  public Record getRecord(int id) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(SELECT_RECORD);

    statement.setInt(1, id);

    LOGGER.debug("Searching for record with id {}", id);

    final ResultSet result = statement.executeQuery();

    final Record record = new Record();

    if (result.next()) {
      record.setId(result.getInt("pk_record_id"));
      record.setDate(result.getDate("date"));
      record.setTime(result.getTime("time"));
      record.setType(RecordType.getType(result.getBoolean("type")));
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Found record ({} {}, {}) with ID {}",
                record.getDate(),
                record.getTime(),
                record.getType(),
                record.getId());
      }
    } else {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Record with id {} was not found", id);
      }
      return null;
    }

    if (result.next()) {
      // record not unique
      throw new SQLException("Multiple records found!");
    }
    return record;
  }

  List<Record> getRecordsOfDay(final User user, final Date date) throws SQLException {
    final PreparedStatement statement = connection.prepareStatement(SELECT_RECORDS_OF_DAY);

    statement.setInt(1, user.getId());
    statement.setDate(2, date);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Searching for records from {}", date);
    }

    final ResultSet result = statement.executeQuery();
    final List<Record> list = new ArrayList<>();

    if (result.next()) {
      do {
        final Record record = new Record();
        record.setId(result.getInt("pk_record_id"));
        record.setDate(result.getDate("date"));
        record.setTime(result.getTime("time"));
        record.setType(RecordType.getType(result.getBoolean("type")));
        list.add(record);
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Found record ({} {}, {}) with ID {}",
                  record.getDate(),
                  record.getTime(),
                  record.getType(),
                  record.getId());
        }
      } while (result.next());
    } else {
      LOGGER.debug("No records found from {}", date);
    }

    return list;
  }

  Day getDay(final User user, int day, int month, int year) throws SQLException {
    final Day result = new Day(day);
    for (final Record record : getRecordsOfDay(user, day, month, year)) {
      result.addItem(record);
    }
    return result;
  }

  List<Record> getRecordsOfDay(final User user, int day, int month, int year) throws SQLException {
    final List<Record> list = new ArrayList<>();

    final String dateString = String.format("%d-%02d-%02d", year, month, day);

    final Date date;
    try {
      date = new Date(DATE_FORMAT.parse(dateString).getTime());
    } catch (final ParseException pe) {
      LOGGER.error(pe.getMessage());
      return list;
    }
    return getRecordsOfDay(user, date);
  }

  List<Record> getMonth(final User user, int month, int year) throws SQLException {
    final List<Record> list = new ArrayList<>();
    final String startMonth = String.format("%d-%02d-01", year, month);
    final String endMonth =
            String.format("%d-%02d-%02d",
                    year,
                    month,
                    TimeUtils.getLastDayOfMonth(year, month));
    final Date startDate;
    final Date endDate;
    try {
      startDate = new Date(DATE_FORMAT.parse(startMonth).getTime());
      endDate = new Date(DATE_FORMAT.parse(endMonth).getTime());
    } catch (final ParseException pe) {
      LOGGER.error(pe.getMessage());
      return list;
    }

    final PreparedStatement statement = connection.prepareStatement(SELECT_RECORDS_OF_MONTH);

    statement.setInt(1, user.getId());
    statement.setDate(2, startDate);
    statement.setDate(3, endDate);

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Searching for records between {} and {}", startDate, endDate);
    }

    final ResultSet result = statement.executeQuery();

    if (result.next()) {
      do {
        final Record record = new Record();
        record.setId(result.getInt("pk_record_id"));
        record.setDate(result.getDate("date"));
        record.setTime(result.getTime("time"));
        record.setType(RecordType.getType(result.getBoolean("type")));
        list.add(record);
        if (LOGGER.isDebugEnabled()) {
          LOGGER.debug("Found record ({} {}, {}) with ID {}",
                  record.getDate(),
                  record.getTime(),
                  record.getType(),
                  record.getId());
        }
      } while (result.next());
    } else {
      LOGGER.debug("No records found between {} and {}", startDate, endDate);
    }

    return list;
  }

  List<Record> getYear(int year) {
    return null;
  }

  void updateRecord(int owner, int oldId, Record newValues) {

  }

  void deleteRecord(int owner, int id) {

  }

}