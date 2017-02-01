package at.sheldor5.tr.api.objects;

import java.util.List;

public class Day extends Container<Session> {

  public Day(int value) {
    super(value);
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