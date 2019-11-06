package mohalim.islamic.moazen.core.di.module;

import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import mohalim.islamic.moazen.core.viewmodel.ViewModelProviderFactory;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory factory);
}
