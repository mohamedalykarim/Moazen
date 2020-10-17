package mohalim.islamic.moazen.core.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Date;

import mohalim.islamic.moazen.core.AzanBroadcastReceiver;
import mohalim.islamic.moazen.core.utils.Constants;

public class AzanTimesWorker extends Worker {
    private final String TAG = "AzanTimesWorker";

    String[] prayerTimes;


    public AzanTimesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        prayerTimes = workerParams.getInputData().getStringArray("prayerTimes");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "set azan");
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);


        cancelReminder(alarmManager, Constants.AZAN_FUGR_REMINDER);
        cancelReminder(alarmManager, Constants.AZAN_ZUHR_REMINDER);
        cancelReminder(alarmManager, Constants.AZAN_ASR_REMINDER);
        cancelReminder(alarmManager, Constants.AZAN_MAGHREB_REMINDER);
        cancelReminder(alarmManager, Constants.AZAN_ESHAA_REMINDER);

        cancelAzan(alarmManager, Constants.AZAN_FUGR);
        cancelAzan(alarmManager, Constants.AZAN_ZUHR);
        cancelAzan(alarmManager, Constants.AZAN_ASR);
        cancelAzan(alarmManager, Constants.AZAN_MAGHREB);
        cancelAzan(alarmManager, Constants.AZAN_ESHAA);

        setReminder(alarmManager,prayerTimes[0], Constants.AZAN_FUGR_REMINDER);
        setReminder(alarmManager, prayerTimes[2], Constants.AZAN_ASR_REMINDER);
        setReminder(alarmManager, prayerTimes[3], Constants.AZAN_MAGHREB_REMINDER);
        setReminder(alarmManager, prayerTimes[5], Constants.AZAN_ESHAA_REMINDER);
        setReminder(alarmManager, prayerTimes[6], Constants.AZAN_ESHAA_REMINDER);

        setAzan(alarmManager,prayerTimes[0], Constants.AZAN_FUGR);
        setAzan(alarmManager,prayerTimes[2], Constants.AZAN_ZUHR);
        setAzan(alarmManager, prayerTimes[3], Constants.AZAN_ASR);
        setAzan(alarmManager, prayerTimes[5], Constants.AZAN_ESHAA);
        setAzan(alarmManager, prayerTimes[6], Constants.AZAN_ESHAA);

        setAzan(alarmManager,"21:25", Constants.AZAN_ESHAA);


        return Result.success();
    }

    private void cancelReminder(AlarmManager alarmManager, int reminderType) {
        Intent intent = new Intent(getApplicationContext(), AzanBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getService(getApplicationContext(), reminderType, intent, 0);
        alarmManager.cancel(alarmIntent);
    }

    private void cancelAzan(AlarmManager alarmManager, int azanType) {
        Intent intent = new Intent(getApplicationContext(), AzanBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getService(getApplicationContext(), azanType, intent, 0);
        alarmManager.cancel(alarmIntent);
    }

    private void setReminder(AlarmManager alarmManager, String time, int reminderType) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3,5)));
        calendar.set(Calendar.SECOND, Integer.parseInt("00"));
        calendar.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

        calendar.setTimeInMillis(calendar.getTimeInMillis() - 5 * 60 * 1000);

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());

        if (calendar.getTimeInMillis() > nowCalendar.getTimeInMillis()){
            Log.d(TAG, "setting time more than now time " + calendar.getTimeInMillis());

            Intent intent = new Intent(getApplicationContext(), AzanBroadcastReceiver.class);
            intent.setAction("mohalim.islamic.moazen.START");
            intent.putExtra(Constants.AZAN_RECEIVER_ORDER, Constants.AZAN_RECEIVER_ORDER_INIT_REMINDER);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), reminderType, intent, 0);
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            }
        }
    }

    private void setAzan(AlarmManager alarmManager, String time, int azanType) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3,5)));
        calendar.set(Calendar.SECOND, Integer.parseInt("00"));
        calendar.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());

        if (calendar.getTimeInMillis() > nowCalendar.getTimeInMillis()){
            Log.d(TAG, "setting time more than now time " + calendar.getTimeInMillis());

            Intent intent = new Intent(getApplicationContext(), AzanBroadcastReceiver.class);
            intent.putExtra(Constants.AZAN_RECEIVER_ORDER, Constants.AZAN_RECEIVER_ORDER_INIT_AZAN);
            intent.setAction("mohalim.islamic.moazen.START");
            intent.putExtra(Constants.AZAN_TYPE, azanType);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), azanType, intent, 0);
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

            } else if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            }
        }
    }


}
