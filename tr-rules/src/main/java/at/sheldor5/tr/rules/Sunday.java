package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Session;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vanessa on 25.05.2017.
 */
public class Sunday extends AbstractRule{
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

    public boolean applies(Session session) {
        if(applies(session.getDate())){
            session.setMultiplier(2);
            return true;
        };
        return false;
    }

    public HashMap<Session,Boolean> applies(List<Session> sessionList) throws GeneralSecurityException, IOException {
        HashMap<Session,Boolean> evaluatedList=new HashMap<>();

        for (Session session: sessionList) {

            evaluatedList.put(session, convertbooleanToBoolean(applies(session)));
        }

        return evaluatedList;
    }

    private Boolean convertbooleanToBoolean(boolean value){
        if(value){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean applies(LocalDate date) {
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

