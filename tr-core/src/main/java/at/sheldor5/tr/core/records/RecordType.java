package at.sheldor5.tr.core.records;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public enum RecordType {
  CHECKIN(1),
  CHECKOUT(0);

  private final boolean bit;

  RecordType(int i) {
    bit = (i == 1);
  }

  public boolean getBoolean() {
    return bit;
  }

  public static RecordType getType(boolean bit) {
    if (bit) {
      return CHECKIN;
    }
    return CHECKOUT;
  }
}