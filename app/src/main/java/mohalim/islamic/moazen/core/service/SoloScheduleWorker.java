package mohalim.islamic.moazen.core.service;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.Date;

import mohalim.islamic.moazen.core.AzanBroadcastReceiver;
import mohalim.islamic.moazen.core.utils.Constants;

public class SoloScheduleWorker extends Worker {
    String action ;
    int azanType;
    String time;

    public SoloScheduleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        action = workerParams.getInputData().getString(Constants.ACTION);
        azanType = workerParams.getInputData().getInt(Constants.AZAN_TYPE,1);
        time = workerParams.getInputData().getString(Constants.TIME);
    }

    @NonNull
    @Override
    public Result doWork() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3,5)));
        calendar.set(Calendar.SECOND, Integer.parseInt("00"));
        calendar.set(Calendar.MILLISECOND, Integer.parseInt("0000"));

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());

        if (calendar.getTimeInMillis() + 2000 < nowCalendar.getTimeInMillis()) return Result.failure();

        Intent intent = new Intent(getApplicationContext(), AzanBroadcastReceiver.class);
        intent.setAction(action);
        intent.putExtra(Constants.AZAN_TYPE, azanType);

        getApplicationContext().sendBroadcast(intent);

        return Result.success();
    }

}
