package ru.kodeks.docmanager.di.module.work

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.WorkerKey
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import ru.kodeks.docmanager.work.checkstate.CheckStateWorker

@Module
abstract class CheckStateWorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(CheckStateWorker::class)
    internal abstract fun bindCheckStateWorker(worker: CheckStateWorker.Factory): ChildWorkerFactory
}