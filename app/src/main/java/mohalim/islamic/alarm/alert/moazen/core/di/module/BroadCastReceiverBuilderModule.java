package mohalim.islamic.alarm.alert.moazen.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.alarm.alert.moazen.core.AzanBroadcastReceiver;

@Module
public abstract class BroadCastReceiverBuilderModule {
    @ContributesAndroidInjector
    abstract AzanBroadcastReceiver contributeAzanBroadcastReceiver();
}
