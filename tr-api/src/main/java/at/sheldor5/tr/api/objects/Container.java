package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic container class to store any type of time information.
 */
public abstract class Container<T extends Comparable<? super T>> implements Comparable<Container> {

  protected final List<T> items = new ArrayList<T>();
  protected final LocalDate date;
  protected boolean sorted = false;

  public Container(final LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("Date is null");
    }
    this.date = date;
  }

  public final LocalDate getDate() {
    return date;
  }

  public void addItem(final T item) {
    synchronized (items) {
      if (validateItem(item)) {
        items.add(item);
        sorted = false;
      } else {
        throw new IllegalArgumentException("Invalid item");
      }
    }
  }

  public List<T> getItems() {
    synchronized (items) {
      if (!sorted) {
        Collections.sort(items);
        sorted = true;
      }
    }
    return new ArrayList<T>(items);
  }

  protected abstract boolean validateItem(final T item);

  public abstract long getSummary();

  public abstract long getValuedSummary();

  @Override
  public int compareTo(final Container other) {
    return (int) this.date.until(other.date, ChronoUnit.DAYS);
  }
}