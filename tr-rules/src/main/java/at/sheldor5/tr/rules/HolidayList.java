package at.sheldor5.tr.rules;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
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
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential.REFRESH_TOKEN;

/**
 * Created by Vanessa on 18.06.2017.
 */
public class HolidayList {
    private static HttpTransport HTTP_TRANSPORT;
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private List<Event> holidayList;
    private static HolidayList INSTANCE;


    private HolidayList() {
        storeHolidays();
    }

    public List<Event> getHolidayList(){
        return holidayList;
    }

    public static HolidayList getInstance(){
        if(HolidayList.INSTANCE ==null){
            synchronized (HolidayList.class){
                HolidayList.INSTANCE =new HolidayList();
            }
        }
        return HolidayList.INSTANCE;
    }

    private void storeHolidays(){
        Calendar service = getAccessToCalendarAPI();
        String calendarID = getCountryCalendarID("Austrian Holidays");
        DateTime start = new DateTime(setDateFromLocalDate(LocalDate.of(2000,1,1)));
        DateTime end= new DateTime(setDateFromLocalDate(LocalDate.of(2100,12,1)));
        Events events = null;
        try {
            events = service.events().list(calendarID)
                    //.setMaxResults(10) to just get 10 results
                    .setTimeMin(start)
                    .setTimeMax(end)
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holidayList = events.getItems();
    }

    public boolean searchForHoliday(DateTime date){
        if(holidayList.size()==0){
            return false;
        }

        for (Event event: holidayList) {
            if(date.toStringRfc3339().startsWith(event.getStart().getDate().toStringRfc3339())){
                return true;
            }
        }
        return false;
    }


    private Date setDateFromLocalDate(LocalDate localDate){

        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Calendar getAccessToCalendarAPI() {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String client_id="1099400721753-k327qrlvue3dahmg5au24j7qbojn1sj9.apps.googleusercontent.com";
        String client_secret = "BJBPI1iS6fMpupK-2RLP9UvJ";
        String access_token= "ya29.GltuBGHdi_vKYe5UXDOSoZn2Op5npHs5GcC9RMu0AbjMupNPWPsXvp7O6jRNOoOm72txoH7PR6UM3nlB4q25AEoh08J8egnpOLFhendvvMzGCm20Mzt6JAm3HAvy";
        String refresh_token="1/yClJ4YqCFkBcxU2KolGoe39U6tA2tq_GokmbmEly-64";

        /*GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(client_id, client_secret)
                .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
                .setRefreshToken(refresh_token);


        try {
            credential.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String newaccessToken = credential.getAccessToken();
*/

        GoogleCredential credentials =
                new GoogleCredential.Builder()
                        .setTransport(HTTP_TRANSPORT)
                        .setJsonFactory(JSON_FACTORY)
                        .setClientSecrets(client_id, client_secret).build();

        credentials.setRefreshToken(refresh_token);
        credentials.setAccessToken(access_token);

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
