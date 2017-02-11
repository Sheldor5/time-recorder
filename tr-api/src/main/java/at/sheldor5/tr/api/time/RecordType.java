package at.sheldor5.tr.api.time;

public enum RecordType {
  CHECKIN(1, true, "CHECKIN"),
  CHECKOUT(0, false, "CHECKOUT");

  private final int intValue;
  private final boolean boolValue;
  private final String stringValue;

  RecordType(int intValue, boolean boolValue, final String stringValue) {
    this.intValue = intValue;
    this.boolValue = boolValue;
    this.stringValue = stringValue;
  }

  public boolean getBoolean() {
    return boolValue;
  }

  public int getInteger() {
    return intValue;
  }

  @Override
  public String toString() {
    return stringValue;
  }

  /**
   * Record type factory for boolean values.
   *
   * @param boolValue The boolean value of the type.
   * @return The type for this boolean value.
   */
  public static RecordType getType(boolean boolValue) {
    return boolValue ? CHECKIN : CHECKOUT;
  }


  /**
   * Record type factory for int values.
   *
   * @param intValue The int value of the type.
   * @return The type for this int value.
   */
  public static RecordType getType(int intValue) {
    if (intValue == 1) {
      return CHECKIN;
    } else if (intValue == 0) {
      return CHECKOUT;
    }
    return null;
  }

}