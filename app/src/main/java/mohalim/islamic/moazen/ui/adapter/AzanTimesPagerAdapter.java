package mohalim.islamic.moazen.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import mohalim.islamic.moazen.ui.fragments.AzanTimesFragmet;

public class AzanTimesPagerAdapter extends FragmentPagerAdapter {
    int numberOfDays;


    public AzanTimesPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        AzanTimesFragmet fragment = new AzanTimesFragmet();
        fragment.setPosition(position);
        return fragment;
    }


    @Override
    public int getCount() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}
