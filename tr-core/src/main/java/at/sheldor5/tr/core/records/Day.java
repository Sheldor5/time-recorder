package at.sheldor5.tr.core.records;

import at.sheldor5.tr.core.objects.Record;
import at.sheldor5.tr.core.utils.TimeUtils;
import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 13.01.2017.
 */
public class Day extends Container<Record> {

  public Day(int value) {
    super(value);
  }

  @Override
  public final synchronized long getSummary() {

    final List<Record> items = this.getItems();

    if (items.get(0).getType() == RecordType.CHECKOUT) {
      items.add(0, new Record(-1, items.get(0).getDate(), TimeUtils.getStartOfDay(), RecordType.CHECKIN));
    }

    if (items.get(items.size()-1).getType() == RecordType.CHECKIN) {
      items.add(items.size(), new Record(-1, items.get(0).getDate(), TimeUtils.getEndOfDay(), RecordType.CHECKOUT));
    }

    if (items.size() % 2 != 0) {
      throw new RuntimeException("uneven count of records");
    }

    long sum = 0;
    long start;
    long end;

    for (int i = 0; i < items.size(); i++) {
      start = items.get(i++).getTime().getTime();
      end = items.get(i).getTime().getTime();
      sum += (end - start);
    }

    return sum;
  }
}