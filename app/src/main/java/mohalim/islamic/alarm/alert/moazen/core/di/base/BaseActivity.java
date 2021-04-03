package mohalim.islamic.alarm.alert.moazen.core.di.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import dagger.android.support.DaggerAppCompatActivity;

public class BaseActivity extends DaggerAppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
