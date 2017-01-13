package at.sheldor5.wtr.records;

import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public interface RecordsProvider {

  List<Record> getDayRecords(int day, int month, int year);

  List<Record> getMonthRecord(int month, int year);

  List<Record> getYear(int year);

}