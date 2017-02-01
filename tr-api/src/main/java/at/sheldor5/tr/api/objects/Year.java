package at.sheldor5.tr.api.objects;

public class Year extends Container<Month> {
  public Year(int value) {
    super(value);
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