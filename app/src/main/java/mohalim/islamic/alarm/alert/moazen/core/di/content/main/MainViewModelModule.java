package mohalim.islamic.alarm.alert.moazen.core.di.content.main;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.islamic.alarm.alert.moazen.core.di.key.ViewModelKey;
import mohalim.islamic.alarm.alert.moazen.ui.main.MainViewModel;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);
}
