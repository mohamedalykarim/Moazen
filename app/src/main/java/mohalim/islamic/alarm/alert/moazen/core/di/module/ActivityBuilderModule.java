package mohalim.islamic.alarm.alert.moazen.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.alarm.alert.moazen.core.di.content.main.MainFragmentModule;
import mohalim.islamic.alarm.alert.moazen.core.di.content.main.MainViewModelModule;
import mohalim.islamic.alarm.alert.moazen.core.di.content.setting.SettingFragmentModule;
import mohalim.islamic.alarm.alert.moazen.core.di.content.setting.SettingViewModelModule;
import mohalim.islamic.alarm.alert.moazen.core.di.content.update_times.UpdateTimesFragmentModule;
import mohalim.islamic.alarm.alert.moazen.core.di.content.update_times.UpdateTimesViewModelModule;
import mohalim.islamic.alarm.alert.moazen.ui.main.MainActivity;
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingActivity;
import mohalim.islamic.alarm.alert.moazen.ui.update_times.UpdateTimesActivity;

@Module
public abstract class ActivityBuilderModule   {

    @ContributesAndroidInjector(
            modules = {
                    MainFragmentModule.class,
                    MainViewModelModule.class
            }
    )
    abstract MainActivity contributeMainActivity();


    @ContributesAndroidInjector(
            modules = {
                    SettingFragmentModule.class,
                    SettingViewModelModule.class
            }
    )
    abstract SettingActivity contributeSettingActivity();

    @ContributesAndroidInjector(
            modules = {
                    UpdateTimesFragmentModule.class,
                    UpdateTimesViewModelModule.class
            }
    )
    abstract UpdateTimesActivity contributeUpdateTimesActivity();

}
