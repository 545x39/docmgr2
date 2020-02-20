package ru.kodeks.docmanager.network.operations

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.constants.LogTag.TAG
import ru.kodeks.docmanager.constants.ResponseFileNames
import ru.kodeks.docmanager.constants.ServiceMethod
import ru.kodeks.docmanager.model.io.SyncRequest
import ru.kodeks.docmanager.network.Network
import ru.kodeks.docmanager.network.Parser
import ru.kodeks.docmanager.network.util.write
import ru.kodeks.docmanager.util.DocManagerApp
import ru.kodeks.docmanager.util.tools.stackTraceToString
import java.io.File
import java.nio.charset.StandardCharsets

class SyncOperation(request: SyncRequest) : Operation<SyncRequest>(request) {

    override fun getUrl(): String = "${Network.INSTANCE.url}${ServiceMethod.SYNC_SVC}/DoSync"

    override fun parseResponse(response: Response<ResponseBody>) {
        val responseFile =
            File("${DocManagerApp.instance.responseDirectory}${File.separator}${ResponseFileNames.SYNC_RESPONSE_FILENAME}")
        response.body()?.apply {
            val writeResult = write(this, responseFile, outputEncoding = StandardCharsets.UTF_8)
            Log.e(TAG, "Write succeeded? $writeResult")
        } ?: throw IllegalStateException("Response body was NULL.")
        with(responseFile) {
            when (exists()) {
                true -> {
                    try {
                        Parser().parse()
                    } catch (e: Exception) {
                        Log.e(TAG, stackTraceToString(e))
                    }
                }
                false -> throw IllegalStateException("No response file found.")
            }
        }
    }
}