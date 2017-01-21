package at.sheldor5.tr.core.records;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic container class to store any type of time information.
 */
public abstract class Container<T extends Comparable<? super T>> implements Comparable<Container> {

  protected final List<T> items = new ArrayList<T>();
  protected final int value;
  protected boolean sorted = false;

  public Container(final int value) {
    this.value = value;
  }

  public final int getValue() {
    return value;
  }

  public final void addItem(final T item) {
    synchronized (items) {
      items.add(item);
      sorted = false;
    }
  }

  public final List<T> getItems() {
    synchronized (items) {
      if (!sorted) {
        Collections.sort(items);
        sorted = true;
      }
    }
    return new ArrayList<T>(items);
  }

  public abstract long getSummary();

  @Override
  public final int compareTo(Container other) {
    return this.value - other.value;
  }
}