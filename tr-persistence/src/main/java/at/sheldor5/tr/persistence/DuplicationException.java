package at.sheldor5.tr.persistence;

public class DuplicationException extends RuntimeException {

  private static final String MESSAGE_FORMAT = "Duplicated object %s found";

  private final Object duplicate;
  private final String message;

  public DuplicationException(Object duplicate) {
    this.duplicate = duplicate;
    message = String.format(MESSAGE_FORMAT, duplicate);
  }

  public String getMessage() {
    return message;
  }

  public Object getDuplicate() {
    return duplicate;
  }
}
