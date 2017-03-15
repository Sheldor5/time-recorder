package at.sheldor5.tr.persistence.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Table {

  private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

  private static final String SQL_COLUMNS = "SELECT * FROM %s WHERE 1 = 0";

  private final String name;
  private final Connection connection;
  private final Map<String, Integer> columns;

  public Table(final Connection connection, final String name, final Map<String, Integer> columns) throws SQLException {
    this.connection = connection;
    this.name = name;
    this.columns = columns;
  }

  public final boolean check() {
    final Map<String, Boolean> columnExists = new HashMap<>();
    for (final Map.Entry<String, Integer> entry : columns.entrySet()) {
      columnExists.put(entry.getKey(), false);
    }

    try (final Statement statement = connection.createStatement()) {
      try (final ResultSet resultSet = statement.executeQuery(String.format(SQL_COLUMNS, name))) {
        final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        String columnName;
        int columnType;
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
          columnName = resultSetMetaData.getColumnName(i);
          columnType = resultSetMetaData.getColumnType(i);
          if (columns.get(columnName) != null && columns.get(columnName) == columnType) {
            columnExists.put(columnName, true);
            LOGGER.fine(String.format("Column %s with type %d exists", columnName, columnType));
          } else {
            LOGGER.fine(String.format("Unexpected column (%s) or type (%d, %s)", columnName, columnType, resultSetMetaData.getColumnTypeName(i)));
          }
        }
      }
    } catch (final SQLException sqle) {
      LOGGER.severe(sqle.getMessage());
    }

    for (final Map.Entry<String, Boolean> entry : columnExists.entrySet()) {
      if (!entry.getValue()) {
        LOGGER.fine(String.format("Missing column: %s", entry.getKey()));
        return false;
      }
    }

    return true;
  }

}
