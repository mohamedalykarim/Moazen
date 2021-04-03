package mohalim.islamic.alarm.alert.moazen.ui.setting;

import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;
import mohalim.islamic.alarm.alert.moazen.R;


public class SettingActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingFragment.newInstance())
                    .commitNow();
        }
    }
}
