package mohalim.islamic.alarm.alert.moazen.core.di.content.update_times;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.alarm.alert.moazen.ui.update_times.FragmentUpdateTimes;

@Module
public abstract class UpdateTimesFragmentModule {

    @ContributesAndroidInjector
    abstract FragmentUpdateTimes contributeFragmentUpdateTimes();
}
