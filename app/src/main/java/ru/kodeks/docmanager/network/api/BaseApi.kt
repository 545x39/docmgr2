package ru.kodeks.docmanager.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.kodeks.docmanager.constants.JsonNames.REQUEST_KEY
import ru.kodeks.docmanager.constants.Menthods.CHECK_STATE
import ru.kodeks.docmanager.constants.ServiceMethod.GET_DEFERRED_RESPONSE_URL_PATH
import ru.kodeks.docmanager.constants.ServiceMethod.SYNC_SVC
import ru.kodeks.docmanager.model.io.ChkStateResponse
import ru.kodeks.docmanager.model.io.DefaultResponse

/** http://www.jsonschema2pojo.org/ */
/** Passing "Accept-Encoding: gzip" explicitly tells OkHTTP that we
 * want to implement our own decompression, otherwise it will decompress response for us.*/
/**
 * Когда связи нет и адрес задан именем, а не IP, выбрасывается UnknownHostException.
 * В аналогичном случае, если адрес задан IP, выбрасывается ConnectException
 */

/** Базовый API, который может быть нужен в разных компонентах приложения. В основном это всё,
 * что касается синхронизации.*/
interface BaseApi {

    /** Метод проверки сервиса на предмет наличия настройки шифрования пароля и значения этой настройки.*/
    @POST("$SYNC_SVC$CHECK_STATE")
    @Headers(
        "Content-Type: Application/Raw",
        "Accept-Encoding: gzip,deflate",
        "Accept: application/json;charset=utf-8",
        "Cache-Control: max-age=640000"
    )
    fun checkState(): Call<ChkStateResponse>

    /** "." Means root*/
    @GET(".")
    @Headers(
        "Content-Type: Application/Raw",
        "Accept-Encoding: gzip,deflate",
        "Accept: application/json;charset=utf-8",
        "Cache-Control: max-age=640000"
    )
    fun requestRoot(): Call<ResponseBody>

    @Streaming
    @POST
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun post(
//        @Url url: String,
        @Body body: Any,
        @Header("Range") range: String = "bytes=0-"
    ): Call<ResponseBody>


    @Streaming
    @GET
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun get(
        @Url url: String,
        @Header("Range") range: String = "bytes=0-"
    ): Call<ResponseBody>

    /** Отправка POST-запроса при runDeferred = true. Ответ будет содержать DefaultResponse,
     * в коттором в основном сожержатся данные пользователя, а также requestKey, с которым
     * и нужно будет запрашивать готовый ответ посредством getDeferred().*/
    @Streaming
    @POST
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun postDeferred( //@Url url: String,
        @Body body: Any,
        @Header("Range") range: String = "bytes=0-"
    ): Call<DefaultResponse>

    /** Получение отложенного ответа по ключу requestKey.*/
    @Streaming
    @GET(GET_DEFERRED_RESPONSE_URL_PATH)
    @Headers("Content-Type: Application/Raw", "Cache-Control: max-age=640000")
    fun getDeferred(@Query(REQUEST_KEY) key: String): Call<ResponseBody>
}