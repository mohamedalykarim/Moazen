package mohalim.islamic.moazen.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.moazen.core.di.content.main.MainFragmentModule;
import mohalim.islamic.moazen.core.di.content.main.MainViewModelModule;
import mohalim.islamic.moazen.core.di.content.setting.SettingFragmentModule;
import mohalim.islamic.moazen.core.di.content.setting.SettingViewModelModule;
import mohalim.islamic.moazen.core.service.AzanPlayer;
import mohalim.islamic.moazen.core.service.AzanService;
import mohalim.islamic.moazen.ui.main.MainActivity;
import mohalim.islamic.moazen.ui.setting.SettingActivity;

@Module
public abstract class ServiceBuilderModule {

    @ContributesAndroidInjector()
    abstract AzanService contributeAzanService();

    @ContributesAndroidInjector()
    abstract AzanPlayer contributeAzanPlayer();

}
