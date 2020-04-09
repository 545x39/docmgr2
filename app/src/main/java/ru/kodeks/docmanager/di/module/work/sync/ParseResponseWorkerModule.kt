package ru.kodeks.docmanager.di.module.work.sync

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.WorkerKey
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import ru.kodeks.docmanager.work.sync.ParseResponseWorker

@Module
abstract class ParseResponseWorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(ParseResponseWorker::class)
    internal  abstract fun bindWorker(worker: ParseResponseWorker.Factory): ChildWorkerFactory
}