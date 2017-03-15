package at.sheldor5.tr.persistence.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class SystemTable extends Table {

  private static final String NAME = "time-recorder";
  private static final Map<String, Integer> COLUMNS = new HashMap<>();

  static {
    COLUMNS.put("pk_update_id", Types.INTEGER);
    COLUMNS.put("timestamp", Types.TIMESTAMP);
    COLUMNS.put("version_major", Types.INTEGER);
    COLUMNS.put("version_minor", Types.INTEGER);
    COLUMNS.put("version_patch", Types.INTEGER);
  }

  public SystemTable(final Connection connection) throws SQLException {
    super(connection, NAME, COLUMNS);
  }

}
