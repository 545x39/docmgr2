package ru.kodeks.docmanager.work.base

import android.content.Context
import androidx.work.WorkerParameters
import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.model.io.RequestBase
import ru.kodeks.docmanager.network.api.BaseApi
import ru.kodeks.docmanager.persistence.parser.write
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

abstract class WriteResponseToFileWorker<A, Q>(
    api: A,
    responseDirectory: String,
    context: Context, workerParameters: WorkerParameters
) : BaseWorker<A, Q, ResponseBody>(
    api,
    responseDirectory,
    context,
    workerParameters
) where A : BaseApi, Q : RequestBase {

    /** Менять при необходимости. */
    override fun charset(): Charset? = null

    override fun requestByKey(requestKey: String): Response<ResponseBody> {
        return api.getDeferred(requestKey).execute()
    }

    override suspend fun saveResponse() {
        val responseFile =
            File("${responseDirectory}${File.separator}${outputFileName()}")
        response.body()?.apply {
            when (write(this, responseFile, outputEncoding = charset())) {
                true -> {
                    responseFilePath = responseFile.absolutePath
                    Timber.e("Write succeeded")
                }
                false -> {
                    Timber.e("Write FAILED.")
                    throw IOException("Couldn't write response to a file.")
                }
            }
        } ?: throw IllegalArgumentException("Response body was NULL.")
    }
}