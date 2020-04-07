package ru.kodeks.docmanager.di.module.work

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.WorkerKey
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import ru.kodeks.docmanager.work.sync.SyncRequestWorker

@Module
abstract class SyncRequestWorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(SyncRequestWorker::class)
    internal abstract fun bindCheckStateWorker(worker: SyncRequestWorker.Factory): ChildWorkerFactory
}