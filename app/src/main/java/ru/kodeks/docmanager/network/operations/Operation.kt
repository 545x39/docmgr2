package ru.kodeks.docmanager.network.operations

import android.util.Log
import ru.kodeks.docmanager.network.Network
import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.constants.JsonNames.REQUEST_KEY
import ru.kodeks.docmanager.constants.LogTag
import ru.kodeks.docmanager.constants.ServiceMethod.GET_DEFERRED_RESPONSE_URL_PATH
import ru.kodeks.docmanager.constants.ServiceMethod.SYNC_SVC
import ru.kodeks.docmanager.model.io.RequestBase
import ru.kodeks.docmanager.constants.Settings.ENCRYPT_PASSWORD
import ru.kodeks.docmanager.util.DocManagerApp
import ru.kodeks.docmanager.util.NoInternetException
import java.net.HttpURLConnection

const val MAX_ATTEMPTS = 3

abstract class Operation<T>(var request: T) where T : RequestBase {

    //TODO потом перевести на возврат значения из настройки
    private val timeout get() = 2000L

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
            val response =
                Network.INSTANCE.api.postDeferred(url = getUrl(), body = request).execute()
            if (response.isSuccessful) {
                requestKey = response.body()?.requestKey
            } else {
                throw NoInternetException()
            }
        }

        fun getResponse() {
            val response = Network.INSTANCE.api.post(url = getUrl(), body = request).execute()
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
        val url = "${Network.INSTANCE.url}$GET_DEFERRED_RESPONSE_URL_PATH?$REQUEST_KEY=$requestKey"
        var response: Response<ResponseBody>
        loop@ while (true) {
            Log.d(LogTag.TAG, "Deferred request by key $requestKey")
            response = Network.INSTANCE.api.get(url).execute()
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
        try {
            operation()
        } catch (e: Exception) {
            retry(attempt + 1, e) { operation() }
        }
    }

    @Throws(Exception::class)
    fun retry(attempt: Int, exception: java.lang.Exception, function: () -> Unit) {
        if (attempt < MAX_ATTEMPTS) {
            Thread.sleep(timeout)
            run(attempt) { function() }
        } else {
            throw exception
        }
    }

    @Throws(IllegalStateException::class)
    private fun check() {
        val response =
            Network.INSTANCE.api.checkState("${Network.INSTANCE.url}${SYNC_SVC}/ChkState").execute()
        if (response.isSuccessful) {
            when (response.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val encrypt = response.body()?.aes ?: false
                    DocManagerApp.instance.preferences.edit().putBoolean(ENCRYPT_PASSWORD, encrypt)
                        .apply()
                }
                else -> {
                    throw IllegalStateException("Got error ${response.code()}")
                }
            }
        } else {
            /** Заглушка для старых версий сервиса, у которых нет ChkState. */
            if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                DocManagerApp.instance.preferences.edit()
                    .putBoolean(ENCRYPT_PASSWORD, false).apply()
            } else {
                throw IllegalStateException("Request was a failure")
            }
        }
    }
}