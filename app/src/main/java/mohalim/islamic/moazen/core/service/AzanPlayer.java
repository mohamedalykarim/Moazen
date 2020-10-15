package mohalim.islamic.moazen.core.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import javax.inject.Inject;

import dagger.android.DaggerService;
import mohalim.islamic.moazen.core.utils.Constants;

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

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: Start Command azan player service" + mediaPlayer.getCurrentPosition());


        if (intent.hasExtra(Constants.PLAYER_POSITION)){
            Log.d(TAG, "onStartCommand: "+ intent.getIntExtra(Constants.PLAYER_POSITION,0));
            mediaPlayer.seekTo(intent.getIntExtra(Constants.PLAYER_POSITION,0));
            mediaPlayer.start();
        }

        playMedia();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Create azan player service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: Bind Service");
        return iBinder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved: Task remove in azan player service");
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

        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
    }

}


