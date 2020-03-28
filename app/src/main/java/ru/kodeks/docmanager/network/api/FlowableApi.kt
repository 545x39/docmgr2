package ru.kodeks.docmanager.network.api

import io.reactivex.rxjava3.core.Flowable
import okhttp3.ResponseBody
import retrofit2.http.*
import ru.kodeks.docmanager.constants.Menthods
import ru.kodeks.docmanager.constants.ServiceMethod
import ru.kodeks.docmanager.model.io.DefaultResponse

interface FlowableApi {
    /** Запрос синхронизации ланных с сервером, как начальной (init), так и последующей.*/
    @Streaming
    @POST("${ServiceMethod.SYNC_SVC}${Menthods.DO_SYNC}")
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun post(
        @Body body: Any,
        @Header("Range") range: String = "bytes=0-"
    ): Flowable<ResponseBody>

    @Streaming
    @POST("${ServiceMethod.SYNC_SVC}${Menthods.DO_SYNC}")
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun postDeferred( //@Url url: String,
        @Body body: Any,
        @Header("Range") range: String
    ): Flowable<DefaultResponse>
}