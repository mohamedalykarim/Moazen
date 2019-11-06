package mohalim.islamic.moazen.core.di.module;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dagger.Module;
import dagger.Provides;
import mohalim.islamic.moazen.core.utils.PrayTime;

@Module
public class AppModule {

    @Provides
    static PrayTime providePrayerTime(){
        PrayTime prayers = new PrayTime();

        prayers.setTimeFormat(prayers.Time24);
        prayers.setCalcMethod(prayers.Egypt);
        prayers.setAsrJuristic(prayers.Shafii);
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        return prayers;
    }


    @Provides
    static String[] providePrayTimes(PrayTime prayers){
        double latitude = 25.8441239;
        double longitude = 32.834721;
        double timezone = 2;

        // Test Prayer times here

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
                latitude, longitude, timezone);

        String[] times = new String[prayerTimes.size()];

        for (int i=0; i < prayerTimes.size(); i++){
            times[i] = prayerTimes.get(i);
        }

        return times;
    }


    @Provides
    Calendar provideCalender(){
        return Calendar.getInstance();
    }





}
