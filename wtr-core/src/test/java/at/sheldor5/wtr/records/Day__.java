package at.sheldor5.wtr.records;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public final class Day__ implements Comparable<Day__> {

  private final int day_;
  private final List<Record> records_ = new ArrayList<Record>();

  public Day__(final int day) {
    day_ = day;
  }

  public int getDay() {
    return day_;
  }

  public void addRecord(final Record record) {
    records_.add(record);
  }

  public List<Record> getRecords() {
    Collections.sort(records_);
    return records_;
  }

  public int compareTo(Day__ other) {
    return this.day_ - other.day_;
  }
}