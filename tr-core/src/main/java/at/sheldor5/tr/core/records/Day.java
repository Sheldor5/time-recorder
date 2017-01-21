package at.sheldor5.tr.core.records;

import at.sheldor5.tr.core.objects.Record;

import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class Day extends Container<Record> {

  public Day(int value) {
    super(value);
  }

  @Override
  public final long getSummary() {
    long sum = 0;

    List<Record> records = this.getItems();

    if (records.get(0).getType() == RecordType.CHECKOUT) {
      records.add(0, new Record(-1, Record.getStartOfDay(records.get(0).getTimestamp()), RecordType.CHECKIN));
    }

    if (records.get(records.size()-1).getType() == RecordType.CHECKIN) {
      records.add(records.size(), new Record(-1, Record.getEndOfDay(records.get(records.size()-1).getTimestamp()), RecordType.CHECKOUT));
    }

    long start, end;
    for (int i = 0; i < records.size(); i++) {
      start = records.get(i++).getTimestamp().getTime();
      end = records.get(i).getTimestamp().getTime();
      sum += (end - start);
    }

    return sum;
  }
}