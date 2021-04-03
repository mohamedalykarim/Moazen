package mohalim.islamic.alarm.alert.moazen.ui.update_times;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.android.support.DaggerFragment;
import mohalim.islamic.alarm.alert.moazen.R;

public class FragmentUpdateTimes extends DaggerFragment {

    private UpdateTimesViewModel mViewModel;

    public static FragmentUpdateTimes newInstance() {
        return new FragmentUpdateTimes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_times, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UpdateTimesViewModel.class);
        // TODO: Use the ViewModel
    }

}