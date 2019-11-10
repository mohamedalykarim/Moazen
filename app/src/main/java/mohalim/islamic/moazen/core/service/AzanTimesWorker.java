package mohalim.islamic.moazen.core.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import mohalim.islamic.moazen.core.utils.AppDateUtil;
import mohalim.islamic.moazen.core.utils.Constants;

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
//        addAllAzanDay("06-11-2019, 22:15");

        String currentAzan = "";



        for (int i = 0 ; i < prayerTimes.length; i ++) {
            if (i == 0) currentAzan = Constants.AZAN_FUGR;
            if (i == 1) currentAzan = Constants.AZAN_SHOROQ;
            if (i == 2) currentAzan = Constants.AZAN_ZUHR;
            if (i == 3) currentAzan = Constants.AZAN_ASR;
            if (i == 4) currentAzan = Constants.AZAN_MAGHREB;
            if (i == 6) currentAzan = Constants.AZAN_ESHAA;

            addAllAzanDay(calendar.get(Calendar.DAY_OF_MONTH)
                    + "-" + (calendar.get(Calendar.MONTH) + 1)
                    + "-" + calendar.get(Calendar.YEAR)
                    + ", " + prayerTimes[i], currentAzan);
        }


        return Result.success();
    }


    private void addAllAzanDay(String date, String azanType) {
        long currentTimeMillis = System.currentTimeMillis();
        long azanTimeMillis = AppDateUtil.convertDateToMillisecond(date);
        long delayToAzan =  azanTimeMillis - currentTimeMillis;
        long delayToReminder = delayToAzan - (15 * 60000);

        if (currentTimeMillis > azanTimeMillis)return;


        Data data = new Data.Builder().putString(Constants.AZAN_TYPE, azanType).build();

        OneTimeWorkRequest azanRequest = new OneTimeWorkRequest.Builder(AzanWorker.class)
                .setInitialDelay(delayToAzan, TimeUnit.MILLISECONDS).setInputData(data).build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniqueWork(
                        "azan_"+azanType,
                        ExistingWorkPolicy.REPLACE,
                        azanRequest
                );



        if (currentTimeMillis > (azanTimeMillis-(15*60000)))return;

        OneTimeWorkRequest reminderRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(delayToReminder, TimeUnit.MILLISECONDS).setInputData(data).build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniqueWork(
                        "reminder_"+azanType,
                        ExistingWorkPolicy.REPLACE,
                        reminderRequest
                );

    }


}
