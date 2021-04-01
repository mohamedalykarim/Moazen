package mohalim.islamic.moazen.core.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import mohalim.islamic.moazen.core.AzanBroadcastReceiver;
import mohalim.islamic.moazen.core.utils.Constants;
import mohalim.islamic.moazen.core.utils.Utils;
import mohalim.islamic.moazen.ui.update_times.UpdateTimesActivity;

import static android.content.Context.POWER_SERVICE;

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
        if(Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                return Result.failure();
            }
        }

        Intent intent = new Intent(getApplicationContext(), UpdateTimesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.PLAYER_TIMES, prayerTimes);
        getApplicationContext().startActivity(intent);



        return Result.success();
    }




}
