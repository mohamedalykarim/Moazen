package mohalim.islamic.moazen.core.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import mohalim.islamic.moazen.core.utils.AppDateUtil;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AzanTimesWorker extends Worker {

    String[] prayerTimes;


    public AzanTimesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        prayerTimes = workerParams.getInputData().getStringArray("prayerTimes");

    }

    @NonNull
    @Override
    public Result doWork() {

        Calendar calendar = Calendar.getInstance();
        addAllAzanDay("06-11-2019, 22:15");

        for (String prayerTime : prayerTimes) {

            addAllAzanDay(calendar.get(Calendar.DAY_OF_MONTH)
                    + "-" + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.YEAR)
                    + ", " + prayerTime);
        }


        return Result.success();
    }


    private void addAllAzanDay(String date) {
        long currentTimeMillis = System.currentTimeMillis();
        long azanTimeMillis = AppDateUtil.convertDateToMillisecond(date);
        long delayToAzan =  azanTimeMillis - currentTimeMillis;
        long delayToReminder = delayToAzan - (15 * 60000);

        if (currentTimeMillis > azanTimeMillis)return;

        OneTimeWorkRequest azanRequest = new OneTimeWorkRequest.Builder(AzanWorker.class)
                .setInitialDelay(delayToAzan, TimeUnit.MILLISECONDS).build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniqueWork(
                        "azan"+date,
                        ExistingWorkPolicy.REPLACE,
                        azanRequest
                );



        if (currentTimeMillis > (azanTimeMillis-(15*60000)))return;

        OneTimeWorkRequest reminderRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(delayToReminder, TimeUnit.MILLISECONDS).build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniqueWork(
                        "reminder"+date,
                        ExistingWorkPolicy.REPLACE,
                        reminderRequest
                );

    }


}
