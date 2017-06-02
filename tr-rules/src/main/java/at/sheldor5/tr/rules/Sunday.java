package at.sheldor5.tr.rules;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vanessa on 25.05.2017.
 */
public class Sunday {
    private Date date;

    public Sunday(LocalDate localDate) {
        setDateFromLocalDate(localDate);
    }

    private void setDateFromLocalDate(LocalDate localDate) {
        setDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    public Sunday(){
        setDateFromLocalDate(LocalDate.now());
    }

    public Sunday(Date date) {
        setDate(date);
    }

    private void setDate(Date date) {
        this.date = date;
    }

    public boolean applies() {
        return isSunday();
    }

    public boolean applies(LocalDate date) throws GeneralSecurityException, IOException {
        setDateFromLocalDate(date);
        return isSunday();
    }

    public boolean applies(Date date) throws GeneralSecurityException, IOException {
        setDate(date);
        return isSunday();
    }

    private boolean isSunday(){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int day = c1.get(Calendar.DAY_OF_WEEK);
        if(day == Calendar.SUNDAY){

            System.out.println("Day is sunday");
            return true;
        }
        System.out.println("Day is not sunday");
        return false;
    }
}

