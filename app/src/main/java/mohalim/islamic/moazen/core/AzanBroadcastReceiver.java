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

    @Inject
    PrayTime prayTime;

    MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        AndroidInjection.inject(this, context);

        int azanType = 1;
        if (intent.hasExtra(Constants.AZAN_TYPE)){
            azanType = intent.getIntExtra(Constants.AZAN_TYPE,1);
        }

        Log.d(TAG, "onReceive: AzanBroadcastReceiver");

        if (!intent.hasExtra(Constants.AZAN_RECEIVER_ORDER)) return;
        String order = intent.getStringExtra(Constants.AZAN_RECEIVER_ORDER);


        if (order.equals(Constants.AZAN_RECEIVER_ORDER_INIT_AZAN)){
            Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
            azanPlayIntent.putExtra(Constants.PLAYER_SERVICE_TYPE, Constants.PLAYER_AZAN);
            azanPlayIntent.putExtra(Constants.AZAN_TYPE, azanType);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(azanPlayIntent);
            } else {
                context.startService(azanPlayIntent);
            }

            initNotification(context, azanType);

        }else if (order.equals(Constants.AZAN_RECEIVER_ORDER_RESUME)){
            Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
            azanPlayIntent.setAction("resume");
            azanPlayIntent.putExtra(Constants.PLAYER_POSITION, intent.getIntExtra(Constants.PLAYER_POSITION,0));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(azanPlayIntent);
            } else {
                context.startService(azanPlayIntent);
            }
        }else if (order.equals(Constants.AZAN_RECEIVER_ORDER_INIT_REMINDER)){
            Log.d(TAG, "onReceive: init reminder");
            Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
            azanPlayIntent.setAction("reminder");
            azanPlayIntent.putExtra(Constants.PLAYER_SERVICE_TYPE, Constants.PLAYER_REMINDER);
            azanPlayIntent.putExtra(Constants.AZAN_TYPE, azanType);
            initReminderNotification(context, azanType);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(azanPlayIntent);
            } else {
                context.startService(azanPlayIntent);
            }
        }else if (order.equals(Constants.AZAN_RECEIVER_ORDER_STOP)){
            Intent azanPlayIntent = new Intent(context, AzanPlayer.class);
            azanPlayIntent.setAction("resume");
            azanPlayIntent.putExtra(Constants.PLAYER_SERVICE_TYPE, Constants.AZAN_RECEIVER_ORDER_STOP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(azanPlayIntent);
            } else {
                context.startService(azanPlayIntent);
            }

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.cancel(Constants.NOTIFICATION_ID);

        }




    }


    public void initNotification(Context context, int azanType){
        Intent snoozeIntent = new Intent(context, AzanBroadcastReceiver.class);
        snoozeIntent.setAction("Close");
        snoozeIntent.putExtra(Constants.AZAN_RECEIVER_ORDER, Constants.AZAN_RECEIVER_ORDER_STOP);
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


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());

    }

    public void initReminderNotification(Context context, int azanType){
        Log.d(TAG, "initReminderNotification: ");
        Intent snoozeIntent = new Intent(context, AzanBroadcastReceiver.class);
        snoozeIntent.setAction("Close");
        snoozeIntent.putExtra(Constants.AZAN_RECEIVER_ORDER, Constants.AZAN_RECEIVER_ORDER_STOP);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = null;

        Locale currentLocale = context.getResources().getConfiguration().locale;

        if (currentLocale.getLanguage().equals("en")){
            builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_masjed_icon)
                    .setContentTitle("5 Minutes till "+ Utils.getAzantTypeName(context, azanType) + " azan")
                    .setContentText("5 Minutes till " + Utils.getAzantTypeName(context, azanType) + " azan")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .addAction(R.drawable.ic_stop_play,"Stop",
                            snoozePendingIntent);
        }else if (currentLocale.getLanguage().equals("ar")){
            builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_masjed_icon)
                    .setContentTitle("باقي 5 دقائق علي اذان  "+ Utils.getAzantTypeName(context, azanType))
                    .setContentText("باقي 5 دقائق علي اذان  "+ Utils.getAzantTypeName(context, azanType))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .addAction(R.drawable.ic_stop_play,"Stop",
                            snoozePendingIntent);
        }


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());

    }
}
