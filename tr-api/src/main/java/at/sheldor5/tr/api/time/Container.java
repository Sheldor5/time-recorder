package at.sheldor5.tr.api.time;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generic container class to store any type of time information.
 */
abstract class Container<T extends Comparable<? super T>> implements Comparable<Container> {

  protected final List<T> items = new ArrayList<T>();
  protected final LocalDate date;
  protected boolean sorted = false;

  /**
   * Minimal constructor.
   *
   * @param date The date the items of this container belong to.
   */
  Container(final LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("Date is null");
    }
    this.date = date;
  }

  /**
   * Getter for date.
   *
   * @return This container's date.
   */
  public final LocalDate getDate() {
    return date;
  }

  /**
   * Add an item to this container.
   *
   * @param item The item to add.
   */
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

  /**
   * Get all items of this container.
   *
   * @return List of items.
   */
  public List<T> getItems() {
    synchronized (items) {
      if (!sorted) {
        Collections.sort(items);
        sorted = true;
      }
    }
    return new ArrayList<T>(items);
  }

  /**
   * Before an item is added to this container
   * it must be validated.
   *
   * @param item The item to add.
   * @return True if the item is valid, false otherwise.
   */
  protected abstract boolean validateItem(final T item);

  /**
   * Get a summary of this container.
   * Mostly the sum of all items.
   *
   * @return Summary of this container.
   */
  public abstract long getSummary();

  /**
   * Get a valued summary of this container.
   * Mostly the sum of all items multiplied by the multiplier.
   *
   * @return Valued summary of this container.
   */
  public abstract long getValuedSummary();

  /**
   * Compare this container to another container.
   * Used to sort containers based on their date.
   *
   * @param other The container to compare.
   * @return {@link LocalDate#compareTo(ChronoLocalDate)}
   */
  @Override
  public int compareTo(final Container other) {
    return this.date.compareTo(other.date);
  }

}
