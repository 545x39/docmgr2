package ru.kodeks.docmanager.util

import timber.log.Timber
import java.io.*


object FileTools {

    fun copyFile(sourceFile: File, destinationFile: File) {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = FileInputStream(sourceFile)
            outputStream = FileOutputStream(destinationFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
        } catch (e: Exception) {
            Timber.e("Couldn't copy file  ${sourceFile.name} to ${destinationFile.parent}")
        } finally {
            runCatching { inputStream!!.close() }
            runCatching { outputStream!!.close() }
        }
    }
}