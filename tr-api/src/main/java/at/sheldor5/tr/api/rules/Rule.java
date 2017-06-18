package at.sheldor5.tr.api.rules;

import at.sheldor5.tr.api.time.Day;

/**
 * @author Michael Palata
 * @date 18.06.2017
 */
public interface Rule {

  int getId();

  String getName();

  boolean applies(Day day);

  void apply(Day day);

}
