package at.sheldor5.tr.api.time;

import java.time.LocalDate;

public class Year extends Container<Month> {

  public Year(final LocalDate date) {
    super(date);
  }

  @Override
  protected boolean validateItem(final Month item) {
    return this.date.getYear() == item.date.getYear();
  }

  @Override
  public long getSummary() {
    long sum = 0;
    for (final Month month : items) {
      sum += month.getSummary();
    }
    return sum;
  }

  @Override
  public long getValuedSummary() {
    long sum = 0;
    for (final Month month : items) {
      sum += month.getValuedSummary();
    }
    return sum;
  }
}