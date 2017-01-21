package at.sheldor5.tr.core.records;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class Month extends Container<Day> {

  public Month(int value) {
    super(value);
  }

  @Override
  public long getSummary() {
    long sum = 0;
    for (final Day day : this.getItems()) {
      sum += day.getSummary();
    }
    return sum;
  }
}