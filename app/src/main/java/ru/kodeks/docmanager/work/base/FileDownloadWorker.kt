package ru.kodeks.docmanager.work.base

import android.content.Context
import androidx.work.WorkerParameters
import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.model.io.RequestBase
import ru.kodeks.docmanager.network.api.BaseApi
import timber.log.Timber
import java.io.*
import java.nio.charset.Charset
import kotlin.math.min

abstract class FileDownloadWorker<A, Q>(
    api: A,
    responseDirectory: String,
    context: Context, workerParameters: WorkerParameters
) : AbstractWorker<A, Q, ResponseBody>(
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

    private fun write(
        body: ResponseBody,
        file: File,
        inputEncoding: String = "Cp1251",
        outputEncoding: Charset? = null
    ): Boolean {

        fun toFile(inputStream: InputStream, outputStream: FileOutputStream) {
            val fileReader = ByteArray(4096)
            var fileSizeDownloaded: Long = 0
            while (true) {
                val read = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
                outputStream.flush()
            }
        }

        fun toFileWithEncoding(
            inputStream: InputStream,
            outputStream: FileOutputStream,
            inputEncoding: String,
            outputEncoding: Charset
        ) {
            val reader = InputStreamReader(inputStream, inputEncoding)
            val writer = OutputStreamWriter(outputStream, outputEncoding)
            val bufferSize = 4 * 1024
            var bytesRead: Int
            val buffer = CharArray(min(bufferSize.toLong(), body.contentLength()).toInt())
            var downloaded = 0
            while (true) {
                bytesRead = reader.read(buffer)
                if (bytesRead == -1) {
                    break
                }
                writer.write(buffer, 0, bytesRead)
                downloaded += bytesRead
                Timber.d("Downloaded $downloaded bytes of ${body.contentLength()}")
            }
            writer.flush()
        }

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        return try {
            Timber.d("WRITING RESPONSE.")
            inputStream = body.byteStream()
            outputStream = FileOutputStream(file)
            ////
            outputEncoding?.also { toFileWithEncoding(inputStream, outputStream, inputEncoding, it) }
                ?: toFile(inputStream, outputStream)
            true
        } catch (e: IOException) {
            false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }
}
