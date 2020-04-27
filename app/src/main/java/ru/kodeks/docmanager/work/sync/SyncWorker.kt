package ru.kodeks.docmanager.work.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.const.PathsAndFileNames
import ru.kodeks.docmanager.const.SYNC_PROGRESS
import ru.kodeks.docmanager.db.Database
import ru.kodeks.docmanager.di.const.RESPONSE_DIR
import ru.kodeks.docmanager.di.factory.ChildWorkerFactory
import ru.kodeks.docmanager.model.io.SyncRequest
import ru.kodeks.docmanager.network.api.SyncApi
import ru.kodeks.docmanager.network.requestbuilder.sync.SyncRequestBuilder
import ru.kodeks.docmanager.work.base.FileDownloadWorker
import javax.inject.Inject
import javax.inject.Named

class SyncWorker @Inject constructor(
    api: SyncApi,
    @Named(RESPONSE_DIR)
    responseDirectory: String,
    database: Database,
    sharedPreferences: SharedPreferences,
    context: Context, workerParameters: WorkerParameters
) : FileDownloadWorker<SyncApi, SyncRequest>(
    api,
    responseDirectory,
    context,
    workerParameters
) {
    override var request: SyncRequest = object : SyncRequestBuilder(database, sharedPreferences) {
        override fun credentials() = credentials
    }.build()

    /** Синки пишутся в UTF-8.*/
    override fun charset() = Charsets.UTF_8

    override val startStatusMessage: String = context.getString(R.string.status_sync_started)

    override fun outputFileName() = PathsAndFileNames.SYNC_RESPONSE_FILENAME

    override fun requestByKey(requestKey: String): Response<ResponseBody> {
        setProgressAsync(workDataOf(SYNC_PROGRESS to context.getString(R.string.status_waiting_for_response)))
        return super.requestByKey(requestKey)
    }

    override suspend fun saveResponse() {
        setProgressAsync(workDataOf(SYNC_PROGRESS to context.getString(R.string.status_response_ready)))
        super.saveResponse()
    }

    //<editor-fold desc="FACTORY" defaultstate="collapsed">
    class Factory @Inject constructor(
        val api: SyncApi,
        @Named(RESPONSE_DIR)
        val responseDirectory: String,
        val database: Database,
        private val sharedPreferences: SharedPreferences
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return SyncWorker(
                api,
                responseDirectory,
                database,
                sharedPreferences,
//                parser,
                appContext,
                params
            )
        }
    }
    //</editor-fold>
}