package at.sheldor5.tr.core.persistence;

import at.sheldor5.tr.api.PersistenceEngine;
import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.time.RecordType;
import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.time.Year;
import at.sheldor5.tr.api.utils.UuidUtils;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseEngine implements PersistenceEngine {

  private static final Logger LOGGER = Logger.getLogger(DatabaseEngine.class.getName());

  private static final String INSERT_RECORD = "INSERT INTO [records] ([fk_user_id], [date], [time], [type]) VALUES (?, ?, ?, ?)";
  private static final String SELECT_RECORD = "SELECT * FROM [records] WHERE [pk_record_id] = ? AND [fk_user_id] = ?";

  private static final String SELECT_RECORDS_OF_DAY = "SELECT * FROM [records] WHERE [fk_user_id] = ? AND [date] = ?";
  private static final String SELECT_RECORDS_OF_MONTH = "SELECT * FROM [records] WHERE [fk_user_id] = ? AND [date] >= ? AND [date] <= ?";
  private static final String SELECT_RECORDS_OF_YEAR = "SELECT * FROM [records] WHERE [fk_user_id] = ? AND [date] >= ? AND [date] <= ?";

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  private static DatabaseEngine instance;

  public static DatabaseEngine getInstance() {
    if (instance == null) {
      instance = new DatabaseEngine();
    }
    return instance;
  }

  private static final Map<DataSource, DatabaseEngine> instances = new HashMap<>();

  public static DatabaseEngine getInstance(final DataSource dataSource) {
    DatabaseEngine engine = instances.get(dataSource);
    if (engine == null) {
      engine = instances.put(dataSource, new DatabaseEngine(dataSource));
    }
    return engine;
  }

  private DataSource dataSource;

  public DatabaseEngine() {

  }

  public DatabaseEngine(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void addRecord(final User user, final Record record) {
    if (user == null) {
      LOGGER.warning("User is null");
      return;
    }
    if (record == null) {
      LOGGER.warning("Record is null");
      return;
    }

    try (final Connection connection = dataSource.getConnection()) {

      int id = 0;

      try (final PreparedStatement statement = connection.prepareStatement(INSERT_RECORD, Statement.RETURN_GENERATED_KEYS)) {

        statement.setInt(1, user.getId());
        statement.setDate(2, Date.valueOf(record.getDate()));
        statement.setTime(3, Time.valueOf(record.getTime()));
        statement.setBoolean(4, record.getType().getBoolean());

        if (statement.executeUpdate() != 0) {

          try (final ResultSet generatedKeys = statement.getGeneratedKeys()) {

            if (generatedKeys.next()) {
              id = generatedKeys.getInt(1);
            } else {
              LOGGER.severe("Could not store record, no ID obtained");
            }

          } catch (final SQLException sqle) {
            LOGGER.severe("Result Error: " + sqle.getMessage());
          }

        } else {
          LOGGER.severe("Could not store record, insert failed");
        }

      } catch (final SQLException sqle) {
        LOGGER.severe("Statement Execution Error: " + sqle.getMessage());
      }

      if (id > 0) {
        if (!connection.getAutoCommit()) {
          connection.commit();
        }
        record.setId(id);
        if (LOGGER.isLoggable(Level.FINE)) {
          LOGGER.fine(String.format("Successfully stored record (%s %s, %s) for user ID %s",
                  record.getDate(),
                  record.getTime(),
                  record.getType(),
                  user.getId()));
        }
      } else if (!connection.getAutoCommit()) {
        connection.rollback();
      }

    } catch (final SQLException sqle) {
      LOGGER.severe("Connection Error: " + sqle.getMessage());
    }
  }

  @Override
  public void updateRecord(User user, int id, Record newValues) {

  }

  @Override
  public void deleteRecord(User user, int id) {

  }

  @Override
  public Record getRecord(final User user, int id) {
    if (user == null) {
      LOGGER.warning("User is null");
      return null;
    }

    try (final Connection connection = dataSource.getConnection()) {

      try (final PreparedStatement statement = connection.prepareStatement(SELECT_RECORD)) {
        statement.setInt(1, id);
        statement.setInt(2, user.getId());

        try (final ResultSet result = statement.executeQuery()) {

          if (result.next()) {
            final Record record = new Record();
            record.setId(result.getInt("pk_record_id"));
            record.setDate(result.getDate("date").toLocalDate());
            record.setTime(result.getTime("time").toLocalTime());
            record.setType(RecordType.getType(result.getBoolean("type")));

            if (result.next()) {
              // record not unique
              LOGGER.severe("Multiple records found with ID " + record.getId());
              return null;
            } else if (LOGGER.isLoggable(Level.FINE)) {
              LOGGER.fine(String.format("Found record (%s %s, %s) with ID %s",
                      record.getDate(),
                      record.getTime(),
                      record.getType(),
                      record.getId()));
            }

            return record;

          }

          LOGGER.fine("No record with id " + id + " found");

        } catch (final SQLException sqle) {
          LOGGER.severe("Result Error: " + sqle.getMessage());
        }

      } catch (final SQLException sqle) {
        LOGGER.severe("Statement Execution Error: " + sqle.getMessage());
      }

    } catch (final SQLException sqle) {
      LOGGER.severe("Connection Error: " + sqle.getMessage());
    }

    return null;
  }

  @Override
  public List<Record> getDayRecords(final User user, int yyyy, int mm, int dd) {
    final LocalDate date = LocalDate.of(yyyy, mm, dd);

    try (final Connection connection = dataSource.getConnection()) {

      try (final PreparedStatement statement = connection.prepareStatement(SELECT_RECORDS_OF_DAY)) {

        statement.setInt(1, user.getId());
        statement.setDate(2, Date.valueOf(LocalDate.of(yyyy, mm, dd)));

        try (final ResultSet result = statement.executeQuery()) {

          if (result.next()) {
            LOGGER.fine("Found records from " + date);
            final List<Record> list = new ArrayList<>();
            Record record;
            do {
              record = new Record();
              record.setId(result.getInt("pk_record_id"));
              record.setDate(result.getDate("date").toLocalDate());
              record.setTime(result.getTime("time").toLocalTime());
              record.setType(RecordType.getType(result.getBoolean("type")));
              list.add(record);

              if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(String.format("Found record (%s %s, %s) with ID %s",
                        record.getDate(),
                        record.getTime(),
                        record.getType(),
                        record.getId()));
              }
            } while (result.next());
            return list;
          } else {
            LOGGER.fine("No records found from " + date);
          }

        } catch (final SQLException sqle) {
          LOGGER.severe("Result Error: " + sqle.getMessage());
        }

      } catch (final SQLException sqle) {
        LOGGER.severe("Statement Execution Error: " + sqle.getMessage());
      }

    } catch (final SQLException sqle) {
      LOGGER.severe("Connection Error: " + sqle.getMessage());
    }

    return null;
  }

  @Override
  public Day getDay(final User user, int day, int month, int year) {
    final List<Record> records = getDayRecords(user, day, month, year);
    final List<Session> sessions = Session.buildSessions(records);
    return Day.buildDay(sessions);
  }

  @Override
  public Month getMonth(User user, int year, int month) {
    return null;
  }

  @Override
  public List<Record> getMonthRecords(final User user, int month, int year) {
    final LocalDate tmp = LocalDate.of(year, month, 1);
    Date startDate;
    Date endDate;

    try {
      startDate = Date.valueOf(tmp);
      endDate = Date.valueOf(LocalDate.of(year, month, tmp.lengthOfMonth()));
    } catch (final DateTimeException pe) {
      LOGGER.severe(pe.getMessage());
      return null;
    }

    ;
    try (final Connection connection = dataSource.getConnection()) {

      try (final PreparedStatement statement = connection.prepareStatement(SELECT_RECORDS_OF_MONTH)) {

        statement.setInt(1, user.getId());
        statement.setDate(2, startDate);
        statement.setDate(3, endDate);

        try (final ResultSet result = statement.executeQuery()) {

          if (result.next()) {
            LOGGER.fine("Found records between " + startDate + " and " + endDate);
            final List<Record> list = new ArrayList<>();
            Record record;
            do {
              record = new Record();
              record.setId(result.getInt("pk_record_id"));
              record.setDate(result.getDate("date").toLocalDate());
              record.setTime(result.getTime("time").toLocalTime());
              record.setType(RecordType.getType(result.getBoolean("type")));
              list.add(record);

              if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(String.format("Found record (%s %s, %s) with ID %s",
                        record.getDate(),
                        record.getTime(),
                        record.getType(),
                        record.getId()));
              }

            } while (result.next());
            return list;
          } else {
            LOGGER.fine("No records found between " + startDate + " and " + endDate);
          }

        } catch (final SQLException sqle) {
          LOGGER.severe("Result Error: " + sqle.getMessage());
        }

      } catch (final SQLException sqle) {
        LOGGER.severe("Statement Execution Error: " + sqle.getMessage());
      }

    } catch (final SQLException sqle) {
      LOGGER.severe("Connection Error: " + sqle.getMessage());
    }

    return null;
  }

  @Override
  public Year getYear(User user, int year) {
    return null;
  }

  @Override
  public List<Record> getYearRecords(User user, int year) {
    return null;
  }
}