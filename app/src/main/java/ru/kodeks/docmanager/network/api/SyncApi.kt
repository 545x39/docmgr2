package ru.kodeks.docmanager.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.kodeks.docmanager.constants.Menthods.DO_SYNC
import ru.kodeks.docmanager.constants.ServiceMethod.SYNC_SVC
import ru.kodeks.docmanager.model.io.DefaultResponse

interface SyncApi : BaseApi {
    /** Запрос синхронизации ланных с сервером, как начальной (init), так и последующей.*/
    @Streaming
    @POST("${SYNC_SVC}${DO_SYNC}")
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    override fun post(
        @Body body: Any,
        @Header("Range") range: String
    ): Call<ResponseBody>

    @Streaming
    @POST("${SYNC_SVC}${DO_SYNC}")
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    override fun postDeferred( //@Url url: String,
        @Body body: Any,
        @Header("Range") range: String
    ): Call<DefaultResponse>
}