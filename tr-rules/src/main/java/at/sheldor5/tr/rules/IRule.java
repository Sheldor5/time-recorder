package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vanessa on 14.06.2017.
 */
public interface IRule {

    String getName();

    String getDescription();

    boolean applies(final Day day);

    boolean applies(final Session session);

    boolean applies(final LocalDate date);

    void apply(final Day day);

    /**
     * Result list does not include initial session.
     * @param session
     * @return
     */
    List<Session> applyExcluding(final Session session);

    /**
     * Result list does not include initial session.
     * @param session
     * @return
     */
    List<Session> applyIncluding(final Session session);

}
