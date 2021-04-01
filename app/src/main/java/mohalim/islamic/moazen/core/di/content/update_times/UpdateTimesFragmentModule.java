package mohalim.islamic.moazen.core.di.content.update_times;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.moazen.ui.setting.SettingFragment;
import mohalim.islamic.moazen.ui.update_times.FragmentUpdateTimes;

@Module
public abstract class UpdateTimesFragmentModule {

    @ContributesAndroidInjector
    abstract FragmentUpdateTimes contributeFragmentUpdateTimes();
}
