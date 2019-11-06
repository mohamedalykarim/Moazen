package mohalim.islamic.moazen.core.service;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.CountDownLatch;

import mohalim.islamic.moazen.R;

public class ReminderWorker extends Worker {

    private MediaPlayer mediaPlayer;

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.reminder);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                countDownLatch.countDown();
            }
        });


        try {
            countDownLatch.await();
            return Result.success();

        } catch (InterruptedException e) {
            return Result.success();
        }
    }
}
