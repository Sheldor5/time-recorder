package at.sheldor5.tr.rules;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vanessa on 19.06.2017.
 */
public class HolidayCheck {

    private static HolidayCheck INSTANCE;
    List<String> holidayList;

    private HolidayCheck() {
        Deserialize des = Deserialize.getInstance();
        holidayList=new ArrayList<String>();
        holidayList=des.getHolidayList();
    }

    public static HolidayCheck getInstance(){
        if(HolidayCheck.INSTANCE ==null){
            synchronized (HolidayList.class){
                HolidayCheck.INSTANCE =new HolidayCheck();
            }
        }
        return HolidayCheck.INSTANCE;
    }
    public boolean searchForHoliday(DateTime date){

        if(holidayList.size()==0){
            return false;
        }

        for (String string: holidayList) {
            if(date.toStringRfc3339().startsWith(string)){
                return true;
            }
        }
        return false;
    }
}
