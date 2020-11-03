package mohalim.islamic.moazen.ui.setting;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import mohalim.islamic.moazen.R;
import mohalim.islamic.moazen.core.service.AzanTimesWorker;
import mohalim.islamic.moazen.core.utils.AppExecutor;
import mohalim.islamic.moazen.core.utils.AppSettingHelper;
import mohalim.islamic.moazen.core.utils.PrayTime;
import mohalim.islamic.moazen.core.viewmodel.ViewModelProviderFactory;
import mohalim.islamic.moazen.databinding.FragmentSettingBinding;


public class SettingFragment extends DaggerFragment {
    private static final String TAG = "SettingFragment";

    FragmentSettingBinding binding;
    private WorkManager manager;


    private SettingViewModel mViewModel;
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    AppExecutor appExecutor;

    @Inject
    PrayTime prayTime;

    @Inject
    String[] prayTimes;

    private boolean goToStartGPS = false;


    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;


    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(SettingViewModel.class);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d(TAG, "onLocationResult: null");
                    return;
                }

                int i = 0;
                for (Location location : locationResult.getLocations()) {
                    if (i == 0) {
                        updateLocation(location);
                    }
                    i++;
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                Log.d(TAG, "onLocationAvailability: " + locationAvailability.isLocationAvailable());
                if (!locationAvailability.isLocationAvailable()) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            updateLocation(location);
                        }
                    });
                }
            }
        };


        binding.cityTv.setText(AppSettingHelper.getLocationName(getActivity()));



        int calMethod = AppSettingHelper.getAzanCalculationMethod(getActivity(), prayTime.Karachi);
        String calMethodName = "";
        if (calMethod == prayTime.Karachi) {
            calMethodName = getResources().getString(R.string.university_of_islamic_sciences_karachi);
        } else if (calMethod == prayTime.ISNA) {
            calMethodName = getResources().getString(R.string.islamic_society_of_north_america_isna);
        } else if (calMethod == prayTime.MWL) {
            calMethodName = getResources().getString(R.string.muslim_world_league_mwl);
        } else if (calMethod == prayTime.Makkah) {
            calMethodName = getResources().getString(R.string.umm_al_qura_makkah);
        } else if (calMethod == prayTime.Egypt) {
            calMethodName = getResources().getString(R.string.egyptian_general_authority_of_survey);
        } else if (calMethod == prayTime.Tehran) {
            calMethodName = getResources().getString(R.string.institute_of_geophysics_university_of_tehran);
        }

        binding.calculationTypeTv.setText(calMethodName);
        binding.latAndLongAndTimezone.setText(
                "Latitude : " + String.format("%.2f", Double.valueOf(AppSettingHelper.getLatitude(getActivity()))) + "\n"
                        + "Longitude : " + String.format("%.2f", Double.valueOf(AppSettingHelper.getLongitude(getActivity()))) + "\n"
                        + "Timezone : " + AppSettingHelper.getTimeZone(getActivity())
        );

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        binding.locationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                } else {

                    if (!isGPSIsEnabled(getActivity())) {
                            // notify user
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("Sorry GPS is not enabled")
                                    .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();

                            goToStartGPS = true;

                    } else {
                        locationInit();
                    }


                }


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

    @Override
    public void onResume() {
        super.onResume();

        if (goToStartGPS) {
            locationInit();
            goToStartGPS = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        binding.loading.setVisibility(View.GONE);

    }


    public void locationInit() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        binding.loading.setVisibility(View.VISIBLE);

    }

    private void updateLocation(Location location) {
        if (location == null) return;

        List<Address> addresses = getAddresses(location.getLatitude(), location.getLongitude());

        TimeZone tz = TimeZone.getDefault();


        binding.cityTv.setText(addresses.get(0).getAddressLine(0));
        binding.latAndLongAndTimezone.setText(
                "Latitude : " + String.format("%.2f", location.getLatitude()) + "\n"
                        + "Longitude : " + String.format("%.2f", location.getLongitude()) + "\n"
                        + "Timezone : " +tz.getRawOffset()/1000/60/60
        );


        AppSettingHelper.setLocationName(getActivity(), addresses.get(0).getAddressLine(0));
        AppSettingHelper.setLatitude(getActivity(),String.valueOf(location.getLatitude()));
        AppSettingHelper.setLongitude(getActivity(),String.valueOf(location.getLongitude()));
        AppSettingHelper.setTimeZone(getActivity(),String.valueOf(tz.getRawOffset()/1000/60/60));

        binding.loading.setVisibility(View.GONE);
        fusedLocationClient.removeLocationUpdates(locationCallback);
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



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationInit();

            } else {
                Toast.makeText(getActivity(), "You have to grant location permission to determine your location", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public boolean isGPSIsEnabled(Context context){
        LocationManager mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);


        try {
            return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch(Exception ex) {
            return true;
        }

    }

    private void startManager() {
        Data data = new Data.Builder().putStringArray("prayerTimes",prayTimes).build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                AzanTimesWorker.class,
                90,
                TimeUnit.MINUTES
        ).setInputData(data).build();

        manager = WorkManager.getInstance(getContext());


        manager.enqueueUniquePeriodicWork(
                "AzanTimes",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
        );
    }


}
