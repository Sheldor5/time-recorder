package at.sheldor5.tr.core.rules;

import at.sheldor5.tr.api.objects.Session;
import at.sheldor5.tr.api.objects.Day;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class Rule {

  String name;
  LocalDate keyDate;
  List<TimeOperation> timeOperations = new ArrayList<>();
  List<EffortOperation> effortOperations = new ArrayList<>();

  Rule() {

  }

  public Rule(final String name, final LocalDate keyDate) {
    this.name = name;
    this.keyDate = keyDate;
  }

  public boolean applies(final Session session) {
    if (session == null || keyDate.isAfter(session.getDate())) {
      return false;
    }
    for (final TimeOperation operation : timeOperations) {
      if (operation.applies(session)) {
        return true;
      }
    }
    return false;
  }

  public boolean applies(final Day day) {
    if (day == null || keyDate.isAfter(day.getItems().get(0).getDate())) {
      return false;
    }
    for (final EffortOperation operation : effortOperations) {
      /*if (operation.applies(session)) {
        return true;
      }*/
    }
    return false;
  }

  public List<Session> apply(final Session session) {
    final List<Session> list = new LinkedList<>();
    return new ArrayList<>(list);
  }

}