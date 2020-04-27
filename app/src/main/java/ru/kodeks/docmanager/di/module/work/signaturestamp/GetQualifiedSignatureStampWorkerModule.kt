package ru.kodeks.docmanager.di.module.work.signaturestamp

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kodeks.docmanager.di.factory.ChildWorkerFactory
import ru.kodeks.docmanager.di.key.WorkerKey
import ru.kodeks.docmanager.work.signaturestamps.GetQualifiedSignatureStampWorker

@Module
abstract class GetQualifiedSignatureStampWorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(GetQualifiedSignatureStampWorker::class)
    internal abstract fun bindWorker(worker: GetQualifiedSignatureStampWorker.Factory): ChildWorkerFactory
}