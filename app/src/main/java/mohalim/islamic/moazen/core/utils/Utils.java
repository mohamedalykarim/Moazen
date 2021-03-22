package mohalim.islamic.moazen.core.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.work.ListenableWorker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Provider;

import mohalim.islamic.moazen.R;

public class Utils {

    private static final String TAG = "Utils";

    public static String getAzantTypeName(Context context, int azanType){
        if (azanType == Constants.AZAN_FUGR){
            return context.getResources().getString(R.string.azan_fagr);
        }else if (azanType == Constants.AZAN_ZUHR){
            return context.getResources().getString(R.string.azan_zuhr);
        }else if (azanType == Constants.AZAN_ASR){
            return context.getResources().getString(R.string.azan_asr);
        }else if (azanType == Constants.AZAN_MAGHREB){
            return context.getResources().getString(R.string.azan_maghreb);
        }else if (azanType == Constants.AZAN_ESHAA){
            return context.getResources().getString(R.string.azan_eshaa);
        }else return "";
    }

    public static String getReminderName(Context context, int azanType){
        if (azanType == Constants.AZAN_FUGR){
            return context.getResources().getString(R.string.azan_fagr)+"_Reminder";
        }else if (azanType == Constants.AZAN_ZUHR){
            return context.getResources().getString(R.string.azan_zuhr)+"_Reminder";
        }else if (azanType == Constants.AZAN_ASR){
            return context.getResources().getString(R.string.azan_asr)+"_Reminder";
        }else if (azanType == Constants.AZAN_MAGHREB){
            return context.getResources().getString(R.string.azan_maghreb)+"_Reminder";
        }else if (azanType == Constants.AZAN_ESHAA){
            return context.getResources().getString(R.string.azan_eshaa)+"_Reminder";
        }else return "";
    }

    public static String getNextAzantTypeName(Context context, int azanType){
        if (azanType == Constants.AZAN_FUGR){
            return context.getResources().getString(R.string.azan_zuhr);
        }else if (azanType == Constants.AZAN_ZUHR){
            return context.getResources().getString(R.string.azan_asr);
        }else if (azanType == Constants.AZAN_ASR){
            return context.getResources().getString(R.string.azan_maghreb);
        }else if (azanType == Constants.AZAN_MAGHREB){
            return context.getResources().getString(R.string.azan_eshaa);
        }else if (azanType == Constants.AZAN_ESHAA){
            return context.getResources().getString(R.string.azan_fagr);
        }else return "";
    }

    public static String []  getAzanTimes(Context context, long timeMillisecond, PrayTime prayers){
        double latitude = Double.parseDouble(AppSettingHelper.getLatitude(context));
        double longitude = Double.parseDouble(AppSettingHelper.getLongitude(context));;
        double timezone = Double.parseDouble(AppSettingHelper.getTimeZone(context));;

        // Test Prayer times here

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeMillisecond);

        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
                latitude, longitude, timezone);

        String[] times = new String[prayerTimes.size()];

        for (int i=0; i < prayerTimes.size(); i++){
            times[i] = prayerTimes.get(i);
        }

        return times;
    }


    public static String getRemainTime(int azanType, String[] timesOfToday, String[] timesOfTomorrow) {
        long millisUntilNextAzan = 0;
        if (azanType == Constants.AZAN_FUGR) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[0].substring(0, 2)));
            calendar1.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[0].substring(3, 5)));
            calendar1.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar1.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[2].substring(0, 2)));
            calendar2.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[2].substring(3, 5)));
            calendar2.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar2.set(Calendar.MILLISECOND, Integer.parseInt("0000"));


            millisUntilNextAzan = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        }else if (azanType == Constants.AZAN_ZUHR) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[2].substring(0, 2)));
            calendar1.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[2].substring(3, 5)));
            calendar1.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar1.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[3].substring(0, 2)));
            calendar2.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[3].substring(3, 5)));
            calendar2.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar2.set(Calendar.MILLISECOND, Integer.parseInt("0000"));


            millisUntilNextAzan = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        }else if (azanType == Constants.AZAN_ASR) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[3].substring(0, 2)));
            calendar1.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[3].substring(3, 5)));
            calendar1.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar1.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[5].substring(0, 2)));
            calendar2.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[5].substring(3, 5)));
            calendar2.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar2.set(Calendar.MILLISECOND, Integer.parseInt("0000"));


            millisUntilNextAzan = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        }else if (azanType == Constants.AZAN_MAGHREB) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[5].substring(0, 2)));
            calendar1.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[5].substring(3, 5)));
            calendar1.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar1.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[6].substring(0, 2)));
            calendar2.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[6].substring(3, 5)));
            calendar2.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar2.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

            millisUntilNextAzan = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        }else if (azanType == Constants.AZAN_ESHAA) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfToday[6].substring(0, 2)));
            calendar1.set(Calendar.MINUTE, Integer.parseInt(timesOfToday[6].substring(3, 5)));
            calendar1.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar1.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timesOfTomorrow[0].substring(0, 2)));
            calendar2.set(Calendar.MINUTE, Integer.parseInt(timesOfTomorrow[0].substring(3, 5)));
            calendar2.set(Calendar.SECOND, Integer.parseInt("00"));
            calendar2.set(Calendar.MILLISECOND, Integer.parseInt("0000"));
            calendar2.setTimeInMillis(calendar2.getTimeInMillis()+24*60*60*1000);

            millisUntilNextAzan = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        }

        Log.d(TAG, "getRemainTime: "+millisUntilNextAzan);

        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilNextAzan),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilNextAzan) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilNextAzan)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilNextAzan) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilNextAzan)));
    }



    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            return Settings.canDrawOverlays(context);
        } else {
            if (Settings.canDrawOverlays(context)) return true;
            try {
                WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (mgr == null) return false; //getSystemService might return null
                View viewToAdd = new View(context);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0, android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
                viewToAdd.setLayoutParams(params);
                mgr.addView(viewToAdd, params);
                mgr.removeView(viewToAdd);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static String [] getPrayerTimes (Context context){
        PrayTime prayers = new PrayTime();

        prayers.setTimeFormat(prayers.Time24);
        prayers.setCalcMethod(AppSettingHelper.getAzanCalculationMethod(context, prayers.Egypt));
        prayers.setAsrJuristic(AppSettingHelper.getAzanJuristicMethod(context,prayers.Shafii));
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        double latitude = Double.parseDouble(AppSettingHelper.getLatitude(context));
        double longitude = Double.parseDouble(AppSettingHelper.getLongitude(context));;
        double timezone = Double.parseDouble(AppSettingHelper.getTimeZone(context));;

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

}
