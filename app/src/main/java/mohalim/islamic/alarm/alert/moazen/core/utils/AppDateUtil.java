package mohalim.islamic.alarm.alert.moazen.core.utils;

import java.text.SimpleDateFormat;

public class AppDateUtil {
    public static long convertDateToMillisecond(String date){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
            formatter.setLenient(false);

            return formatter.parse(date).getTime();

        }catch (Exception e){
            return 0;
        }
    }
}
