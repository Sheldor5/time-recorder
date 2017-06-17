package at.sheldor5.tr.rules;

import java.time.LocalDate;

/**
 * Created by Vanessa on 14.06.2017.
 */
public class Rule extends AbstractRule {

    public Rule() {
        super();
    }

    public Rule(final String name, final LocalDate keyDate){
        super(name, keyDate);
    }
}
