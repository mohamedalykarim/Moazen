package mohalim.islamic.moazen.core;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DaggerBroadcastReceiver;
import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.di.base.BaseApplication;
import mohalim.islamic.moazen.core.service.AzanPlayer;
import mohalim.islamic.moazen.core.utils.Constants;
import mohalim.islamic.moazen.core.utils.PrayTime;
import mohalim.islamic.moazen.core.utils.Utils;

public class AzanBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "AzanBroadcastReceiver";



    MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        AndroidInjection.inject(this, context);

        if (intent == null) return;
        if (intent.getAction() == null) return;

        int azanType = 1;
        if (intent.hasExtra(Constants.AZAN_TYPE)){
            azanType = intent.getIntExtra(Constants.AZAN_TYPE,1);
        }


        String action = intent.getAction();

        Log.d(TAG, "onReceive: action "+ action);
        Log.d(TAG, "onReceive: azanType "+ azanType);

        switch (action) {
            case Constants.AZAN_RECEIVER_ORDER_INIT_AZAN: {
                Log.d(TAG, "onReceive: AZAN_RECEIVER_ORDER_INIT_AZAN");
                Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
                azanPlayIntent.setAction(Constants.PLAYER_AZAN);
                azanPlayIntent.putExtra(Constants.AZAN_TYPE, azanType);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(azanPlayIntent);
                } else {
                    context.startService(azanPlayIntent);
                }
                break;
            }

            case Constants.AZAN_RECEIVER_ORDER_INIT_REMINDER: {
                Log.d(TAG, "onReceive: AZAN_RECEIVER_ORDER_INIT_REMINDER");
                Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
                azanPlayIntent.setAction(Constants.PLAYER_REMINDER);
                azanPlayIntent.putExtra(Constants.AZAN_TYPE, azanType);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(azanPlayIntent);
                } else {
                    context.startService(azanPlayIntent);
                }
                break;
            }

            case Constants.AZAN_RECEIVER_ORDER_RESUME: {
                Log.d(TAG, "onReceive: AZAN_RECEIVER_ORDER_RESUME");
                Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
                azanPlayIntent.setAction(Constants.AZAN_RECEIVER_ORDER_RESUME);
                azanPlayIntent.putExtra(Constants.PLAYER_POSITION, intent.getIntExtra(Constants.PLAYER_POSITION, 0));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(azanPlayIntent);
                } else {
                    context.startService(azanPlayIntent);
                }
                break;
            }

            case Constants.AZAN_RECEIVER_ORDER_STOP: {
                Log.d(TAG, "onReceive: AZAN_RECEIVER_ORDER_STOP");
                Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
                azanPlayIntent.setAction(Constants.AZAN_RECEIVER_ORDER_STOP);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(azanPlayIntent);
                } else {
                    context.startService(azanPlayIntent);
                }

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.cancel(Constants.NOTIFICATION_ID);

                break;
            }
        }




    }


}
