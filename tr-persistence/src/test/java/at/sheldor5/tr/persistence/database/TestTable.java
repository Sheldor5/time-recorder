package at.sheldor5.tr.persistence.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TestTable extends Table {

  private static final String NAME = "TEST";
  private static final Map<String, Integer> COLUMNS = new HashMap<>();

  static {
    COLUMNS.put("PK_USER_ID", Types.INTEGER);
    COLUMNS.put("UUID", Types.BINARY);
  }

  public TestTable(final Connection connection) throws SQLException {
    super(connection, NAME, COLUMNS);
  }

}
