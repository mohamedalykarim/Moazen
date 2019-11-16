package mohalim.islamic.moazen.core.di.module;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import mohalim.islamic.moazen.core.di.key.WorkerKey;
import mohalim.islamic.moazen.core.service.AzanTimesWorker;
import mohalim.islamic.moazen.core.service.AzanWorker;
import mohalim.islamic.moazen.core.service.ChildWorkerFactory;
import mohalim.islamic.moazen.core.service.ReminderWorker;

@Module
public interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(AzanWorker.class)
    ChildWorkerFactory bindAzanWorker(AzanWorker.Factory factory);


    @Binds
    @IntoMap
    @WorkerKey(AzanTimesWorker.class)
    ChildWorkerFactory bindAzanTimesWorker(AzanTimesWorker.Factory factory);

    @Binds
    @IntoMap
    @WorkerKey(ReminderWorker.class)
    ChildWorkerFactory bindReminderWorker(ReminderWorker.Factory factory);

}

