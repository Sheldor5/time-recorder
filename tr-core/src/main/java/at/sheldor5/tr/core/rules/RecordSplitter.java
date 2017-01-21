package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.core.objects.Record;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class RecordSplitter {

  private final Record before;
  private final Record after;

  private RecordSplitter(final Record before, final Record after) {
    this.before = before;
    this.after = after;
  }

  public Record getBefore() {
    return before;
  }

  public Record getAfter() {
    return after;
  }

  public static void split() {

  }
}