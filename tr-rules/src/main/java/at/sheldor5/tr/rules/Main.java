package at.sheldor5.tr.rules;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vanessa on 24.04.2017.
 */
public class Main {
    public static void main(String [ ] args)
    {
        Serialize se = Serialize.getInstance();
        Deserialize de = Deserialize.getInstance();
        List<String> list = new ArrayList<>();
        list = de.getHolidayList();
        long now = 15094908;
        now=now*100000;
        DateTime date = new DateTime(now);

        HolidayCheck check = HolidayCheck.getInstance();
        DateTime nowdate = new DateTime(now);
        System.out.println( check.searchForHoliday(nowdate));
    }
}
