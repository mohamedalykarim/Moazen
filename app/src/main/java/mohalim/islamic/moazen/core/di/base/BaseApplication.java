package mohalim.islamic.moazen.core.di.base;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import mohalim.islamic.moazen.core.di.component.AppComponent;
import mohalim.islamic.moazen.core.di.component.DaggerAppComponent;

public class BaseApplication extends DaggerApplication {

    private AppComponent appComponent;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        appComponent = DaggerAppComponent.builder().getInstance(this).app();

        return appComponent;
    }
}
