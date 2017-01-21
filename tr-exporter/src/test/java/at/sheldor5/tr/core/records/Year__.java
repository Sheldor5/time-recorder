package at.sheldor5.tr.core.records;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public final class Year__ implements Comparable<Year__> {

  private final int year_;
  private final List<Month__> months_ = new ArrayList<Month__>();

  public Year__(int year) {
    year_ = year;
  }

  public int getYear() {
    return year_;
  }

  public void addMonth(final Month__ month) {
    months_.add(month);
  }

  public List<Month__> getMonths() {
    Collections.sort(months_);
    return months_;
  }

  public int compareTo(Year__ other) {
    return this.year_ - other.year_;
  }
}