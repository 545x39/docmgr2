package ru.kodeks.docmanager.di.module.work.sync

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.factory.ChildWorkerFactory
import ru.kodeks.docmanager.di.key.WorkerKey
import ru.kodeks.docmanager.work.checkstate.CheckStateWorker
import ru.kodeks.docmanager.work.sync.ParserWorker
import ru.kodeks.docmanager.work.sync.SyncWorker
import ru.kodeks.docmanager.work.sync.TestWorker

@Module
abstract class CheckStateWorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(CheckStateWorker::class)
    internal abstract fun bindWorker(worker: CheckStateWorker.Factory): ChildWorkerFactory
}

@Module
abstract class SyncWorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(SyncWorker::class)
    internal abstract fun bindWorker(worker: SyncWorker.Factory): ChildWorkerFactory
}

@Module
abstract class ParserWorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(ParserWorker::class)
    internal abstract fun bindWorker(worker: ParserWorker.Factory): ChildWorkerFactory
}

//TODO DELETE
@Module
abstract class TestWorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(TestWorker::class)
    internal abstract fun bindWorker(worker: TestWorker.Factory): ChildWorkerFactory
}