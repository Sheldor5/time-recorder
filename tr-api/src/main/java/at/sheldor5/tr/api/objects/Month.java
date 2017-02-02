package at.sheldor5.tr.api.objects;

import java.time.LocalDate;

public class Month extends Container<Day> {

  public Month(final LocalDate date) {
    super(date);
  }

  @Override
  protected boolean validateItem(final Day item) {
    return this.date.getYear() == item.date.getYear() && this.date.getMonth() == item.date.getMonth();
  }

  @Override
  public long getSummary() {
    long sum = 0;
    for (final Day day : this.getItems()) {
      sum += day.getSummary();
    }
    return sum;
  }

  @Override
  public long getValuedSummary() {
    long sum = 0;
    for (final Day day : this.getItems()) {
      sum += day.getValuedSummary();
    }
    return sum;
  }
}