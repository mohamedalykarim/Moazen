package mohalim.islamic.moazen.core.utils;

import androidx.work.ListenableWorker;

import java.util.Map;
import java.util.Objects;

import javax.inject.Provider;

import mohalim.islamic.moazen.core.service.ChildWorkerFactory;

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

    public static Provider<ChildWorkerFactory> getWorkerFactoryProviderByKey(Map<Class<? extends ListenableWorker>, Provider<ChildWorkerFactory>> map, String key) {
        for (Map.Entry<Class<? extends ListenableWorker>, Provider<ChildWorkerFactory>> entry : map.entrySet()) {
            if (Objects.equals(key, entry.getKey().getName())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
