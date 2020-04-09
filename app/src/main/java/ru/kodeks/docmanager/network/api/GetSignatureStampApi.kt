package ru.kodeks.docmanager.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.kodeks.docmanager.const.Menthods
import ru.kodeks.docmanager.const.ServiceMethod
import ru.kodeks.docmanager.model.io.DefaultResponse

interface GetSignatureStampApi : BaseApi {

    @Streaming
    @POST("${ServiceMethod.DOCS_SVC}${Menthods.GET_SIGNATURE_STAMP}")
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    override fun post(
        @Body body: Any,
        @Header("Range") range: String
    ): Call<ResponseBody>

    @Streaming
    @POST("${ServiceMethod.DOCS_SVC}${Menthods.GET_SIGNATURE_STAMP}")
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    override fun postDeferred( //@Url url: String,
        @Body body: Any,
        @Header("Range") range: String
    ): Call<DefaultResponse>
}