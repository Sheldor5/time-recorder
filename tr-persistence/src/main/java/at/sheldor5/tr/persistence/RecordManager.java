package at.sheldor5.tr.persistence;

import at.sheldor5.tr.api.time.Record;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;

public class RecordManager implements Engine<Record, Integer> {

  @Override
  public Record create(final Record record) {
    EntityManager entityManager = EntityManagerHelper.getEntityManager();
    EntityManagerHelper.beginTransaction();

    Record result = null;

    try {
      entityManager.persist(record);
      EntityManagerHelper.commit();
      result = record;
    } catch (Exception e) {
      e.printStackTrace();
      EntityManagerHelper.rollback();
    }

    EntityManagerHelper.closeEntityManager();

    return result;
  }

  @Override
  public Record read(final Integer identifier) {
    EntityManager entityManager = EntityManagerHelper.getEntityManager();
    EntityManagerHelper.beginTransaction();

    Record record = null;

    try {
      record = entityManager.find(Record.class, identifier);
      EntityManagerHelper.commit();
    } catch (final Exception e) {
      EntityManagerHelper.rollback();
      e.printStackTrace();
    }

    EntityManagerHelper.closeEntityManager();

    return record;
  }

  @Override
  public void update(final Record record) {
    EntityManager entityManager = EntityManagerHelper.getEntityManager();
    EntityManagerHelper.beginTransaction();

    try {
      entityManager.merge(record);
      EntityManagerHelper.commit();
    } catch (final Exception e) {
      EntityManagerHelper.rollback();
      e.printStackTrace();
    }

    EntityManagerHelper.closeEntityManager();
  }

  @Override
  public void delete(Integer integer) {

  }

  public List<Record> read(int userId, final LocalDate fromInclusive, final LocalDate toInclusive) {
    return null;
  }
}
