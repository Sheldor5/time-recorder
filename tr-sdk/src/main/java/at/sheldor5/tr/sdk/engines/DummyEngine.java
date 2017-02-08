package at.sheldor5.tr.sdk.engines;

import at.sheldor5.tr.api.RecordEngine;
import at.sheldor5.tr.api.objects.Record;
import at.sheldor5.tr.api.objects.Session;
import at.sheldor5.tr.api.objects.User;
import at.sheldor5.tr.api.objects.Day;
import at.sheldor5.tr.api.objects.Month;
import at.sheldor5.tr.api.objects.Year;
import at.sheldor5.tr.api.utils.StringUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DummyEngine implements RecordEngine {

  private final Map<User, List<Record>> records = new HashMap<>();

  @Override
  public void addRecord(final User user, final Record record) {
    List<Record> list = records.get(user);
    if (list == null) {
      list = new ArrayList<>();
      records.put(user, list);
    }
    list.add(record);
    if (list.size() == 0) {
      record.setId(1);
    } else {
      record.setId(list.get(list.size() - 1).getId() + 1);
    }
  }

  @Override
  public void updateRecord(final User user, int id, final Record newValues) {
    final List<Record> list = records.get(user);
    if (list != null) {
      for (final Record record : list) {
        if (record.getId() == id) {
          record.setDate(newValues.getDate());
          record.setTime(newValues.getTime());
          record.setType(newValues.getType());
          break;
        }
      }
    }
  }

  @Override
  public Record getRecord(User user, int id) {
    final List<Record> list = records.get(user);
    if (list != null) {
      for (final Record record : list) {
        if (record.getId() == id) {
          return record;
        }
      }
    }
    return null;
  }

  @Override
  public void deleteRecord(User user, int id) {
    final List<Record> list = records.get(user);
    if (list != null) {
      final Iterator<Record> iterator = list.iterator();
      Record record;
      while (iterator.hasNext()) {
        record = iterator.next();
        if (record.getId() == id) {
          iterator.remove();
          break;
        }
      }
    }
  }

  @Override
  public Day getDay(User user, int year, int month, int day) {
    final List<Record> records = getDayRecords(user, year, month, day);

    if (records == null) {
      return null;
    }

    final List<Session> sessions = Session.buildSessions(records);
    final Day result = new Day(LocalDate.of(year, month, day));
    for (final Session session : sessions) {
      result.addItem(session);
    }

    return result;
  }

  @Override
  public Month getMonth(User user, int year, int month) {
    return null;
  }

  @Override
  public Year getYear(User user, int year) {
    return null;
  }

  @Override
  public List<Record> getDayRecords(User user, int year, int month, int day) {
    final LocalDate date = LocalDate.of(year, month, day);
    final List<Record> records = this.records.get(user);

    if (records == null) {
      return null;
    }

    final List<Record> result = new ArrayList<>();
    for (final Record record : records) {
      if (date.equals(record.getDate())) {
        result.add(record);
      }
    }

    return result;
  }

  @Override
  public List<Record> getMonthRecords(User user, int year, int month) {
    return null;
  }

  @Override
  public List<Record> getYearRecords(User user, int year) {
    return null;
  }

  public void reset() {
    for (final Map.Entry<User, List<Record>> entry : records.entrySet()) {
      entry.getValue().clear();
    }
    records.clear();
  }

  public void generateTestData() {
    final String user_prefix = "user_";
    for (int i = 0; i < 10; i++) {
      final String username = user_prefix + i;
      final User user = new User();
      user.setUsername(username);
      final String hash = StringUtils.getMD5(username + username);
      // TODO
    }
  }
}