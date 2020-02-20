package ru.kodeks.docmanager.network.util

import android.util.Log
import okhttp3.ResponseBody
import ru.kodeks.docmanager.constants.LogTag.TAG
import java.io.*
import java.nio.charset.Charset
import kotlin.math.min


fun write(body: ResponseBody, file: File, inputEncoding: String = "Cp1251", outputEncoding: Charset? = null): Boolean {

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

    fun toFileWithEncoding(inputStream: InputStream, outputStream: FileOutputStream, inputEncoding: String, outputEncoding: Charset) {
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
            Log.d(TAG, "Downloaded $downloaded bytes of ${body.contentLength()}")
        }
        writer.flush()
    }

    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null
    return try {
        Log.d(TAG, "WRITING RESPONSE.")
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