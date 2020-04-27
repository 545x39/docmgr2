package ru.kodeks.docmanager.work.checkstate

import android.content.Context
import android.content.SharedPreferences
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import ru.kodeks.docmanager.R
import ru.kodeks.docmanager.const.Network
import ru.kodeks.docmanager.const.SYNC_PROGRESS
import ru.kodeks.docmanager.const.Settings
import ru.kodeks.docmanager.di.factory.ChildWorkerFactory
import ru.kodeks.docmanager.network.api.BaseApi
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

class CheckStateWorker @Inject constructor(
    var preferences: SharedPreferences,
    var api: BaseApi,
    val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        setProgressAsync(workDataOf(SYNC_PROGRESS to context.getString(R.string.status_checking_connection)))
        kotlin.runCatching { run { check() } }.onFailure {
            Timber.e(it)
            return Result.failure()
        }
        return Result.success()
    }

    @Throws(IllegalStateException::class)
    private fun check() {
        val response = api.checkState().execute()
        if (response.isSuccessful) {
            when (response.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val encrypt = response.body()?.aes ?: false
                    preferences.edit().putBoolean(Settings.ENCRYPT_PASSWORD, encrypt).apply()
                }
                else -> {
                    throw IllegalStateException("Got error ${response.code()}")
                }
            }
        } else {
            /** Заглушка для старых версий сервиса, у которых нет ChkState. */
            if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                preferences.edit().putBoolean(Settings.ENCRYPT_PASSWORD, false).apply()
            } else {
                throw IllegalStateException("Request was a failure")
            }
        }
    }

    @Throws(java.lang.Exception::class)
    private fun run(attempt: Int = 0, operation: () -> Unit) {
        kotlin.runCatching { operation() }.onFailure { retry(attempt + 1, it) { operation() } }
    }

    @Throws(Exception::class)
    fun retry(attempt: Int, exception: Throwable, function: () -> Unit) {
        if (attempt < Network.MAX_REQUEST_ATTEMPTS) {
            Thread.sleep(Network.DEFERRED_REQUEST_RECONNECT_PERIOD)
            run(attempt) { function() }
        } else {
            throw exception
        }
    }

    //<editor-fold desc="FACTORY" defaultstate="collapsed">
    class Factory @Inject constructor(
        var preferences: SharedPreferences,
        var api: BaseApi
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return CheckStateWorker(preferences, api, appContext, params)
        }
    }
    //</editor-fold>
}