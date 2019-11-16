package mohalim.islamic.moazen.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mohalim.islamic.moazen.core.database.AzanTimesDao;
import mohalim.islamic.moazen.core.database.AzanTimesDatabase;
import mohalim.islamic.moazen.core.model.AzanTimesItem;
import mohalim.islamic.moazen.core.utils.AppExecutor;
import mohalim.islamic.moazen.core.utils.AppSettingHelper;
import mohalim.islamic.moazen.core.utils.PrayTime;
import mohalim.islamic.moazen.databinding.AzanTimesFragmentBinding;

public class AzanTimesFragmet extends DaggerFragment {
    @Inject
    PrayTime prayers;



    int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AzanTimesFragmentBinding binding = AzanTimesFragmentBinding.inflate(inflater, container, false);

        double latitude = Double.parseDouble(AppSettingHelper.getLatitude(getActivity()));
        double longitude = Double.parseDouble(AppSettingHelper.getLongitude(getActivity()));;
        double timezone = Double.parseDouble(AppSettingHelper.getTimeZone(getActivity()));;

        // Test Prayer times here

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, position+1);

        binding.dateTv.setText(
                cal.get(Calendar.DAY_OF_MONTH)
                +"-"+
                (cal.get(Calendar.MONTH)+1)
                +"-"+
                cal.get(Calendar.YEAR)

        );

        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
                latitude, longitude, timezone);

        binding.fagrTimeTv.setText(prayerTimes.get(0));
        binding.shoroqTimeTv.setText(prayerTimes.get(1));
        binding.zohrTimeTv.setText(prayerTimes.get(2));
        binding.asrTimeTv.setText(prayerTimes.get(3));
        binding.maghrebTimeTv.setText(prayerTimes.get(4));
        binding.eshaaTimeTv.setText(prayerTimes.get(6));



        return binding.getRoot();
    }



    public void setPosition(int position) {
        this.position = position;
    }
}
