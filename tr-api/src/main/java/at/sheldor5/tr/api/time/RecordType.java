package at.sheldor5.tr.api.time;

/**
 * Record types.
 */
public enum RecordType {

  /**
   * This type indicates that a {@link Record} represents the start of an event,
   * or the start of a {@link Session}.
   */
  CHECKIN(true, "CHECKIN"),

  /**
   * This type indicates that a {@link Record} represents the end of an event,
   * or the end of a {@link Session}.
   */
  CHECKOUT(false, "CHECKOUT");

  private final boolean boolValue;
  private final String stringValue;

  RecordType(boolean boolValue, final String stringValue) {
    this.boolValue = boolValue;
    this.stringValue = stringValue;
  }

  /**
   * Returns the {@code boolean} value representing this type.
   *
   * @return the {@code boolean} value representing this type.
   */
  public boolean getBoolean() {
    return boolValue;
  }

  /**
   * Returns the {@link String} representation of this type.
   *
   * @return the {@link String} representation of this type.
   */
  @Override
  public String toString() {
    return stringValue;
  }

  /**
   * Returns the {@link RecordType} represented by a {@code boolean} value.
   *
   * @param boolValue the boolean value of the type.
   * @return the type for this boolean value.
   */
  public static RecordType getType(boolean boolValue) {
    return boolValue ? CHECKIN : CHECKOUT;
  }

}
