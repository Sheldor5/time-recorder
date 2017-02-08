package at.sheldor5.tr.api;

import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Month;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.objects.Year;

import java.util.List;

public interface RecordEngine {

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