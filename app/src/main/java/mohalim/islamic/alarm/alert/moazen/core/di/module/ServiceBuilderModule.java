package mohalim.islamic.alarm.alert.moazen.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.alarm.alert.moazen.core.service.AzanPlayer;

@Module
public abstract class ServiceBuilderModule {

    @ContributesAndroidInjector()
    abstract AzanPlayer contributeAzanPlayer();

}
