package mohalim.islamic.moazen.core.di.content.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.moazen.ui.main.AzanTimesFragmet;

@Module
public abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract AzanTimesFragmet provideAzanTimesFragment();
}
