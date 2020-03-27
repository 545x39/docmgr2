package ru.kodeks.docmanager.network.operations

import android.content.SharedPreferences
import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.NoInternetException
import ru.kodeks.docmanager.constants.Network.DEFAULT_DEFERRED_REQUEST_PERIOD
import ru.kodeks.docmanager.constants.Network.MAX_REQUEST_ATTEMPTS
import ru.kodeks.docmanager.constants.Settings.DEFERRED_REQUEST_TIMEOUT
import ru.kodeks.docmanager.constants.Settings.ENCRYPT_PASSWORD
import ru.kodeks.docmanager.model.io.RequestBase
import ru.kodeks.docmanager.network.api.BaseApi
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

//TODO refactor with coroutines
abstract class Operation<A, R>(private var request: R) where A : BaseApi, R : RequestBase {

    @Inject
    lateinit var preferences: SharedPreferences

    abstract var api: A

    private val timeout
        get() = preferences.getLong(
            DEFERRED_REQUEST_TIMEOUT,
            DEFAULT_DEFERRED_REQUEST_PERIOD
        )

    private var requestKey: String? = null

    @Throws(java.lang.Exception::class)
    fun execute() {
        run { check() }
        run { upload() }
        run { request() }
        run { getDeferred() }
    }

    private fun request() {
        fun getResponseKey() {
//            val response = api.postDeferred(url = getUrl(), body = request).execute()
            val response = api.postDeferred(body = request).execute()
            if (response.isSuccessful) {
                requestKey = response.body()?.requestKey
            } else {
                throw NoInternetException()
            }
        }

        fun getResponse() {
//            val response = api.post(url = getUrl(), body = request).execute()
            val response = api.post(body = request).execute()
            if (response.isSuccessful) {
                parseResponse(response)
            } else {
                throw NoInternetException()
            }
        }

        when (request.runDeferred) {
            true -> getResponseKey()
            false -> getResponse()
        }
    }

    private fun getDeferred() {
        if (!request.runDeferred) return
        checkNotNull(requestKey) { "No request key has been obtained from server" }
        var response: Response<ResponseBody>
        loop@ while (true) {
            Timber.d("Deferred request by key $requestKey")
            response = api.getDeferred(requestKey.orEmpty()).execute()
            if (response.isSuccessful) {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_PARTIAL -> {
                        break@loop
                    }
                    else -> {
                        Thread.sleep(timeout)
                    }
                }
            } else {
                throw IllegalStateException("Got error ${response.code()}")
            }
        }
        parseResponse(response)
    }

    protected open fun parseResponse(response: Response<ResponseBody>) {}

    /** Override where needed*/
    protected open fun upload() {
        //
    }

    @Throws(java.lang.Exception::class)
    private fun run(attempt: Int = 0, operation: () -> Unit) {
        kotlin.runCatching { operation() }.onFailure { retry(attempt + 1, it) { operation() } }
    }

    @Throws(Exception::class)
    fun retry(attempt: Int, exception: Throwable, function: () -> Unit) {
        if (attempt < MAX_REQUEST_ATTEMPTS) {
            Thread.sleep(timeout)
            run(attempt) { function() }
        } else {
            throw exception
        }
    }

    @Throws(IllegalStateException::class)
    private fun check() {
//        val response = api.checkState("$serverUrl${SYNC_SVC}/ChkState").execute()
        val response = api.checkState().execute()
        if (response.isSuccessful) {
            when (response.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val encrypt = response.body()?.aes ?: false
                    preferences.edit().putBoolean(ENCRYPT_PASSWORD, encrypt).apply()
                }
                else -> {
                    throw IllegalStateException("Got error ${response.code()}")
                }
            }
        } else {
            /** Заглушка для старых версий сервиса, у которых нет ChkState. */
            if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                preferences.edit().putBoolean(ENCRYPT_PASSWORD, false).apply()
            } else {
                throw IllegalStateException("Request was a failure")
            }
        }
    }
}