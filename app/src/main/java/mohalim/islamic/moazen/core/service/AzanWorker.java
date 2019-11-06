package mohalim.islamic.moazen.core.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.CountDownLatch;

import mohalim.islamic.moazen.R;

public class AzanWorker extends Worker {
    private static final String CHANNEL_ID = "Azan";
    MediaPlayer mediaPlayer;

    public AzanWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        notification();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.quds);
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

    private void notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Moazen")
                .setContentText("Zohr Azan Now")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(0, builder.build());

    }



}
