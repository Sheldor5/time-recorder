package at.sheldor5.tr.rules;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vanessa on 19.06.2017.
 */
public class Serialize {
    List<Event> holidayList;
    List<String> dateList;


    private static Serialize INSTANCE;

    private Serialize() {
        getHolidayList();
        storeHolidaysinDateTime();
        serialize();
    }



    public static Serialize getInstance(){
        if(Serialize.INSTANCE ==null){
            synchronized (HolidayList.class){
                Serialize.INSTANCE =new Serialize();
            }
        }
        return Serialize.INSTANCE;
    }

    public void getHolidayList(){
        HolidayList holidayList = HolidayList.getInstance();
        this.holidayList = holidayList.getHolidayList();
    }

    private void storeHolidaysinDateTime(){
        dateList = new ArrayList<String>();
        for (Event event: holidayList) {
            dateList.add(event.getStart().getDate().toStringRfc3339());
        }
    }

    public void serialize(){
        try{
            FileOutputStream fos= new FileOutputStream(FilePathHolidayDates.getPath());
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(dateList);
            oos.close();
            fos.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }


}
