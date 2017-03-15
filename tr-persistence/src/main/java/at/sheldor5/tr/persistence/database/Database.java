package at.sheldor5.tr.persistence.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Database {

  private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

  private final Connection connection;
  private DatabaseMetaData meta;

  public Database(final Connection connection) {
    this.connection = connection;
  }

  public boolean initialize() {
    return checkSystemTable() && checkUserMappingsTable() && checkRecordsTable();
  }

  protected boolean checkSystemTable() {
    if (!tableExists("time-recorder") || !tableExists("records") || !tableExists("user_mapping")) {
      executeScript("/sqlserver/create_system_tables.sql");
    }
    return false;
  }

  protected boolean checkRecordsTable() {
    return false;
  }

  protected boolean checkUserMappingsTable() {
    return false;
  }

  protected String getVendor() {
    try {
      if (meta == null) {
        meta = connection.getMetaData();
      }
      return meta.getDatabaseProductName();
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
      return null;
    }
  }

  protected boolean tableExists(final String tableName) {
    try {
      if (meta == null) {
        meta = connection.getMetaData();
      }
      try (final ResultSet set = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
        return set.next();
      }
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
    }
    return false;
  }

  public void executeScript(final String script) {
    try (final InputStream is = Database.class.getResourceAsStream(script)) {
      try (final BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
        final StringBuilder stringBuilder = new StringBuilder();
        String line, command;
        while (true) {
          line = reader.readLine();
          if (line == null || line.isEmpty()) {
            command = stringBuilder.toString();
            if (!command.isEmpty()) {
              executeCommand(command);
            }
            stringBuilder.setLength(0);
            if (line == null) {
              break;
            }
          } else if (line.startsWith("--")) {
            continue;
          } else {
            stringBuilder.append(line);
            stringBuilder.append('\n');
          }
        }
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  private void executeCommand(final String command) {
    if (command == null || command.isEmpty()) {
      return;
    }
    try (final Statement statement = connection.createStatement()) {
      statement.executeUpdate(command);
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
    }
  }

}
