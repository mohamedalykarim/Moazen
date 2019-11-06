package mohalim.islamic.moazen.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.moazen.core.di.content.main.MainViewModelModule;
import mohalim.islamic.moazen.ui.main.MainActivity;

@Module
public abstract class ActivityBuilderModule   {

    @ContributesAndroidInjector(
            modules = {
                    MainViewModelModule.class
            }
    )
    abstract MainActivity contributeMainActivity();

}
