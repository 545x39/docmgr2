package ru.kodeks.docmanager.network.operations

import android.content.SharedPreferences
import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.NoInternetException
import ru.kodeks.docmanager.constants.JsonNames.REQUEST_KEY
import ru.kodeks.docmanager.constants.Network.DEFAULT_DEFERRED_REQUEST_PERIOD
import ru.kodeks.docmanager.constants.Network.DEFAULT_URL
import ru.kodeks.docmanager.constants.Network.MAX_REQUEST_ATTEMPTS
import ru.kodeks.docmanager.constants.ServiceMethod.GET_DEFERRED_RESPONSE_URL_PATH
import ru.kodeks.docmanager.constants.ServiceMethod.SYNC_SVC
import ru.kodeks.docmanager.constants.Settings.DEFERRED_REQUEST_TIMEOUT
import ru.kodeks.docmanager.constants.Settings.ENCRYPT_PASSWORD
import ru.kodeks.docmanager.constants.Settings.SERVER_ADDRESS
import ru.kodeks.docmanager.model.io.RequestBase
import ru.kodeks.docmanager.network.Retrofit
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

//TODO refactor with coroutines
abstract class Operation<T>(var request: T) where T : RequestBase {

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var retrofit: Retrofit

    private val timeout
        get() = preferences.getLong(
            DEFERRED_REQUEST_TIMEOUT,
            DEFAULT_DEFERRED_REQUEST_PERIOD
        )

    //TODO Delete stub
    val serverUrl: String
        get() {
//            UrlValidator().isValid(DocManagerApp.instance.preferences.getString(SERVER_ADDRESS, "http://172.16.1.61/tek_sm/"))
            return preferences.getString(SERVER_ADDRESS, DEFAULT_URL) ?: DEFAULT_URL
        }

    var requestKey: String? = null

    protected abstract fun getUrl(): String

    @Throws(java.lang.Exception::class)
    fun execute() {
        run { check() }
        run { upload() }
        run { request() }
        run { getDeferred() }
    }

    private fun request() {
        fun getResponseKey() {
            val response = retrofit.postDeferred(url = getUrl(), body = request).execute()
            if (response.isSuccessful) {
                requestKey = response.body()?.requestKey
            } else {
                throw NoInternetException()
            }
        }

        fun getResponse() {
            val response = retrofit.post(url = getUrl(), body = request).execute()
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
        val url = "$serverUrl$GET_DEFERRED_RESPONSE_URL_PATH?$REQUEST_KEY=$requestKey"
        var response: Response<ResponseBody>
        loop@ while (true) {
            Timber.d("Deferred request by key $requestKey")
            response = retrofit.get(url).execute()
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
        val response = retrofit.checkState("$serverUrl${SYNC_SVC}/ChkState").execute()
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