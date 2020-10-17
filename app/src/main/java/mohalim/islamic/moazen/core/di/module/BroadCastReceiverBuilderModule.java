package mohalim.islamic.moazen.core.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import mohalim.islamic.moazen.core.AzanBroadcastReceiver;

@Module
public abstract class BroadCastReceiverBuilderModule {
    @ContributesAndroidInjector
    abstract AzanBroadcastReceiver contributeAzanBroadcastReceiver();
}
