package mohalim.islamic.alarm.alert.moazen.core.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefsHelper {

    private static final String APP_SETTINGS =  "mohalim.islamic.alarm.alert.moazen.APP_SETTING";


    /**
     * Return Shared Preferences
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }


    /**
     * First time app open
     */

    private static final String FIRST_TIME_APP_OPENED =  "first_time_app_opened";

    public static boolean getIsFirstTimeAppOpened(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_TIME_APP_OPENED , true);
    }

    public static void setIsFirstTimeAppOpened(Context context, boolean newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(FIRST_TIME_APP_OPENED , newValue);
        editor.commit();
    }


    /**
     * Location Name
     */

    private static final String LOCATION_NAME =  "location_name";

    public static String getLocationName(Context context) {
        return getSharedPreferences(context).getString(LOCATION_NAME, "Cairo");
    }

    public static void setLocationName(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LOCATION_NAME , newValue);
        editor.commit();
    }


    /**
     * LATITUDE
     */

    private static final String LATITUDE =  "latitude";

    public static String getLatitude(Context context) {
        return getSharedPreferences(context).getString(LATITUDE, "31.235823");
    }

    public static void setLatitude(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LATITUDE , newValue);
        editor.commit();
    }

    /**
     * LONGITUDE
     */

    private static final String LONGITUDE =  "longitude";

    public static String getLongitude(Context context) {
        return getSharedPreferences(context).getString(LONGITUDE, "30.044448");
    }

    public static void setLongitude(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LONGITUDE , newValue);
        editor.commit();
    }

    /**
     * TIME_ZONE
     */

    private static final String TIME_ZONE =  "time_zone";

    public static String getTimeZone(Context context) {
        return getSharedPreferences(context).getString(TIME_ZONE, "2");
    }

    public static void setTimeZone(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(TIME_ZONE , newValue);
        editor.commit();
    }


    /**
     * AZAN_CALCULATION_METHOD
     */

    private static final String AZAN_CALCULATION_METHOD =  "azan_calculation_method";

    public static int getAzanCalculationMethod(Context context, int defaultValue) {
        return getSharedPreferences(context).getInt(AZAN_CALCULATION_METHOD, defaultValue);
    }

    public static void setAzanCalculationMethod(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(AZAN_CALCULATION_METHOD , newValue);
        editor.commit();
    }

    /**
     * AZAN_JURISTIC_METHOD
     */

    private static final String AZAN_JURISTIC_METHOD =  "azan_juristic_method";

    public static int getAzanJuristicMethod(Context context, int defaultValue) {
        return getSharedPreferences(context).getInt(AZAN_JURISTIC_METHOD, defaultValue);
    }


    public static void setAzanJuristicMethod(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(AZAN_JURISTIC_METHOD , newValue);
        editor.commit();
    }


    /**
     * REMINDER_BEFORE_TIME
     */

    private static final String REMINDER_BEFORE_TIME =  "reminder_before_time";

    public static int getReminderTime(Context context, int defaultValue) {
        return getSharedPreferences(context).getInt(REMINDER_BEFORE_TIME, defaultValue);
    }


    public static void setReminderTime(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(REMINDER_BEFORE_TIME , newValue);
        editor.commit();
    }


}
