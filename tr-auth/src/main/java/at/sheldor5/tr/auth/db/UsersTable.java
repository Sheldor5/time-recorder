package at.sheldor5.tr.auth.db;

import at.sheldor5.tr.persistence.database.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class UsersTable extends Table {

  private static final String NAME = "USERS";
  private static final Map<String, Integer> COLUMNS = new HashMap<>();

  static {
    COLUMNS.put("PK_USER_ID", Types.INTEGER);
    COLUMNS.put("UUID", Types.VARBINARY);
    COLUMNS.put("USERNAME", Types.VARCHAR);
    COLUMNS.put("PASSWORD", Types.CHAR);
    COLUMNS.put("FORENAME", Types.VARCHAR);
    COLUMNS.put("SURNAME", Types.VARCHAR);
  }

  public UsersTable(final Connection connection) throws SQLException {
    super(connection, NAME, COLUMNS);
  }

}
