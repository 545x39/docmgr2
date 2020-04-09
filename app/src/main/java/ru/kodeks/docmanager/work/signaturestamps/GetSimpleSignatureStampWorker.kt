package ru.kodeks.docmanager.work.signaturestamps

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.kodeks.docmanager.const.PathsAndFileNames.LOGIN
import ru.kodeks.docmanager.const.PathsAndFileNames.PASSWORD
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import ru.kodeks.docmanager.network.api.BaseApi
import ru.kodeks.docmanager.network.api.GetSignatureStampApi
import ru.kodeks.docmanager.network.operations.signaturestamp.GetSignatureStampOperation
import ru.kodeks.docmanager.network.requestbuilder.signaturestamp.GetSimpleSignatureStampRequestBuilder
import timber.log.Timber
import javax.inject.Inject

/** Загрузка штампа простой подписи. */
class GetSimpleSignatureStampWorker @Inject constructor(
    var operation: GetSignatureStampOperation,
    var api: BaseApi, context: Context, workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        runCatching {
            operation.apply {
                request = object : GetSimpleSignatureStampRequestBuilder() {
                    override fun user(): Pair<String, String> {
                        return inputData.getString(LOGIN).orEmpty() to
                                inputData.getString(PASSWORD).orEmpty()
                    }
                }.build()
                execute()
            }
        }.onFailure {
            Timber.e(it)
            return Result.failure()
        }
        return Result.success()
    }

    //<editor-fold desc="FACTORY" defaultstate="collapsed">
    class Factory @Inject constructor(
        var operation: GetSignatureStampOperation,
        var api: GetSignatureStampApi
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): Worker {
            return GetSimpleSignatureStampWorker(
                operation, api, appContext, params
            )
        }
    }
    //</editor-fold>
}