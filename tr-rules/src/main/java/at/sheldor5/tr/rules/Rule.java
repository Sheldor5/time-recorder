package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Session;
import at.sheldor5.tr.api.time.Day;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Rule {

  protected String name;
  protected LocalDate keyDate;
  protected String description;
  protected List<TimeOperation> timeOperations = new ArrayList<>();
  protected List<EffortOperation> effortOperations = new ArrayList<>();

  protected Rule() {

  }

  public Rule(final String name, final LocalDate keyDate) {
    this.name = name;
    this.keyDate = keyDate;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean applies(final Day day) {
    if (day == null) {
      return false;
    }
    return applies(day.getDate());
  }

  public boolean applies(final Session session) {
    if (session == null) {
      return false;
    }
    return applies(session.getDate());
  }

  public boolean applies(final LocalDate date) {
    if (date == null) {
      return false;
    }
    return keyDate.isBefore(date);
  }

  public void apply(final Day day) {
    if (day == null || keyDate.isAfter(day.getDate())) {
      return;
    }
    final List<Session> sessions = day.getItems();
    final List<Session> result = new ArrayList<>();
    for (final Session session : sessions) {
      result.add(session);
      for (final Session s : applyExcluding(session)) {
        result.add(s);
      }
    }
    day.setItems(result);
  }

  /**
   * Result list does not include initial session.
   * @param session
   * @return
   */
  public List<Session> applyExcluding(final Session session) {
    final List<Session> list = new ArrayList<>();
    apply(session, list);
    return list;
  }

  /**
   * Result list does not include initial session.
   * @param session
   * @return
   */
  public List<Session> applyIncluding(final Session session) {
    final List<Session> list = new ArrayList<>();
    list.add(session);
    apply(session, list);
    return list;
  }

  private void apply(final Session session, final List<Session> sessions) {
    Session tmp;
    for (final TimeOperation operation : timeOperations) {
      tmp = operation.split(session);
      if (tmp != null) {
        sessions.add(tmp);
        apply(tmp, sessions);
      }
    }
  }

}