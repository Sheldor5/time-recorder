package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.time.Record;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class RecordEngine implements Engine<Record, Integer> {

  @Override
  public Record create(final Record record) {
    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    try {
      session.save(record);
      session.flush();
      tx.commit();
    } catch (final Exception e) {
      tx.rollback();
      throw e;
    }

    return record;
  }

  @Override
  public Record read(final Integer identifier) {
    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();
    final Record record = session.get(Record.class, identifier);
    session.flush();
    tx.commit();
    return record;
  }

  @Override
  public void update(final Record record) {
    Session session = DatabaseManager.getSession();
    Transaction tx = session.beginTransaction();

    try {
      session.save(record);
      session.flush();
      tx.commit();
    } catch (final Exception e) {
      tx.rollback();
      throw e;
    }
  }

  @Override
  public void delete(Integer integer) {

  }

  public List<Record> read(int userId, final LocalDate fromInclusive, final LocalDate toInclusive) {
    return null;
  }
}
