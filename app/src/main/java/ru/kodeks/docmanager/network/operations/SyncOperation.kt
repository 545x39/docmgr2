package ru.kodeks.docmanager.network.operations

import okhttp3.ResponseBody
import retrofit2.Response
import ru.kodeks.docmanager.constants.PathsAndFileNames.SYNC_RESPONSE_FILENAME
import ru.kodeks.docmanager.constants.ServiceMethod
import ru.kodeks.docmanager.model.io.SyncRequest
import ru.kodeks.docmanager.persistence.parser.Parser
import ru.kodeks.docmanager.persistence.parser.write
import timber.log.Timber
import java.io.File
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Named

class SyncOperation(request: SyncRequest) : Operation<SyncRequest>(request) {

    @Inject
    @field:Named("responseDir")
    lateinit var responseDirectory: String

    override fun getUrl(): String = "$serverUrl${ServiceMethod.SYNC_SVC}/DoSync"

    override fun parseResponse(response: Response<ResponseBody>) {
        val responseFile =
            File("$responseDirectory${File.separator}${SYNC_RESPONSE_FILENAME}")
        response.body()?.apply {
            val writeResult = write(
                this,
                responseFile,
                outputEncoding = StandardCharsets.UTF_8
            )
            Timber.e("Write succeeded? $writeResult")
        } ?: throw IllegalStateException("Response body was NULL.")
        with(responseFile) {
            when (exists()) {
                true -> {
                    try {
                        Parser().parse()
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
                false -> throw IllegalStateException("No response file found.")
            }
        }
    }
}