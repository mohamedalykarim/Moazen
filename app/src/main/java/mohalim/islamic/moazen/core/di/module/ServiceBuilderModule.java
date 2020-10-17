package mohalim.islamic.moazen.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.moazen.core.service.AzanPlayer;

@Module
public abstract class ServiceBuilderModule {

    @ContributesAndroidInjector()
    abstract AzanPlayer contributeAzanPlayer();

}
