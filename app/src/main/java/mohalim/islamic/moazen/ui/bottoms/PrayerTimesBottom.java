package mohalim.islamic.moazen.ui.bottoms;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import mohalim.islamic.moazen.databinding.AzanTimesBottomBinding;
import mohalim.islamic.moazen.ui.adapter.AzanTimesPagerAdapter;

public class PrayerTimesBottom extends BottomSheetDialogFragment {
    AzanTimesPagerAdapter pagerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AzanTimesBottomBinding binding = AzanTimesBottomBinding.inflate(inflater, container, false);


        pagerAdapter = new AzanTimesPagerAdapter(
                this.getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );


        int days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR);
        pagerAdapter.setNumberOfDays(days);


        binding.azanTimesViewPager.setAdapter(pagerAdapter);

        binding.azanTimesViewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)-1);

        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog=(BottomSheetDialog)
                super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dia) {
                BottomSheetDialog dialog = (BottomSheetDialog) dia;
                FrameLayout bottomSheet =  dialog .findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
            }
        });
        return bottomSheetDialog;
    }
}
