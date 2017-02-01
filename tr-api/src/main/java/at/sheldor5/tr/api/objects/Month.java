package at.sheldor5.tr.api.objects;

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

  @Override
  public long getValuedSummary() {
    long sum = 0;
    for (final Day day : this.getItems()) {
      sum += day.getValuedSummary();
    }
    return sum;
  }
}