package mohalim.islamic.moazen.core.utils;

import androidx.work.ListenableWorker;

import java.util.Map;
import java.util.Objects;

import javax.inject.Provider;

public class Utils {

    public static String getAzantTypeResource(String azanType){
        if (azanType.equals(Constants.AZAN_FUGR)){
            return "Al fagr";
        }else if (azanType.equals(Constants.AZAN_SHOROQ)){
            return "Al Shoroq";
        }else if (azanType.equals(Constants.AZAN_ZUHR)){
            return "Al Zuhr";
        }else if (azanType.equals(Constants.AZAN_ASR)){
            return "Al Asr";
        }else if (azanType.equals(Constants.AZAN_MAGHREB)){
            return "Al Maghreb";
        }else if (azanType.equals(Constants.AZAN_ESHAA)){
            return "Al Eshaa";
        }else return null;
    }


}
