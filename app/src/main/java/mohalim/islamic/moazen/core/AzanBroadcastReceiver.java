package mohalim.islamic.moazen.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.service.AzanPlayer;
import mohalim.islamic.moazen.core.utils.Constants;

public class AzanBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "AzanBroadcastReceiver";

    MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: AzanBroadcastReceiver");

        Intent azanPlayIntent = new Intent(context, AzanPlayer.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(azanPlayIntent);
        } else {
            context.startService(azanPlayIntent);
        }
        context.startService(azanPlayIntent);

    }
}
