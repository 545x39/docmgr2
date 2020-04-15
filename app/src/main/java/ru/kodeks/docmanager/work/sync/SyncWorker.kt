package ru.kodeks.docmanager.work.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.kodeks.docmanager.const.PathsAndFileNames
import ru.kodeks.docmanager.di.const.RESPONSE_DIR
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import ru.kodeks.docmanager.model.io.SyncRequest
import ru.kodeks.docmanager.network.api.SyncApi
import ru.kodeks.docmanager.network.requestbuilder.sync.SyncRequestBuilder
import ru.kodeks.docmanager.persistence.Database
import ru.kodeks.docmanager.persistence.parser.Parser
import ru.kodeks.docmanager.work.base.WriteResponseToFileWorker
import javax.inject.Inject
import javax.inject.Named

class SyncWorker @Inject constructor(
    api: SyncApi,
    @Named(RESPONSE_DIR)
    responseDirectory: String,
    database: Database,
    sharedPreferences: SharedPreferences,
    private val parser: Parser,
    context: Context, workerParameters: WorkerParameters
) : WriteResponseToFileWorker<SyncApi, SyncRequest>(
    api,
    responseDirectory,
    context,
    workerParameters
) {
    override var request: SyncRequest = object : SyncRequestBuilder(database, sharedPreferences) {
        override fun user() = credentials
    }.build()

    /** Синки пишутся в UTF-8.*/
    override fun charset() = Charsets.UTF_8

    override fun outputFileName() = PathsAndFileNames.SYNC_RESPONSE_FILENAME

    override suspend fun saveResponse() {
        super.saveResponse()
        parser.parse(login = credentials.first, password = credentials.second)
    }

    //<editor-fold desc="FACTORY" defaultstate="collapsed">
    class Factory @Inject constructor(
        val api: SyncApi,
        @Named(RESPONSE_DIR)
        val responseDirectory: String,
        val database: Database,
        private val sharedPreferences: SharedPreferences,
        val parser: Parser
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return SyncWorker(
                api,
                responseDirectory,
                database,
                sharedPreferences,
                parser,
                appContext,
                params
            )
        }
    }
    //</editor-fold>
}