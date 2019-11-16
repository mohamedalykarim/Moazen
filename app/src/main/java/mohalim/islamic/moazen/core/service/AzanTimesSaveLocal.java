package mohalim.islamic.moazen.core.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

import mohalim.islamic.moazen.core.database.AzanTimesDao;
import mohalim.islamic.moazen.core.database.AzanTimesDatabase;
import mohalim.islamic.moazen.core.model.AzanTimesItem;
import mohalim.islamic.moazen.core.utils.AppExecutor;

public class AzanTimesSaveLocal extends Worker {

    public AzanTimesSaveLocal(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final AzanTimesDatabase database = AzanTimesDatabase.getDatabase(getApplicationContext());
        final AzanTimesDao azanTimesDao = database.azanTimesDao();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        FirebaseDatabase.getInstance().getReference()
                .child("CityLocation")
                .child("Luxor")
                .child("AzanTimes")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists())return;

                        AzanTimesItem azanTimesItem = new AzanTimesItem();

                        for (DataSnapshot monthSnapshot : dataSnapshot.getChildren()){
                            for (DataSnapshot daySnapshot : monthSnapshot.getChildren()){



                                AppExecutor.getInstance().diskIO().execute(()->{
                                    azanTimesItem.setCity("Luxor");
                                    azanTimesItem.setMonth(monthSnapshot.getKey());
                                    azanTimesItem.setDay(daySnapshot.getKey());
                                    azanTimesItem.setFagr(daySnapshot.child("fagr").getValue().toString());
                                    azanTimesItem.setShoroq(daySnapshot.child("shoroq").getValue().toString());
                                    azanTimesItem.setZohr(daySnapshot.child("zohr").getValue().toString());
                                    azanTimesItem.setAsr(daySnapshot.child("asr").getValue().toString());
                                    azanTimesItem.setMaghreb(daySnapshot.child("maghreb").getValue().toString());
                                    azanTimesItem.setEshaa(daySnapshot.child("eshaa").getValue().toString());

                                    long is = azanTimesDao.insert(azanTimesItem);
                                    Log.v("Day : "+ daySnapshot.getKey(), " Month : "+ monthSnapshot.getKey()+ " inserted:  "+ is);

                                });


                            }


                        }

                        countDownLatch.countDown();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        try {
            countDownLatch.await();
            return Result.success();
        } catch (InterruptedException e) {
            return Result.success();
        }



    }


}
