package ru.kodeks.docmanager.work.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.NoInternetException
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import ru.kodeks.docmanager.model.io.SyncRequest
import ru.kodeks.docmanager.network.api.BaseApi
import timber.log.Timber
import java.net.HttpURLConnection
import javax.inject.Inject

const val SYNC_REQUEST_TAG = "SYNC_REQUEST_TAG"

class SyncRequestWorker @Inject constructor(var preferences: SharedPreferences,
                                    var api: BaseApi,
                                    context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private var requestKey: String? = null

    //TODO Provide
    lateinit var request: SyncRequest

    override fun doWork(): ListenableWorker.Result {
        Timber.e(inputData.getString("login"))
//        setProgressAsync()
//        return ListenableWorker.Result.failure()
        return ListenableWorker.Result.success()
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
//                parseResponse(response)
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
                        Thread.sleep(5000)
                    }
                }
            } else {
                throw IllegalStateException("Got error ${response.code()}")
            }
        }
        //TODO make return success here
//        parseResponse(response)
    }

/////////////////////////////////
    class Factory @Inject constructor(
        var preferences: SharedPreferences,
        var api: BaseApi
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): Worker {
            return SyncRequestWorker(preferences, api, appContext, params)
        }
    }
}