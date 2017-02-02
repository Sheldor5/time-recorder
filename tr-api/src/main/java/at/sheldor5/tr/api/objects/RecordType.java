package at.sheldor5.tr.api.objects;

public enum RecordType {
  CHECKIN(1, true, "CHECKIN"),
  CHECKOUT(0, false, "CHECKOUT");

  private final int i;
  private final boolean b;
  private final String s;

  RecordType(int i, boolean b, final String s) {
    this.i = i;
    this.b = b;
    this.s = s;
  }

  public boolean getBoolean() {
    return b;
  }

  public int getInteger() {
    return i;
  }

  @Override
  public String toString() {
    return s;
  }

  public static RecordType getType(boolean b) {
    return b ? CHECKIN : CHECKOUT;
  }

  public static RecordType getType(int i) {
    if (i == 1) {
      return CHECKIN;
    } else if (i == 0) {
      return CHECKOUT;
    }
    return null;
  }
}