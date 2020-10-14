package mohalim.islamic.moazen.core.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import mohalim.islamic.moazen.core.di.base.BaseApplication;
import mohalim.islamic.moazen.core.di.module.ActivityBuilderModule;
import mohalim.islamic.moazen.core.di.module.AppModule;
import mohalim.islamic.moazen.core.di.module.ServiceBuilderModule;
import mohalim.islamic.moazen.core.di.module.ViewModelFactoryModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuilderModule.class,
                ServiceBuilderModule.class,
                ViewModelFactoryModule.class,
                AppModule.class
        })

public interface AppComponent extends AndroidInjector<BaseApplication> {



    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder getInstance(Application application);

        AppComponent app();

    }


}
