package at.sheldor5.tr.rules;

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
import java.util.List;

/**
 * Created by Vanessa on 18.06.2017.
 */
public class HolidayList {
    private static HttpTransport HTTP_TRANSPORT;
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    private List<Event> holidayList;
    private static HolidayList INSTANCE;

    public HolidayList() {
        storeHolidays();
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
        DateTime end= new DateTime(setDateFromLocalDate(LocalDate.of(2100,1,1)));
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
        String access_token= "ya29.GlxtBNO6UfjsHl8tgY3Fa6jJHvlLSF70OkvVbP5DfWRR4gI2uUiLEi1LP1Dgfe0jaUQRqPuq5y2NdtDD0DQ5KsVmNYSwBVa9W9NlZ1DUj-87uoPweEhqpmOM_-7fwA";
        String refresh_token="1/PqznRm7at2uUVjMDyADNBm3Opll4QkoIBQRdiOmMjsU";

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
