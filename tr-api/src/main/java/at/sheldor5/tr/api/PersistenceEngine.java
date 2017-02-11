package at.sheldor5.tr.api;

import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Month;
import at.sheldor5.tr.api.time.Record;
import at.sheldor5.tr.api.user.User;
import at.sheldor5.tr.api.time.Year;
import java.util.List;
import java.util.UUID;

public interface PersistenceEngine {

  void addRecord(User user, Record record);

  void updateRecord(User user, int id, Record newValues);

  Record getRecord(User user, int id);

  void deleteRecord(User user, int id);

  Day getDay(User user, int year, int month, int day);

  Month getMonth(User user, int year, int month);

  Year getYear(User user, int year);

  List<Record> getDayRecords(User user, int year, int month, int day);

  List<Record> getMonthRecords(User user, int year, int month);

  List<Record> getYearRecords(User user, int year);

}