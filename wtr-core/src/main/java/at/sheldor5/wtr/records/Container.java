package at.sheldor5.wtr.records;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public abstract class Container<T extends Comparable<? super T>> implements Comparable<Container> {

  private final int value_;
  private final List<T> items_ = new ArrayList<T>();

  public Container(final int value) {
    value_ = value;
  }

  public final int getValue() {
    return value_;
  }

  public final void addItem(final T item) {
    items_.add(item);
  }

  public final List<T> getItems() {
    Collections.sort(items_);
    return items_;
  }

  public abstract long getSummary();

  @Override
  public final int compareTo(Container other) {
    return this.value_ - other.value_;
  }
}