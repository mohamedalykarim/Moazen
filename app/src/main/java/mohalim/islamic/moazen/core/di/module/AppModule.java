package mohalim.islamic.moazen.core.di.module;

import android.app.Application;
import android.content.ContextWrapper;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.utils.AppExecutor;
import mohalim.islamic.moazen.core.utils.AppSettingHelper;
import mohalim.islamic.moazen.core.utils.PrayTime;

@Module
public class AppModule {

    @Provides
    @Singleton
    static AppExecutor provideAppExecuter(){
        return AppExecutor.getInstance();
    }

    @Provides
    static PrayTime providePrayerTime(Application application){
        PrayTime prayers = new PrayTime();

        prayers.setTimeFormat(prayers.Time24);
        prayers.setCalcMethod(AppSettingHelper.getAzanCalculationMethod(application, prayers.Egypt));
        prayers.setAsrJuristic(AppSettingHelper.getAzanJuristicMethod(application,prayers.Shafii));
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        return prayers;
    }


    @Provides
    static String[] providePrayTimes(PrayTime prayers, Application application){
        double latitude = Double.parseDouble(AppSettingHelper.getLatitude(application));
        double longitude = Double.parseDouble(AppSettingHelper.getLongitude(application));;
        double timezone = Double.parseDouble(AppSettingHelper.getTimeZone(application));;

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

    @Provides
    @Singleton
    static MediaPlayer provideAzanMediaPlayer(Application application){
        return MediaPlayer.create(application, R.raw.quds);
    }








}
