package ru.kodeks.docmanager.util.tools

import android.util.Log
import ru.kodeks.docmanager.util.DocManagerApp
import java.io.*

/** Just concatenate the name */
public val EXPORT_DIR_PATH =
    "${DocManagerApp.instance.filesDir.absolutePath}${File.separator}export${File.separator}"

object FileTools {

    fun CopyFile(sourceFile: File, destinationFile: File) {
        var `is`: InputStream? = null
        var os: OutputStream? = null
        try {
            `is` = FileInputStream(sourceFile)
            os = FileOutputStream(destinationFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (`is`.read(buffer).also { length = it } > 0) {
                os.write(buffer, 0, length)
            }
        } catch (e: Exception) {
            Log.e(
                "TAG",
                "Couldn't copy file " + sourceFile.name + " to " + destinationFile.parent
            )
        } finally {
            try {
                `is`!!.close()
            } catch (e: Exception) { //
            }
            try {
                os!!.close()
            } catch (e: Exception) { //
            }
        }
    }
}