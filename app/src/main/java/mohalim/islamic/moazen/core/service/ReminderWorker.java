package mohalim.islamic.moazen.core.service;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import mohalim.islamic.moazen.R;

public class ReminderWorker extends Worker {

    private MediaPlayer mediaPlayer;

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams, MediaPlayer mediaPlayer) {
        super(context, workerParams);
        this.mediaPlayer = mediaPlayer;
    }

    @NonNull
    @Override
    public Result doWork() {

        CountDownLatch countDownLatch = new CountDownLatch(1);
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


    public static class Factory implements ChildWorkerFactory {

        private final Provider<MediaPlayer> modelProvider;

        @Inject
        public Factory(@Named("ReminderMediaPlayer") Provider<MediaPlayer> modelProvider) {
            this.modelProvider = modelProvider;
        }

        @Override
        public ListenableWorker create(Context context, WorkerParameters workerParameters) {
            return new ReminderWorker(context, workerParameters, modelProvider.get());
        }
    }

}
