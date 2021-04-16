package mohalim.islamic.alarm.alert.moazen.core.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;


import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.DaggerService;
import mohalim.islamic.alarm.alert.moazen.R;
import mohalim.islamic.alarm.alert.moazen.core.AzanBroadcastReceiver;
import mohalim.islamic.alarm.alert.moazen.core.utils.AppPrefsHelper;
import mohalim.islamic.alarm.alert.moazen.core.utils.Constants;
import mohalim.islamic.alarm.alert.moazen.core.utils.PrayTime;
import mohalim.islamic.alarm.alert.moazen.core.utils.Utils;

public class AzanPlayer extends DaggerService implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener{

    private final String TAG = "AzanPlayer";


    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();

    @Inject
    MediaPlayer mediaPlayer;

    @Inject
    PrayTime prayTime;

    @Inject
    String[] prayTimes;

    private int resumePosition;

    private View mFloatingView;
    private WindowManager mWindowManager;
    private WorkManager manager;

    NotificationManager notificationManager;
    AudioManager audioManager;


    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                if (mediaPlayer == null)return;
                if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) >0){
                    //Incoming call: Pause music
                    mediaPlayer.setVolume(0,0);
                }

            } else if(state == TelephonyManager.CALL_STATE_IDLE) {
                //Not in call: Play music
                if (mediaPlayer == null)return;
                if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0){
                    mediaPlayer.setVolume(1,1);
                }

            } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                //A call is dialing, active or on hold
                mediaPlayer.setVolume(0,0);
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };


    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) return Service.START_NOT_STICKY;
        if (intent.getAction() == null) return Service.START_NOT_STICKY;

        updateAzanTimes();

        String action = intent.getAction();
        int azanType = intent.getIntExtra(Constants.AZAN_TYPE, 1);

        switch (action) {
            // Reminder
            case Constants.PLAYER_REMINDER:
                changeMedia(R.raw.reminder);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        initReminderNotification(getApplicationContext(), azanType);
                    }
                });
                Log.d(TAG, "onStartCommand: PLAYER_REMINDER");

            break;
            // Azan
            case Constants.PLAYER_AZAN:
                changeMedia(R.raw.quds);

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        initNotification(getApplicationContext(), azanType);
                    }
                });


                if (Build.VERSION.SDK_INT >= 23) {
                    if (Settings.canDrawOverlays(getApplicationContext())) {
                        startWidget(azanType);
                    }
                }
                Log.d(TAG, "onStartCommand: PLAYER_AZAN");

            break;
            // Stop
            case Constants.AZAN_RECEIVER_ORDER_STOP:
                notificationManager.cancel(Constants.NOTIFICATION_ID);
                this.stopSelf();
                Log.d(TAG, "onStartCommand: AZAN_RECEIVER_ORDER_STOP");
                break;
                // Resume
            case Constants.AZAN_RECEIVER_ORDER_RESUME:
                if (intent.hasExtra(Constants.PLAYER_POSITION)) {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.seekTo(intent.getIntExtra(Constants.PLAYER_POSITION, 0));
                            mp.start();
                        }
                    });
                }

                Log.d(TAG, "onStartCommand: AZAN_RECEIVER_ORDER_STOP");
                break;
        }

        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if(mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }



        return Service.START_NOT_STICKY;
    }

    private void startWidget(int azanType) {
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        final WindowManager.LayoutParams params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }else{
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);
        final ImageView leftImage = mFloatingView.findViewById(R.id.leftImage);
        final ImageView rightImage = mFloatingView.findViewById(R.id.rightImage);
        final ImageView azanNowImg = mFloatingView.findViewById(R.id.azanNowImg);
        final Button endAzanBtn = mFloatingView.findViewById(R.id.endAzanBtn);

        switch (azanType) {
            case Constants.AZAN_FUGR:
                azanNowImg.setImageResource(R.drawable.widget_azan_fugr);
                break;
            case Constants.AZAN_ZUHR:
                azanNowImg.setImageResource(R.drawable.widget_azan_zuhr);
                break;
            case Constants.AZAN_ASR:
                azanNowImg.setImageResource(R.drawable.widget_azan_asr);
                break;
            case Constants.AZAN_MAGHREB:
                azanNowImg.setImageResource(R.drawable.widget_azan_maghreb);
                break;
            case Constants.AZAN_ESHAA:
                azanNowImg.setImageResource(R.drawable.widget_azan_eshaa);
                break;
        }

        endAzanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        if (Locale.getDefault().getLanguage().equals("ar")){
            FrameLayout.LayoutParams leftParamas =new FrameLayout.LayoutParams(
                    (int) Utils.convertDpToPixel(50, getApplicationContext()),
                    (int) Utils.convertDpToPixel(100, getApplicationContext())
            );

            leftParamas.gravity=Gravity.END;
            leftImage.setLayoutParams(leftParamas);
            leftImage.setScaleType(ImageView.ScaleType.FIT_END);


            FrameLayout.LayoutParams rightParamas =new FrameLayout.LayoutParams(
                    (int) Utils.convertDpToPixel(50, getApplicationContext()),
                    (int) Utils.convertDpToPixel(100, getApplicationContext())
            );

            rightParamas.gravity=Gravity.START;
            rightImage.setLayoutParams(rightParamas);
            rightImage.setScaleType(ImageView.ScaleType.FIT_START);
        }

        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    private void changeMedia(int file) {
        AssetFileDescriptor assetFileDescriptor = getResources().openRawResourceFd(file);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            Log.d(TAG, "onStartCommand: "+ e.getMessage());
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

    }

    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Invoked when playback of a media source has completed.
        stopMedia();
        //stop the service
        stopSelf();
    }

    //Handle errors
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        playMedia();
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info.
        return false;
    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.
    }

    public class LocalBinder extends Binder {
        public AzanPlayer getService() {
            return AzanPlayer.this;
        }
    }


    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    private void resumeMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.seekTo(resumePosition);
                    mediaPlayer.start();

                }
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);

        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
    }


    public void initNotification(Context context, int azanType){
        Intent snoozeIntent = new Intent(context, AzanBroadcastReceiver.class);
        snoozeIntent.setAction(Constants.AZAN_RECEIVER_ORDER_STOP);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = null;

        Locale currentLocale = context.getResources().getConfiguration().locale;

        String [] timesOfToday = Utils.getAzanTimes(context, Calendar.getInstance().getTimeInMillis(), prayTime);
        String [] timesOfTomorrow = Utils.getAzanTimes(context, Calendar.getInstance().getTimeInMillis()+24*60*60*1000, prayTime);

        String remainTime = Utils.getRemainTime(azanType, timesOfToday, timesOfTomorrow);

        if (currentLocale.getLanguage().equals("en")){
            builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_masjed_icon)
                    .setContentTitle(Utils.getAzantTypeName(context, azanType) + " azan is now")
                    .setContentText(Utils.getAzantTypeName(context, azanType) + " azan is now, " + remainTime +" till "+ Utils.getNextAzantTypeName(context, azanType) + " Azan")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .addAction(R.drawable.ic_stop_play,"Stop",
                            snoozePendingIntent);
        }else if (currentLocale.getLanguage().equals("ar")){
            builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_masjed_icon)
                    .setContentTitle("حان الان موعد اذان "+ Utils.getAzantTypeName(context, azanType))
                    .setContentText("حان الان موعد اذان "+Utils.getAzantTypeName(context, azanType)+" متبيق على اذان "+Utils.getNextAzantTypeName(context, azanType) +" "+ remainTime)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .addAction(R.drawable.ic_stop_play,"Stop",
                            snoozePendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constants.NOTIFICATION_CHANNEL_ID;
            String description = Constants.NOTIFICATION_CHANNEL_ID;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Don't see these lines in your code...
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            startForeground(Constants.NOTIFICATION_ID, builder.build());

        }else{
            startForeground(Constants.NOTIFICATION_ID, builder.build());
        }
    }

    public void initReminderNotification(Context context, int azanType){
        Log.d(TAG, "initReminderNotification: ");
        Intent snoozeIntent = new Intent(context, AzanBroadcastReceiver.class);
        snoozeIntent.setAction(Constants.AZAN_RECEIVER_ORDER_STOP);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = null;

        Locale currentLocale = context.getResources().getConfiguration().locale;

        int timeBefore = AppPrefsHelper.getReminderTime(getApplicationContext(), 10);

        if (currentLocale.getLanguage().equals("en")){
            builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_masjed_icon)
                    .setContentTitle(timeBefore + " Minutes till "+ Utils.getAzantTypeName(context, azanType) + " azan")
                    .setContentText(timeBefore +" Minutes till " + Utils.getAzantTypeName(context, azanType) + " azan")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .addAction(R.drawable.ic_stop_play,"Stop",
                            snoozePendingIntent);
        }else if (currentLocale.getLanguage().equals("ar")){
            builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_masjed_icon)
                    .setContentTitle("باقي "+timeBefore+" دقائق علي اذان  "+ Utils.getAzantTypeName(context, azanType))
                    .setContentText("باقي "+timeBefore+" دقائق علي اذان  "+ Utils.getAzantTypeName(context, azanType))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .addAction(R.drawable.ic_stop_play,"Stop",
                            snoozePendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constants.NOTIFICATION_CHANNEL_ID;
            String description = Constants.NOTIFICATION_CHANNEL_ID;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            startForeground(Constants.NOTIFICATION_ID, builder.build());

        }else{
            startForeground(Constants.NOTIFICATION_ID, builder.build());
        }




    }

    private void updateAzanTimes() {
        Data data = new Data.Builder().putStringArray("prayerTimes",prayTimes).build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                AzanTimesWorker.class,
                60,
                TimeUnit.MINUTES
        ).setInputData(data).build();

        manager = WorkManager.getInstance(this);

        manager.enqueueUniquePeriodicWork(
                "AzanTimes",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
        );
    }



}


