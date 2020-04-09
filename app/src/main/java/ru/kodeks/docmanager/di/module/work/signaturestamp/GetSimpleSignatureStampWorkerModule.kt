package ru.kodeks.docmanager.di.module.work.signaturestamp

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.key.WorkerKey
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import ru.kodeks.docmanager.work.signaturestamps.GetSimpleSignatureStampWorker

@Module
abstract class GetSimpleSignatureStampWorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(GetSimpleSignatureStampWorker::class)
    internal abstract fun bindWorker(worker: GetSimpleSignatureStampWorker.Factory): ChildWorkerFactory
}