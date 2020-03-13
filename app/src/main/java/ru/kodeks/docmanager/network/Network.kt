package ru.kodeks.docmanager.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.kodeks.docmanager.constants.Settings.SERVER_ADDRESS
import ru.kodeks.docmanager.network.ssl.*
import ru.kodeks.docmanager.util.DocManagerApp
import java.io.File
import java.util.concurrent.TimeUnit

const val TIMEOUT = 120L

class Network private constructor() {

    val url: String
        get() {
//            val default = "http://172.16.1.61/tek_sm/"
            val default = "http://172.16.33.86/servicemobile/"
//            val default = "https://my-json-server.typicode.com/545x39/jsontest/"
//            UrlValidator().isValid(DocManagerApp.instance.preferences.getString(SERVER_ADDRESS, "http://172.16.1.61/tek_sm/"))
            return DocManagerApp.instance.preferences.getString(SERVER_ADDRESS, default) ?: default
        }

    var api = api()

    private fun api() = with(Retrofit.Builder()) {
        baseUrl(url)
        addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        addConverterFactory(ScalarsConverterFactory.create())
        client(with(OkHttpClient.Builder()) {
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            if (false) {
                /** This is better be off in prod due to possible occurrences of OutOfMemoryError when response body is huge.*/
                addInterceptor(HttpLoggingInterceptor()
                    .also { it.level = HttpLoggingInterceptor.Level.BODY }
                )
            }
            when (File(SSL_CERTIFICATE_FILE).exists() && DocManagerApp.instance.preferences.getBoolean(
                USE_SSL_CERT_SETTING,
                false
            )) {
                true -> {
                    TrustStoreFactory.getClientTrustStoreFactory(
                        SSL_CERTIFICATE_FILE,
                        when (DocManagerApp.instance.preferences.getBoolean(
                            USE_SSL_CERT_SETTING,
                            false
                        )) {
                            true -> DocManagerApp.instance.preferences.getString(
                                SSL_CERTIFICATE_PASSWORD,
                                ""
                            )
                            false -> null
                        }
                    )?.let { sslSocketFactory(it, ClientKeyStoreTrustManager()) }
                }
                false -> {
                    TrustStoreFactory.getUnsafeSocketFactory()?.let {
                        sslSocketFactory(it, TrustAllCertsTrustManager())
                    }
                }
            }
            build()
        })
        build()
    }.create(API::class.java) ?: fail()

    private fun fail(): Nothing {
        throw InstantiationException("Couldn't initialize retrofit")
    }

    fun reset() {
        api = api()
    }

    companion object {
        //        @Volatile
        var INSTANCE = Network()

        fun reset() {
            INSTANCE.api = Network().api()
        }
    }
}