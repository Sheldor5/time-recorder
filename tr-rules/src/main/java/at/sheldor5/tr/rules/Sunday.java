package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Day;
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
public class Sunday implements IRule{
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

    @Override
    public String getName() {
        return "Sunday";
    }

    @Override
    public String getDescription() {
        return "Rule applies if day is a sunday";
    }

    @Override
    public boolean applies(Day day) {
        return applies(day.getDate());
    }

    @Override
    public void apply(Day day) {
        if(applies(day.getDate())){
            List<Session> sessions = day.getItems();
            apply(sessions);
        }
    }

    public void apply(Session session){
        if(applies(session)){
            session.setMultiplier(2);
        }
    }

    public void apply(List<Session> sessions){
        for(Session session : sessions){
            apply(session);
        }
    }

    private void setDate(Date date) {
        this.date = date;
    }

    public boolean applies() {
        return isSunday();
    }

    public boolean applies(Session session) {
        return applies(session.getDate());
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

