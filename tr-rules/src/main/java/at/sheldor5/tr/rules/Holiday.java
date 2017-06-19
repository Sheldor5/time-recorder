package at.sheldor5.tr.rules;

import at.sheldor5.tr.api.time.Day;
import at.sheldor5.tr.api.time.Session;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vanessa on 23.04.2017.
 */
public class Holiday implements IRule {
    private static HttpTransport HTTP_TRANSPORT;
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private long nowinlong;

    public Holiday(long nowinlong) {
        this.nowinlong = nowinlong;
    }

    public Holiday(LocalDate localDate){
        setNowFromLocalDate(localDate);
    }

    private void setNowFromLocalDate(LocalDate localDate){
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        setNowFromDate(date);
    }

    public Holiday(Date date){
        setNowFromDate(date);
    }

    private void setNowFromDate(Date date) {
        nowinlong=date.getTime();
    }

    @Override
    public String getName() {
        return "Holiday";
    }

    @Override
    public String getDescription() {
        return "Rule applies if day is a national holiday";
    }

    @Override
    public boolean applies(Day day) {
        return applies(day.getDate());
    }

    @Override
    public void apply(Day day) {
        List<Session> sessions = day.getItems();
        apply(sessions);
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
    public Holiday(){
        nowinlong=System.currentTimeMillis();
    }

    public boolean applies() throws GeneralSecurityException, IOException {
        if(sundayRuleApplies()){
            return false;
        }
        return isHoliday();
    }

    public boolean applies(LocalDate date) {
        setNowFromLocalDate(date);
        if(sundayRuleApplies()){
            return false;
        }
        try {
            return isHoliday();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean applies(Date date) throws GeneralSecurityException, IOException {
        setNowFromDate(date);
        if(sundayRuleApplies()){
            return false;
        }
        return isHoliday();
    }

    public boolean applies(Session session) {
        setNowFromLocalDate(session.getDate());
        if(sundayRuleApplies()){
            return false;
        }
        try {
            return isHoliday();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private boolean sundayRuleApplies(){
        Date date=new Date(nowinlong);
        Sunday sundaycheck = new Sunday(date);
        return sundaycheck.applies();
    }


    private boolean isHoliday() throws GeneralSecurityException, IOException {
        /*HolidayList holidayList = HolidayList.getInstance();
        DateTime now = new DateTime(nowinlong);
        return holidayList.searchForHoliday(now);
*/
        HolidayCheck check = HolidayCheck.getInstance();
        DateTime now = new DateTime(nowinlong);
        return check.searchForHoliday(now);
//        return Test();
    }

    private boolean Test(){
        Deserialize de = Deserialize.getInstance();
        List<String> list = new ArrayList<>();
        list = de.getHolidayList();
        long now = 15094908;
        now=now*100000;
        DateTime date = new DateTime(now);

        HolidayCheck check = HolidayCheck.getInstance();
        DateTime nowdate = new DateTime(now);
        System.out.println( check.searchForHoliday(nowdate));
        return check.searchForHoliday(nowdate);
    }

}
