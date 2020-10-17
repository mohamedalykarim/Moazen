package mohalim.islamic.moazen.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;


import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.di.base.BaseActivity;
import mohalim.islamic.moazen.core.service.AzanTimesWorker;
import mohalim.islamic.moazen.core.utils.AppDateUtil;
import mohalim.islamic.moazen.core.utils.AppSettingHelper;
import mohalim.islamic.moazen.core.utils.Constants;
import mohalim.islamic.moazen.core.viewmodel.ViewModelProviderFactory;
import mohalim.islamic.moazen.databinding.ActivityMainBinding;
import mohalim.islamic.moazen.ui.bottoms.PrayerTimesBottom;
import mohalim.islamic.moazen.ui.setting.SettingActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = "mohalim";

    private static final int NEXT_AZAN_FAGR = 1;
    private static final int NEXT_AZAN_SHOROK = 2;
    private static final int NEXT_AZAN_ZOHR = 3;
    private static final int NEXT_AZAN_ASR = 4;
    private static final int NEXT_AZAN_MAGHREB = 5;
    private static final int NEXT_AZAN_ESHAA = 6;

    ActivityMainBinding binding;

    private int nextAzan = 1;

    private WorkManager manager;
    private String eshaa;
    private String maghreb;
    private String asr;
    private String zohr;
    private String shoroq;
    private String fagr;


    MainViewModel mViewModel;

    @Inject
    ViewModelProviderFactory factory;

    @Inject
    String[] prayTimes;
    
    @Inject
    Calendar calendar;

    @Inject
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);

        binding.prayerTimesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrayerTimesBottom bottomSheet = new PrayerTimesBottom();
                bottomSheet.show(getSupportFragmentManager(), "");
            }
        });



        binding.settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        Data data = new Data.Builder().putStringArray("prayerTimes",prayTimes).build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                AzanTimesWorker.class,
                90,
                TimeUnit.MINUTES
        ).setInputData(data).build();



        manager = WorkManager.getInstance(this);


        manager.enqueueUniquePeriodicWork(
                "AzanTimes",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
        );

        firstTimeAppOpened();

    }

    @Override
    protected void onResume() {
        super.onResume();

        azanTimes(
                calendar.get(Calendar.DAY_OF_MONTH)+"",
                calendar.get(Calendar.MONTH)+1
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer.getCurrentPosition() > 0){
            Intent azanIntent = new Intent();
            azanIntent.setAction("mohalim.islamic.moazen.START");
            azanIntent.putExtra(Constants.AZAN_RECEIVER_ORDER, Constants.AZAN_RECEIVER_ORDER_RESUME);
            Log.d(TAG, "onDestroy: "+ mediaPlayer.getCurrentPosition());
            azanIntent.putExtra(Constants.PLAYER_POSITION, mediaPlayer.getCurrentPosition());
            sendBroadcast(azanIntent);
        }

    }

    void azanTimes(String day, int monthNumber){


            fagr = day + "-" + monthNumber
                    + "-" + calendar.get(Calendar.YEAR)
                    + ", " + prayTimes[0];



            shoroq = day + "-" + monthNumber
                    + "-" + calendar.get(Calendar.YEAR)
                    + ", " + prayTimes[1];

            zohr = day + "-" + monthNumber
                    + "-" + calendar.get(Calendar.YEAR)
                    + ", " + prayTimes[2];

            asr = day + "-" + monthNumber
                    + "-" + calendar.get(Calendar.YEAR)
                    + ", " + prayTimes[3];

            maghreb = day + "-" + monthNumber
                    + "-" + calendar.get(Calendar.YEAR)
                    + ", " + prayTimes[4];

            eshaa = day + "-" + monthNumber
                    + "-" + calendar.get(Calendar.YEAR)
                    + ", " + prayTimes[6];





            long currentTimeMillis = System.currentTimeMillis();
            long fagrTimeMillis = AppDateUtil.convertDateToMillisecond(fagr);
            long shoroqTimeMillis = AppDateUtil.convertDateToMillisecond(shoroq);
            long zohrTimeMillis = AppDateUtil.convertDateToMillisecond(zohr);
            long asrTimeMillis = AppDateUtil.convertDateToMillisecond(asr);
            long maghrebTimeMillis = AppDateUtil.convertDateToMillisecond(maghreb);
            long eshaaTimeMillis = AppDateUtil.convertDateToMillisecond(eshaa);

            if (currentTimeMillis > eshaaTimeMillis){
                int nextDay = Integer.parseInt(day) + 1 ;

                azanTimes(nextDay+"", monthNumber);
                return;
            }


            if (fagrTimeMillis > currentTimeMillis){
                nextAzan = NEXT_AZAN_FAGR;
            }else{
                if (shoroqTimeMillis > currentTimeMillis){
                    nextAzan = NEXT_AZAN_SHOROK;
                }else {
                    if (zohrTimeMillis > currentTimeMillis){
                        nextAzan = NEXT_AZAN_ZOHR;
                    }else {
                        if (asrTimeMillis > currentTimeMillis){
                            nextAzan = NEXT_AZAN_ASR;
                        }else {
                            if (maghrebTimeMillis > currentTimeMillis){
                                nextAzan = NEXT_AZAN_MAGHREB;
                            }else {
                                if (eshaaTimeMillis > currentTimeMillis){
                                    nextAzan = NEXT_AZAN_ESHAA;
                                }
                            }

                        }
                    }
                }
            }


            long delayToAzan = 0;

            if (nextAzan == NEXT_AZAN_FAGR ){
                delayToAzan =  fagrTimeMillis - currentTimeMillis;
            }else if (nextAzan == NEXT_AZAN_SHOROK ){
                delayToAzan =  shoroqTimeMillis - currentTimeMillis;
            }else if (nextAzan == NEXT_AZAN_ZOHR ){
                delayToAzan =  zohrTimeMillis - currentTimeMillis;
            }else if (nextAzan == NEXT_AZAN_ASR ){
                delayToAzan =  asrTimeMillis - currentTimeMillis;
            }else if (nextAzan == NEXT_AZAN_MAGHREB ){
                delayToAzan =  maghrebTimeMillis - currentTimeMillis;
            }else if (nextAzan == NEXT_AZAN_ESHAA ){
                delayToAzan =  eshaaTimeMillis - currentTimeMillis;
            }


            long finalDelayToAzan = delayToAzan;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    CountDownTimer countDownTimer = new CountDownTimer(finalDelayToAzan,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String remainTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));





                            binding.counterTV.setText(remainTime);

                        }

                        @Override
                        public void onFinish() {
                            binding.counterTV.setText("00:00:00");

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                if (Integer.parseInt(day) > calendar.get(Calendar.DAY_OF_MONTH)){
                                    azanTimes(day+"", monthNumber);
                                }else {
                                    int nextDay = Integer.parseInt(day) + 1 ;
                                    azanTimes(nextDay+"", monthNumber);

                                }



                            }

                        }
                    };

                    countDownTimer.start();


                    if (nextAzan == NEXT_AZAN_FAGR){
                        binding.remainsTitleTv.setText("Reminder for next prayer Al Fagr : ");
                    }else if (nextAzan == NEXT_AZAN_SHOROK){
                        binding.remainsTitleTv.setText("Reminder for next prayer Al Shoroq : ");
                    }else if (nextAzan == NEXT_AZAN_ZOHR){
                        binding.remainsTitleTv.setText("Reminder for next prayer Al Zohr : ");
                    }else if (nextAzan == NEXT_AZAN_ASR){
                        binding.remainsTitleTv.setText("Reminder for next prayer Al Asr : ");
                    }else if (nextAzan == NEXT_AZAN_MAGHREB){
                        binding.remainsTitleTv.setText("Reminder for next prayer Al Maghreb : ");
                    }else if (nextAzan == NEXT_AZAN_ESHAA){
                        binding.remainsTitleTv.setText("Reminder for next prayer Al Eshaa :");
                    }
                }
            });




    }


    void firstTimeAppOpened(){
        if (!AppSettingHelper.getIsFirstTimeAppOpened(this)) return;

        AppSettingHelper.setIsFirstTimeAppOpened(this, false);
    }



}
