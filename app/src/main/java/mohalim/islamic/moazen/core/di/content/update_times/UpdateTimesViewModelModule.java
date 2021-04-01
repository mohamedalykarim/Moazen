package mohalim.islamic.moazen.core.di.content.update_times;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.islamic.moazen.core.di.key.ViewModelKey;
import mohalim.islamic.moazen.ui.setting.SettingViewModel;
import mohalim.islamic.moazen.ui.update_times.UpdateTimesViewModel;

@Module
public abstract class UpdateTimesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UpdateTimesViewModel.class)
    public abstract ViewModel bindsSettingViewModel(UpdateTimesViewModel updateTimesViewModel);
}
