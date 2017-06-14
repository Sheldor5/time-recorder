package at.sheldor5.tr.rules;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vanessa on 23.04.2017.
 */
public class Holiday {
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

    public Holiday(){
        nowinlong=System.currentTimeMillis();
    }

    public boolean applies() throws GeneralSecurityException, IOException {
        if(sundayRuleApplies()){
            return false;
        }
        return isHoliday();
    }

    public boolean applies(LocalDate date) throws GeneralSecurityException, IOException {
        setNowFromLocalDate(date);
        if(sundayRuleApplies()){
            return false;
        }
        return isHoliday();
    }

    public boolean applies(Date date) throws GeneralSecurityException, IOException {
        setNowFromDate(date);
        if(sundayRuleApplies()){
            return false;
        }
        return isHoliday();
    }

    public boolean applies(Session session) throws GeneralSecurityException, IOException {
        setNowFromLocalDate(session.getDate());
        if(sundayRuleApplies()){
            session.setMultiplier(2);
            return false;
        }
        session.setMultiplier(2);
        return isHoliday();
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
        Calendar service = getAccessToCalendarAPI();
        String calendarID = getCountryCalendarID("Austrian Holidays");
        DateTime now = new DateTime(nowinlong);
        long hours=3600000;
        DateTime tomorrow = new DateTime(nowinlong+hours);

        Events events = service.events().list(calendarID)
                //.setMaxResults(10) to just get 10 results
                .setTimeMin(now)
                .setTimeMax(tomorrow)
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0) {
            System.out.println("No Holidays found");
            return false;
        } else {
            System.out.println("Holiday found!");
            return true;
        }
    }

    private Calendar getAccessToCalendarAPI() throws GeneralSecurityException, IOException {
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        String client_id="1099400721753-k327qrlvue3dahmg5au24j7qbojn1sj9.apps.googleusercontent.com";
        String client_secret = "BJBPI1iS6fMpupK-2RLP9UvJ";
        String access_token= "ya29.GltpBJWsh4Vv2yC2m3KW5mQL_QyP7ugzp68_CcboZEeqiv43uOG6ZNVIF5qw-w_kvCvqnqHbBCnjfKXP5bPgfc1SRLHdFEIKH3jFpItVH0hBvSKJZR-R8bv2UqMn";
        String refresh_token="1/7A8cni4-tKCwZBuFBGPcVYU0I3-awrOmwt9FF4Fh2jA";

        GoogleCredential credentials =
                new GoogleCredential.Builder()
                        .setTransport(HTTP_TRANSPORT)
                        .setJsonFactory(JSON_FACTORY)
                        .setClientSecrets(client_id, client_secret).build();
        credentials.setAccessToken(access_token);
        credentials.setRefreshToken(refresh_token);

        // Initialize Calendar service with valid OAuth credentials
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName("time_recorder").build();
    }

    private String getCountryCalendarID(String country){
        switch(country){
            case "Australian Holidays": return  "en.australian#holiday@group.v.calendar.google.com";
            case "Austrian Holidays": return  "en.austrian#holiday@group.v.calendar.google.com";
            case "Brazilian Holidays": return  "en.brazilian#holiday@group.v.calendar.google.com";
            case "Canadian Holidays": return  "en.canadian#holiday@group.v.calendar.google.com";
            case "China Holidays": return  "en.china#holiday@group.v.calendar.google.com";
            case "Christian Holidays": return  "en.christian#holiday@group.v.calendar.google.com";
            case "Danish Holidays": return  "en.danish#holiday@group.v.calendar.google.com";
            case "Dutch Holidays": return  "en.dutch#holiday@group.v.calendar.google.com";
            case "Finnish Holidays": return  "en.finnish#holiday@group.v.calendar.google.com";
            case "French Holidays": return  "en.french#holiday@group.v.calendar.google.com";
            case "German Holidays": return  "en.german#holiday@group.v.calendar.google.com";
            case "Greek Holidays": return  "en.greek#holiday@group.v.calendar.google.com";
            case "Hong Kong (C) Holidays": return  "en.hong_kong_c#holiday@group.v.calendar.google.com";
            case "Hong Kong Holidays": return  "en.hong_kong#holiday@group.v.calendar.google.com";
            case "Indian Holidays": return  "en.indian#holiday@group.v.calendar.google.com";
            case "Indonesian Holidays": return  "en.indonesian#holiday@group.v.calendar.google.com";
            case "Iranian Holidays": return  "en.iranian#holiday@group.v.calendar.google.com";
            case "Irish Holidays": return  "en.irish#holiday@group.v.calendar.google.com";
            case "Islamic Holidays": return  "en.islamic#holiday@group.v.calendar.google.com";
            case "Italian Holidays": return  "en.italian#holiday@group.v.calendar.google.com";
            case "Japanese Holidays": return  "en.japanese#holiday@group.v.calendar.google.com";
            case "Jewish Holidays": return  "en.jewish#holiday@group.v.calendar.google.com";
            case "Malaysian Holidays": return  "en.malaysia#holiday@group.v.calendar.google.com";
            case "Mexican Holidays": return  "en.mexican#holiday@group.v.calendar.google.com";
            case "New Zealand Holidays": return  "en.new_zealand#holiday@group.v.calendar.google.com";
            case "Norwegian Holidays": return  "en.norwegian#holiday@group.v.calendar.google.com";
            case "Philippines Holidays": return  "en.philippines#holiday@group.v.calendar.google.com";
            case "Polish Holidays": return  "en.polish#holiday@group.v.calendar.google.com";
            case "Portuguese Holidays": return  "en.portuguese#holiday@group.v.calendar.google.com";
            case "Russian Holidays": return  "en.russian#holiday@group.v.calendar.google.com";
            case "Singapore Holidays": return  "en.singapore#holiday@group.v.calendar.google.com";
            case "South Africa Holidays": return  "en.sa#holiday@group.v.calendar.google.com";
            case "South Korean Holidays": return  "en.south_korea#holiday@group.v.calendar.google.com";
            case "Spain Holidays": return  "en.spain#holiday@group.v.calendar.google.com";
            case "Swedish Holidays": return  "en.swedish#holiday@group.v.calendar.google.com";
            case "Taiwan Holidays": return  "en.taiwan#holiday@group.v.calendar.google.com";
            case "Thai Holidays": return  "en.thai#holiday@group.v.calendar.google.com";
            case "UK Holidays": return  "en.uk#holiday@group.v.calendar.google.com";
            case "US Holidays": return  "en.usa#holiday@group.v.calendar.google.com";
            case "Vietnamese Holidays": return  "en.vietnamese#holiday@group.v.calendar.google.com";
            default: return "";
        }
    }
}
