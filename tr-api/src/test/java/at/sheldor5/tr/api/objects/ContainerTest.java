package at.sheldor5.tr.api.objects;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class ContainerTest {

  class TestItem implements Comparable<TestItem> {
    int value;
    double multiplier;
    TestItem(int value, double multiplier) {
      this.value = value;
      this.multiplier = multiplier;
    }
    @Override
    public int compareTo(TestItem other) {
      return this.value - other.value;
    }
  }

  class TestContainer extends Container<TestItem> {
    TestContainer(final LocalDate date) {
      super(date);
    }

    @Override
    protected boolean validateItem(final TestItem item) {
      return true;
    }

    @Override
    public long getSummary() {
      int sum = 0;
      for (final TestItem item : getItems()) {
        sum += item.value;
      }
      return sum;
    }
    @Override
    public long getValuedSummary() {
      int sum = 0;
      for (final TestItem item : getItems()) {
        sum += item.value * item.multiplier;
      }
      return sum;
    }
  }

  private static final LocalDate date = LocalDate.of(2017, 1, 1);

  @Test(expected = IllegalArgumentException.class)
  public void test_container_initialization() {
    Container<TestItem> container = new TestContainer(null);
  }

  @Test
  public void test_container() {
    Container<TestItem> container = new TestContainer(date);
    container.addItem(new TestItem(1, 1.0));
    container.addItem(new TestItem(2, 1.5));
    container.addItem(new TestItem(3, 2.0));
    Assert.assertEquals("Summary should be 6", 6, container.getSummary());
    Assert.assertEquals("Valued summary should be 10", 10, container.getValuedSummary());
  }
}