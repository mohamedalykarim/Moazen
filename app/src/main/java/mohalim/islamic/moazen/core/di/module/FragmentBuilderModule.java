package mohalim.islamic.moazen.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.moazen.ui.fragments.AzanTimesFragmet;

@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract AzanTimesFragmet provideAzanTimesFragment();
}
