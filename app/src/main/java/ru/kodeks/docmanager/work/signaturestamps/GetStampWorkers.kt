package ru.kodeks.docmanager.work.signaturestamps

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.di.const.STAMP_DIR
import ru.kodeks.docmanager.di.factory.ChildWorkerFactory
import ru.kodeks.docmanager.model.io.GetSignatureStampRequest
import ru.kodeks.docmanager.network.api.GetSignatureStampApi
import ru.kodeks.docmanager.network.requestbuilder.signaturestamp.GetQualifiedSignatureStampRequestBuilder
import ru.kodeks.docmanager.network.requestbuilder.signaturestamp.GetSimpleSignatureStampRequestBuilder
import ru.kodeks.docmanager.work.base.FileDownloadWorker
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Named

abstract class AGetSignatureStampWorker constructor(
    api: GetSignatureStampApi,
    @Named(STAMP_DIR)
    responseDirectory: String,
    context: Context, workerParameters: WorkerParameters
) : FileDownloadWorker<GetSignatureStampApi, GetSignatureStampRequest>(
    api,
    responseDirectory,
    context,
    workerParameters
){

    override fun outputFileName() = "${request.signatureType}.png"

    override val startStatusMessage: String = context.getString(R.string.status_loading_stamps)

    override suspend fun needToRun(): Boolean {
        Timber.e("FILE ${File("$responseDirectory${File.separator}${outputFileName()}")} EXISTS?: ${File("$responseDirectory${File.separator}${outputFileName()}").exists()}")
        return !File("$responseDirectory${File.separator}${outputFileName()}").exists()
    }
}

/** Загрузка штампа простой подписи. */
class GetSimpleSignatureStampWorker @Inject constructor(
    api: GetSignatureStampApi,
    @Named(STAMP_DIR)
    responseDirectory: String,
    context: Context, workerParameters: WorkerParameters
) : AGetSignatureStampWorker(
    api,
    responseDirectory,
    context,
    workerParameters
) {
    override var request = object : GetSimpleSignatureStampRequestBuilder() {
        override fun credentials() = credentials
    }.build()

    //<editor-fold desc="FACTORY" defaultstate="collapsed">
    class Factory @Inject constructor(
        val api: GetSignatureStampApi,
        @Named(STAMP_DIR)
        val responseDirectory: String
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return GetSimpleSignatureStampWorker(
                api,
                responseDirectory,
                appContext,
                params
            )
        }
    }
    //</editor-fold>
}

/** Загрузка штампа квалифицированной подписи. */
class GetQualifiedSignatureStampWorker @Inject constructor(
    api: GetSignatureStampApi,
    @Named(STAMP_DIR)
    responseDirectory: String,
    context: Context, workerParameters: WorkerParameters
) : AGetSignatureStampWorker(
    api,
    responseDirectory,
    context,
    workerParameters
) {
    override var request = object : GetQualifiedSignatureStampRequestBuilder() {
        override fun credentials() = credentials
    }.build()

    //<editor-fold desc="FACTORY" defaultstate="collapsed">
    class Factory @Inject constructor(
        val api: GetSignatureStampApi,
        @Named(STAMP_DIR)
        val responseDirectory: String
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return GetQualifiedSignatureStampWorker(
                api,
                responseDirectory,
                appContext,
                params
            )
        }
    }
    //</editor-fold>
}