package mohalim.islamic.alarm.alert.moazen.core.di.content.setting;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingFragment;

@Module
public abstract class SettingFragmentModule {

    @ContributesAndroidInjector
    abstract SettingFragment contributeSettingFragment();
}
