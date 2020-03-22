package ru.kodeks.docmanager.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.kodeks.docmanager.constants.ServiceMethod.SYNC_SVC
import ru.kodeks.docmanager.model.io.ChkStateResponse
import ru.kodeks.docmanager.model.io.DefaultResponse

interface Api {

    /** http://www.jsonschema2pojo.org/ */
    /** Passing "Accept-Encoding: gzip" explicitly tells OkHTTP that we want to implement our own decompression, otherwise it will decompress response for us.*/
    @POST("$SYNC_SVC/ChkState")
    @Headers("Content-Type: Application/Raw", "Accept-Encoding: gzip,deflate", "Accept: application/json;charset=utf-8", "Cache-Control: max-age=640000")
    fun checkAESState(): Call<ChkStateResponse>

    /**
     * "." Means root
     */
    @GET(".")
    @Headers("Content-Type: Application/Raw", "Accept-Encoding: gzip,deflate", "Accept: application/json;charset=utf-8", "Cache-Control: max-age=640000")
    fun requestRoot(): Call<ResponseBody>

    @Streaming
    @POST
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun post(@Url url: String, @Body body: Any, @Header("Range") range: String = "bytes=0-"): Call<ResponseBody>

    /**
     * Когда связи нет и адрес задан именем, а не IP, выбрасывается UnknownHostException.
     * В аналогичном случае, если адрес задан IP, выбрасывается ConnectException
     */
    @Streaming
    @GET
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun get(@Url url: String, @Header("Range") range: String = "bytes=0-"): Call<ResponseBody>

    @Streaming
    @POST
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun postDeferred(@Url url: String, @Body body: Any, @Header("Range") range: String = "bytes=0-"): Call<DefaultResponse>

    @POST
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    //TODO change default URL to that from settings
    fun checkState(@Url url: String = "http://172.16.1.61/tek_sm/sync.svc/ChkState"): Call<ChkStateResponse>
}