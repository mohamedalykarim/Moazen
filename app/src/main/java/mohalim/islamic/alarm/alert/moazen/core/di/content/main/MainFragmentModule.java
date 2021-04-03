package mohalim.islamic.alarm.alert.moazen.core.di.content.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.alarm.alert.moazen.ui.main.AddDrawingPermissionDialog;
import mohalim.islamic.alarm.alert.moazen.ui.main.AzanTimesFragmet;

@Module
public abstract class MainFragmentModule {

    @ContributesAndroidInjector
    abstract AzanTimesFragmet provideAzanTimesFragment();

    @ContributesAndroidInjector
    abstract AddDrawingPermissionDialog provideAddDrawingPermissionDialogFragment();
}
