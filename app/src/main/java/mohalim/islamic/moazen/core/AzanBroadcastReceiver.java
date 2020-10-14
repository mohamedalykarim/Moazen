package mohalim.islamic.moazen.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.utils.Constants;

public class AzanBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "AzanBroadcastReceiver";

    MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String order = intent.getExtras().getString(Constants.AZAN_RECEIVER_ORDER);
        Log.d(TAG, "onReceive: "+ order);
        if (order.equals(Constants.AZAN_RECEIVER_ORDER_INIT)){
            mediaPlayer = MediaPlayer.create(context, R.raw.quds);
            mediaPlayer.start();
        }
    }
}
