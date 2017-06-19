package at.sheldor5.tr.rules;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Vanessa on 19.06.2017.
 */
public class Deserialize {
    private static Deserialize INSTANCE;
    List<String> holidayList;

    private Deserialize() {
        deserialize();
    }

    public static Deserialize getInstance(){
        if(Deserialize.INSTANCE ==null){
            synchronized (HolidayList.class){
                Deserialize.INSTANCE =new Deserialize();
            }
        }
        return Deserialize.INSTANCE;
    }


    public void deserialize(){

        try
        {
            FileInputStream fis = new FileInputStream(FilePathHolidayDates.getPath());
            ObjectInputStream ois = new ObjectInputStream(fis);
            holidayList = (List) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
            return;
        }catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
        for(String tmp: holidayList){
            System.out.println(tmp);
        }
    }

    public List<String> getHolidayList(){
        return holidayList;
    }
}
