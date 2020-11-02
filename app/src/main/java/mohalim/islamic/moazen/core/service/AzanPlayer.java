package mohalim.islamic.moazen.core.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.DaggerService;
import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.utils.Constants;
import mohalim.islamic.moazen.core.utils.Utils;

public class AzanPlayer extends DaggerService implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener{

    private final String TAG = "AzanPlayer";


    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();

    @Inject
    MediaPlayer mediaPlayer;

    private int resumePosition;
    private AudioManager audioManager;

    private View mFloatingView;
    private WindowManager mWindowManager;

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return Service.START_NOT_STICKY;
        if (intent.getAction() == null) return Service.START_NOT_STICKY;

        String action = intent.getAction();

        if (action.equals(Constants.PLAYER_REMINDER)){
            changeMedia(R.raw.reminder);
            mediaPlayer.start();
        } else if (action.equals(Constants.PLAYER_AZAN)){
            int azanType = intent.getIntExtra(Constants.AZAN_TYPE,1);
            changeMedia(R.raw.quds);
            mediaPlayer.start();
            startWidget(azanType);

        }else if (action.equals(Constants.AZAN_RECEIVER_ORDER_STOP)){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(0);
                mediaPlayer.stop();
            }
        }else if (action.equals(Constants.AZAN_RECEIVER_ORDER_RESUME)){
            if (intent.hasExtra(Constants.PLAYER_POSITION)){
                mediaPlayer.seekTo(intent.getIntExtra(Constants.PLAYER_POSITION,0));
                mediaPlayer.start();
            }
        }


        return Service.START_NOT_STICKY;
    }

    private void startWidget(int azanType) {
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

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
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
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

}


