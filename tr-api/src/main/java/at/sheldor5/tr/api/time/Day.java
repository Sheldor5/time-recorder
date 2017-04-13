package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.util.List;

public class Day extends Container<Session> {

  public Day(final LocalDate date) {
    super(date);
  }

  @Override
  protected boolean validateItem(final Session item) {
    return this.date.equals(item.date);
  }

  @Override
  public final synchronized long getSummary() {

    final List<Session> sessions = this.getItems();

    long sum = 0;

    for (final Session session : sessions) {
      sum += session.getSummary();
    }

    return sum;
  }

  @Override
  public final synchronized long getValuedSummary() {

    final List<Session> sessions = this.getItems();

    long sum = 0;

    for (final Session session : sessions) {
      sum += session.getValuedSummary();
    }

    return sum;
  }

}