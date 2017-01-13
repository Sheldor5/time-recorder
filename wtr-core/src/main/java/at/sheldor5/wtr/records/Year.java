package at.sheldor5.wtr.records;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class Year extends Container<Month> {
  public Year(int value) {
    super(value);
  }

  @Override
  public long getSummary() {
    long sum = 0;
    for (final Month month : this.getItems()) {
      sum += month.getSummary();
    }
    return sum;
  }
}