package at.sheldor5.tr.persistence.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class RecordsTable extends Table {

  private static final String NAME = "records";
  private static final Map<String, Integer> COLUMNS = new HashMap<>();

  static {
    COLUMNS.put("pk_record_id", Types.INTEGER);
    COLUMNS.put("fk_user_id", Types.INTEGER);
    COLUMNS.put("date", Types.DATE);
    COLUMNS.put("time", Types.TIME);
    COLUMNS.put("type", Types.BIT);
  }

  public RecordsTable(final Connection connection) throws SQLException {
    super(connection, NAME, COLUMNS);
  }
}
