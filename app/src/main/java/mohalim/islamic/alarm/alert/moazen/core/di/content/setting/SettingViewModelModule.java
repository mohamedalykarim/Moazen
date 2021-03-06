package mohalim.islamic.alarm.alert.moazen.core.di.content.setting;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.islamic.alarm.alert.moazen.core.di.key.ViewModelKey;
import mohalim.islamic.alarm.alert.moazen.ui.setting.SettingViewModel;

@Module
public abstract class SettingViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel.class)
    public abstract ViewModel bindsSettingViewModel(SettingViewModel settingViewModel);
}
