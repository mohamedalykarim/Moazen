package mohalim.islamic.moazen.core.di.base;

import android.app.Application;

import androidx.work.Configuration;
import androidx.work.WorkManager;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import mohalim.islamic.moazen.core.di.component.AppComponent;
import mohalim.islamic.moazen.core.di.component.DaggerAppComponent;
import mohalim.islamic.moazen.core.service.CustomWorkerFactory;

public class BaseApplication extends DaggerApplication {

    private AppComponent appComponent;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        appComponent = DaggerAppComponent.builder().getInstance(this).app();

        Configuration configuration = new Configuration.Builder()
                .setWorkerFactory(appComponent.factory())
                .build();

        WorkManager.initialize(this, configuration);


        return appComponent;
    }
}
