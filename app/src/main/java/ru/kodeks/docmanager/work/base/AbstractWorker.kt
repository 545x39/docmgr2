package ru.kodeks.docmanager.work.base

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay
import retrofit2.Response
import ru.kodeks.docmanager.const.Network
import ru.kodeks.docmanager.const.PathsAndFileNames
import ru.kodeks.docmanager.const.SYNC_PROGRESS
import ru.kodeks.docmanager.model.io.RequestBase
import ru.kodeks.docmanager.network.api.BaseApi
import timber.log.Timber
import java.net.ConnectException
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

abstract class AbstractWorker<A, Q, R> constructor(
    protected open val api: A,
    protected open val responseDirectory: String,
    val context: Context, workerParameters: WorkerParameters
) :
    CoroutineWorker(context, workerParameters) where A : BaseApi, Q : RequestBase {

    protected lateinit var response: Response<R>

    abstract var request: Q

    private var requestKey: String? = null

    protected var responseFilePath: String? = null

    protected open fun charset(): Charset? = StandardCharsets.UTF_8

    protected open fun outputFileName(): String = "${UUID.randomUUID()}.json"

    protected open val startStatusMessage: String =
        context.getString(ru.kodeks.docmanager.R.string.status_loading)

    protected open var outputData: Data = Data.EMPTY

    abstract fun requestByKey(requestKey: String): Response<R>

    protected val credentials = inputData.getString(PathsAndFileNames.LOGIN).orEmpty() to
            inputData.getString(PathsAndFileNames.PASSWORD).orEmpty()

    /** Как иименно сохранять ответ. */
    abstract suspend fun saveResponse()

    /** Для случаев когда необходимость выполнения работы зависит от какого-либо условия. */
    open suspend fun needToRun() = true

    override suspend fun doWork(): Result {
        kotlin.runCatching {
            if (needToRun()) {
                setProgressAsync(workDataOf(SYNC_PROGRESS to startStatusMessage))
                run { request() }
                run { getDeferred() }
            }
        }.onFailure {
            when (it) {
                is ConnectException -> Result.retry()
                else -> Result.failure(outputData)
            }
        }
        return Result.success(outputData)
    }

    @Throws(java.lang.Exception::class)
    private suspend fun run(attempt: Int = 0, operation: suspend () -> Unit) {
        kotlin.runCatching { operation() }.onFailure { retry(attempt + 1, it) { operation() } }
    }

    private suspend fun request() {
        fun getResponseKey() {
            val keyResponse = api.postDeferred(body = request).execute()
            when (keyResponse.isSuccessful) {
                true -> requestKey = keyResponse.body()?.requestKey
                false -> throw ConnectException()
            }
        }

        @Suppress("BlockingMethodInNonBlockingContext")
        suspend fun getResponse() {
            val response = api.post(body = request).execute()
            when (response.isSuccessful) {
                true -> saveResponse()
                false -> throw ConnectException()
            }
        }

        when (request.runDeferred) {
            true -> getResponseKey()
            false -> getResponse()
        }
    }

    private suspend fun getDeferred() {
        if (!request.runDeferred) return
        checkNotNull(requestKey) { "No request key has been obtained from server" }
        /**Try for DEFERRED_REQUEST_LIFETIME, then quit.*/
        loop@ for (i in 1..Network.DEFERRED_REQUEST_LIFETIME / Network.DEFERRED_REQUEST_RECONNECT_PERIOD) {
            Timber.d("Deferred request by key $requestKey")
            response = requestByKey(requestKey.orEmpty())
            if (response.isSuccessful) {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK,
                    HttpURLConnection.HTTP_PARTIAL -> break@loop
                    else -> delay(Network.DEFERRED_REQUEST_RECONNECT_PERIOD)
                }
            } else {
                throw ConnectException("Got error ${response.code()}")
            }
        }
        saveResponse()
    }

    @Throws(Exception::class)
    private suspend fun retry(attempt: Int, exception: Throwable, function: suspend () -> Unit) {
        if (attempt < Network.MAX_REQUEST_ATTEMPTS) {
            delay(Network.DEFERRED_REQUEST_RECONNECT_PERIOD)
            run(attempt) { function() }
        } else {
            throw exception
        }
    }
}