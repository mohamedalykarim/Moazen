package mohalim.islamic.moazen.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mohalim.islamic.moazen.core.database.AzanTimesDao;
import mohalim.islamic.moazen.core.database.AzanTimesDatabase;
import mohalim.islamic.moazen.core.model.AzanTimesItem;
import mohalim.islamic.moazen.core.utils.AppExecutor;
import mohalim.islamic.moazen.databinding.AzanTimesFragmentBinding;

public class AzanTimesFragmet extends Fragment {
    int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AzanTimesFragmentBinding binding = AzanTimesFragmentBinding.inflate(inflater, container, false);

        Calendar calendar = Calendar.getInstance();

        int day = position+1;
        int monthNumber = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        String month = simpleDateFormat.format(calendar.getTime());


        binding.dateTv.setText(day+"/"+monthNumber+"/"+year);


        AppExecutor.getInstance().diskIO().execute(()->{
            AzanTimesDatabase database = AzanTimesDatabase.getDatabase(getContext());
            AzanTimesDao azanTimesDao = database.azanTimesDao();

            AzanTimesItem azanTimesItem = azanTimesDao.getAzanTimesForday("Luxor", month+"", day+"");
            binding.fagrTimeTv.setText(azanTimesItem.getFagr());
            binding.shoroqTimeTv.setText(azanTimesItem.getShoroq());
            binding.zohrTimeTv.setText(azanTimesItem.getZohr());
            binding.asrTimeTv.setText(azanTimesItem.getAsr());
            binding.maghrebTimeTv.setText(azanTimesItem.getMaghreb());
            binding.eshaaTimeTv.setText(azanTimesItem.getEshaa());

        });


        return binding.getRoot();
    }



    public void setPosition(int position) {
        this.position = position;
    }
}
