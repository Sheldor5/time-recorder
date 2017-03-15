package at.sheldor5.tr.persistence.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class UserMappingTable extends Table {

  private static final String NAME = "records";
  private static final Map<String, Integer> COLUMNS = new HashMap<>();

  static {
    COLUMNS.put("pk_user_id", Types.INTEGER);
    COLUMNS.put("uuid", Types.BINARY);
  }

  public UserMappingTable(final Connection connection) throws SQLException {
    super(connection, NAME, COLUMNS);
  }
}
