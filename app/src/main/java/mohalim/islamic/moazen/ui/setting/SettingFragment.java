package mohalim.islamic.moazen.ui.setting;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.utils.AppExecutor;
import mohalim.islamic.moazen.core.utils.AppSettingHelper;
import mohalim.islamic.moazen.core.utils.PrayTime;
import mohalim.islamic.moazen.core.viewmodel.ViewModelProviderFactory;
import mohalim.islamic.moazen.databinding.FragmentSettingBinding;

import static android.content.Context.LOCATION_SERVICE;


public class SettingFragment extends DaggerFragment {
    private static final String TAG = "SettingFragment";

    FragmentSettingBinding binding;

    private SettingViewModel mViewModel;
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    AppExecutor appExecutor;

    @Inject
    PrayTime prayTime;

    private LocationManager mLocationManager;
    private Location location;


    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        binding.cityTv.setText(AppSettingHelper.getLocationName(getActivity()));


        int calMethod = AppSettingHelper.getAzanCalculationMethod(getActivity(), prayTime.Karachi);
        String calMethodName = "";
        if (calMethod == prayTime.Karachi){
            calMethodName = getResources().getString(R.string.university_of_islamic_sciences_karachi);
        }else if (calMethod == prayTime.ISNA){
            calMethodName = getResources().getString(R.string.islamic_society_of_north_america_isna);
        }else if (calMethod == prayTime.MWL){
            calMethodName = getResources().getString(R.string.muslim_world_league_mwl);
        }else if (calMethod == prayTime.Makkah){
            calMethodName = getResources().getString(R.string.umm_al_qura_makkah);
        }else if (calMethod == prayTime.Egypt){
            calMethodName = getResources().getString(R.string.egyptian_general_authority_of_survey);
        }else if (calMethod == prayTime.Tehran){
            calMethodName = getResources().getString(R.string.institute_of_geophysics_university_of_tehran);
        }

        binding.calculationTypeTv.setText(calMethodName);
        binding.latAndLongAndTimezone.setText(
                "Latitude : " + AppSettingHelper.getLatitude(getActivity()) + "\n"
                        + "Longitude : " + AppSettingHelper.getLongitude(getActivity()) + "\n"
                        + "Timezone : " +AppSettingHelper.getTimeZone(getActivity())
        );

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(SettingViewModel.class);


        binding.locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationInit();
                binding.loading.setVisibility(View.VISIBLE);
            }
        });

        binding.calTypeEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calTypeEditBtn.setVisibility(View.GONE);
                binding.calTypeBtnsContainer.setVisibility(View.VISIBLE);
            }
        });

        binding.calType1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calTypeEditBtn.setVisibility(View.VISIBLE);
                binding.calTypeBtnsContainer.setVisibility(View.GONE);
                binding.calTypeBtnsContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                AppSettingHelper.setAzanCalculationMethod(getContext(), prayTime.Karachi);
                binding.calculationTypeTv.setText(getResources().getString(R.string.university_of_islamic_sciences_karachi));
            }
        });



        binding.calType2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calTypeEditBtn.setVisibility(View.VISIBLE);
                binding.calTypeBtnsContainer.setVisibility(View.GONE);
                AppSettingHelper.setAzanCalculationMethod(getContext(), prayTime.ISNA);
                binding.calculationTypeTv.setText(getResources().getString(R.string.islamic_society_of_north_america_isna));


            }
        });

        binding.calType3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calTypeEditBtn.setVisibility(View.VISIBLE);
                binding.calTypeBtnsContainer.setVisibility(View.GONE);
                AppSettingHelper.setAzanCalculationMethod(getContext(), prayTime.MWL);
                binding.calculationTypeTv.setText(getResources().getString(R.string.muslim_world_league_mwl));


            }
        });

        binding.calType4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calTypeEditBtn.setVisibility(View.VISIBLE);
                binding.calTypeBtnsContainer.setVisibility(View.GONE);
                AppSettingHelper.setAzanCalculationMethod(getContext(), prayTime.Makkah);
                binding.calculationTypeTv.setText(getResources().getString(R.string.umm_al_qura_makkah));


            }
        });

        binding.calType5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calTypeEditBtn.setVisibility(View.VISIBLE);
                binding.calTypeBtnsContainer.setVisibility(View.GONE);
                AppSettingHelper.setAzanCalculationMethod(getContext(), prayTime.Egypt);
                binding.calculationTypeTv.setText(getResources().getString(R.string.egyptian_general_authority_of_survey));

            }
        });

        binding.calType6Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calTypeEditBtn.setVisibility(View.VISIBLE);
                binding.calTypeBtnsContainer.setVisibility(View.GONE);
                AppSettingHelper.setAzanCalculationMethod(getContext(), prayTime.Tehran);
                binding.calculationTypeTv.setText(getResources().getString(R.string.institute_of_geophysics_university_of_tehran));


            }
        });

    }

    public void locationInit() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                List<Address> addresses = getAddresses(location.getLatitude(), location.getLongitude());
                if (addresses == null)return;
                if (addresses.size() == 0 )return;

//                IConverter iconv = Converter.getInstance(TimeZoneListStore.class);
//                TimeZone tz = iconv.getTimeZone(location.getLatitude(), location.getLongitude());
//
                TimeZone tz = TimeZone.getDefault();


                binding.cityTv.setText(addresses.get(0).getAddressLine(0));
                binding.latAndLongAndTimezone.setText(
                        "Latitude : " + location.getLatitude() + "\n"
                        + "Longitude : " + location.getLongitude() + "\n"
                        + "Timezone : " +tz.getRawOffset()/1000/60/60
                );
                AppSettingHelper.setLocationName(getActivity(), addresses.get(0).getAddressLine(0));
                AppSettingHelper.setLatitude(getActivity(),String.valueOf(location.getLatitude()));
                AppSettingHelper.setLongitude(getActivity(),String.valueOf(location.getLongitude()));
                AppSettingHelper.setTimeZone(getActivity(),String.valueOf(tz.getRawOffset()/1000/60/60));

                binding.loading.setVisibility(View.GONE);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }else {



            if(!isGPSEnabled() && !isNetworkEnabled()) {
                // notify user
                new AlertDialog.Builder(getActivity())
                        .setMessage("Sorry GPS is not enabled")
                        .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
            }


            mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener,null);
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }






    }

    private boolean isGPSEnabled(){
        try {
            return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {
            return false;
        }
    }

    private boolean isNetworkEnabled(){
        try {
            return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {
            return false;
        }
    }

    public List<Address> getAddresses(double myLat, double myLong){
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(myLat, myLong, 1);

            return addresses;
        }catch (Exception e){
            return null;
        }
    }

    private String getTimeZone(String countryName){
        return "" + ((TimeZone.getTimeZone(countryName).getRawOffset())/1000/60/60);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationInit();

                } else {
                    Toast.makeText(getActivity(), "You have to grant location permission to determine your location", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


}
