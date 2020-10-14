package mohalim.islamic.moazen.core.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


import dagger.android.DaggerService;
import mohalim.islamic.moazen.core.utils.Constants;

public class AzanService extends DaggerService {
    private final String TAG = "AzanService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent azanIntent = new Intent();
        azanIntent.setAction("mohalim.islamic.moazen.START");
        azanIntent.putExtra(Constants.AZAN_RECEIVER_ORDER, Constants.AZAN_RECEIVER_ORDER_INIT);
        sendBroadcast(azanIntent);
        Log.d(TAG, "onStartCommand: from service send to broadcast receiver");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




}
