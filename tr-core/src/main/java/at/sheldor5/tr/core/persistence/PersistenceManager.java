package at.sheldor5.tr.core.persistence;

import at.sheldor5.tr.api.RecordEngine;
import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Month;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.objects.Year;
import at.sheldor5.tr.api.persistence.DatabaseConnection;
import java.util.List;

public class PersistenceManager implements RecordEngine {

  private static PersistenceManager instance;

  public static PersistenceManager getInstance() {
    if (instance == null) {
      instance = new PersistenceManager();
    }
    return instance;
  }

  private RecordEngine engine;

  private PersistenceManager() {
    engine = new DatabaseEngine(DatabaseConnection.getInstance().getConnection());
  }

  @Override
  public void addRecord(final User user, final Record record) {
    engine.addRecord(user, record);
  }

  @Override
  public void updateRecord(final User user, int id, final Record newValues) {
    engine.updateRecord(user, id, newValues);
  }

  @Override
  public Record getRecord(final User user, int id) {
    return engine.getRecord(user, id);
  }

  @Override
  public void deleteRecord(final User user, int id) {
    engine.deleteRecord(user, id);
  }

  @Override
  public Day getDay(final User user, int year, int month, int day) {
    final Day result = engine.getDay(user, year, month, day);

    return engine.getDay(user, year, month, day);
  }

  @Override
  public Month getMonth(final User user, int year, int month) {
    return engine.getMonth(user, year, month);
  }

  @Override
  public Year getYear(final User user, int year) {
    return engine.getYear(user, year);
  }

  @Override
  public List<Record> getDayRecords(final User user, int year, int month, int day) {
    return engine.getDayRecords(user, year, month, day);
  }

  @Override
  public List<Record> getMonthRecords(final User user, int year, int month) {
    return engine.getMonthRecords(user, year, month);
  }

  @Override
  public List<Record> getYearRecords(final User user, int year) {
    return engine.getYearRecords(user, year);
  }
}