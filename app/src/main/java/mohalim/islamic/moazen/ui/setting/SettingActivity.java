package mohalim.islamic.moazen.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dagger.android.support.DaggerAppCompatActivity;
import mohalim.islamic.moazen.R;


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
