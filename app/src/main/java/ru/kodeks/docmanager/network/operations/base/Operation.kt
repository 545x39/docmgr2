package ru.kodeks.docmanager.network.operations.base

import retrofit2.Response
import ru.kodeks.docmanager.NoInternetException
import ru.kodeks.docmanager.const.Network
import ru.kodeks.docmanager.const.Network.DEFERRED_REQUEST_LIFETIME
import ru.kodeks.docmanager.const.Network.DEFERRED_REQUEST_RECONNECT_PERIOD
import ru.kodeks.docmanager.model.io.RequestBase
import ru.kodeks.docmanager.network.api.BaseApi
import timber.log.Timber
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*

abstract class Operation<A, Q, R>(
    open val api: A,
    open val responseDirectory: String
) where A : BaseApi, Q : RequestBase {

    protected lateinit var response: Response<R>

    abstract var request: Q

    private var requestKey: String? = null

    protected var responseFilePath: String? = null

    protected open fun charset(): Charset? = StandardCharsets.UTF_8

    protected open fun outputFileName(): String = "${UUID.randomUUID()}.json"

    abstract fun requestByKey(requestKey: String): Response<R>

    @Throws(java.lang.Exception::class)
    fun execute(): String? {
        run { request() }
        run { getDeferred() }
        return responseFilePath
    }

    abstract fun saveResponse()

    private fun request() {
        fun getResponseKey() {
            val keyResponse = api.postDeferred(body = request).execute()
            when (keyResponse.isSuccessful) {
                true -> requestKey = keyResponse.body()?.requestKey
                false -> throw NoInternetException()
            }
        }

        fun getResponse() {
            val response = api.post(body = request).execute()
            when (response.isSuccessful) {
                true -> saveResponse()
                false -> throw NoInternetException()
            }
        }

        when (request.runDeferred) {
            true -> getResponseKey()
            false -> getResponse()
        }
    }
    //TODO SWITCH TO COROUTINES
    private fun getDeferred() {
        fun read(): Boolean {
            when (response.code()) {
                HttpURLConnection.HTTP_OK,
                HttpURLConnection.HTTP_PARTIAL -> return true
                else -> Thread.sleep(DEFERRED_REQUEST_RECONNECT_PERIOD)
            }
            return false
        }
        if (!request.runDeferred) return
        checkNotNull(requestKey) { "No request key has been obtained from server" }
        //Try for DEFERRED_REQUEST_LIFETIME, then quit.
        loop@ for (i in 1..DEFERRED_REQUEST_LIFETIME / DEFERRED_REQUEST_RECONNECT_PERIOD) {
            Timber.d("Deferred request by key $requestKey")
            response = requestByKey(requestKey.orEmpty())
            if (response.isSuccessful) {
                when (response.code()) {
                    HttpURLConnection.HTTP_OK,
                    HttpURLConnection.HTTP_PARTIAL -> break@loop
                    else -> Thread.sleep(DEFERRED_REQUEST_RECONNECT_PERIOD)
                }
            } else {
                throw IllegalStateException("Got error ${response.code()}")
            }
        }
        saveResponse()
    }

    @Throws(java.lang.Exception::class)
    private fun run(attempt: Int = 0, operation: () -> Unit) {
        kotlin.runCatching { operation() }.onFailure { retry(attempt + 1, it) { operation() } }
    }

    @Throws(Exception::class)
    private fun retry(attempt: Int, exception: Throwable, function: () -> Unit) {
        if (attempt < Network.MAX_REQUEST_ATTEMPTS) {
            Thread.sleep(DEFERRED_REQUEST_RECONNECT_PERIOD)
            run(attempt) { function() }
        } else {
            throw exception
        }
    }
}
