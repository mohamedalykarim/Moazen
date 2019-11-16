package mohalim.islamic.moazen.core.service;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.utils.Constants;
import mohalim.islamic.moazen.core.utils.Utils;

public class AzanWorker extends Worker {
    private static final String CHANNEL_ID = "Azan";

    MediaPlayer mediaPlayer;

    String  azanType;

    public AzanWorker(@NonNull Context context, @NonNull WorkerParameters workerParams, MediaPlayer mediaPlayer) {
        super(context, workerParams);
        azanType = workerParams.getInputData().getString(Constants.AZAN_TYPE);
        this.mediaPlayer = mediaPlayer;
    }

    @NonNull
    @Override
    public Result doWork() {
        notification();
        CountDownLatch countDownLatch = new CountDownLatch(1);


//        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.quds);
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
                .setContentText(Utils.getAzantTypeResource(azanType)+ " Azan Now")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(0, builder.build());

    }



    public static class Factory implements ChildWorkerFactory {

        private final Provider<MediaPlayer> modelProvider;

        @Inject
        public Factory(@Named("AzanMediaPlayer") Provider<MediaPlayer> modelProvider) {
            this.modelProvider = modelProvider;
        }

        @Override
        public ListenableWorker create(Context context, WorkerParameters workerParameters) {
            return new AzanWorker(context,
                    workerParameters,
                    modelProvider.get());
        }
    }



}
