package at.sheldor5.wtr.records;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public final class Month__ implements Comparable<Month__> {

  private final int month_;
  private final List<Day__> days_ = new ArrayList<Day__>();

  public Month__(int month) {
    month_ = month;
  }

  public int getMonth() {
    return month_;
  }

  public void addDay(final Day__ day) {
    days_.add(day);
  }

  public List<Day__> getDays() {
    Collections.sort(days_);
    return days_;
  }

  public int compareTo(Month__ other) {
    return this.month_ - other.month_;
  }
}