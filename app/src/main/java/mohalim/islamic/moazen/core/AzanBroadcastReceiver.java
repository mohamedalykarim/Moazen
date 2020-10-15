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

        if (!intent.hasExtra(Constants.AZAN_RECEIVER_ORDER)) return;
        String order = intent.getStringExtra(Constants.AZAN_RECEIVER_ORDER);


        if (order.equals(Constants.AZAN_RECEIVER_ORDER_INIT)){
            Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(azanPlayIntent);
            } else {
                context.startService(azanPlayIntent);
            }
        }else if (order.equals(Constants.AZAN_RECEIVER_ORDER_RESUME)){
            Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
            azanPlayIntent.setAction("resume");
            azanPlayIntent.putExtra(Constants.PLAYER_POSITION, intent.getIntExtra(Constants.PLAYER_POSITION,0));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(azanPlayIntent);
            } else {
                context.startService(azanPlayIntent);
            }
        }



    }
}
