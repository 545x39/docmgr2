package ru.kodeks.docmanager.di.providerfactory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

interface ChildWorkerFactory {
    fun create(appContext: Context, params: WorkerParameters): ListenableWorker
}

class WorkerProviderFactory @Inject constructor(
private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val foundEntry =
            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value
            ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")
        Timber.e("Created class $workerClassName")
        return factoryProvider.get().create(appContext, workerParameters)
    }
}

//class WorkerProviderFactory @Inject constructor(
//    private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
//) : WorkerFactory() {
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): ListenableWorker? {
//        val foundEntry =
//            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
//        return if (foundEntry != null) {
//            val factoryProvider = foundEntry.value
//            factoryProvider.get().create(appContext, workerParameters)
//        } else {
//            val workerClass = Class.forName(workerClassName).asSubclass(Worker::class.java)
//            val constructor = workerClass.getDeclaredConstructor(
//                Context::class.java,
//                WorkerParameters::class.java
//            )
//            constructor.newInstance(appContext, workerParameters)
//        }
//    }
//}