package ru.kodeks.docmanager.di.module

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import androidx.work.WorkManager
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.const.Network
import ru.kodeks.docmanager.const.Network.CONNECT_TIMEOUT
import ru.kodeks.docmanager.const.Network.READ_TIMEOUT
import ru.kodeks.docmanager.const.PathsAndFileNames.DB_DIRECTORY
import ru.kodeks.docmanager.const.PathsAndFileNames.RESPONSE_DIRECTORY
import ru.kodeks.docmanager.const.PathsAndFileNames.STAMP_DIRECTORY
import ru.kodeks.docmanager.const.Settings
import ru.kodeks.docmanager.const.Settings.SslSettings.SSL_CERTIFICATE_PASSWORD
import ru.kodeks.docmanager.const.Settings.SslSettings.USE_SSL_CERT_SETTING
import ru.kodeks.docmanager.db.Database
import ru.kodeks.docmanager.di.const.BASE_URL
import ru.kodeks.docmanager.di.const.RESPONSE_DIR
import ru.kodeks.docmanager.di.const.STAMP_DIR
import ru.kodeks.docmanager.network.api.BaseApi
import ru.kodeks.docmanager.network.api.GetSignatureStampApi
import ru.kodeks.docmanager.network.api.SyncApi
import ru.kodeks.docmanager.util.crypto.ssl.CERT_FILENAME
import ru.kodeks.docmanager.util.crypto.ssl.ClientKeyStoreTrustManager
import ru.kodeks.docmanager.util.crypto.ssl.TrustAllCertsTrustManager
import ru.kodeks.docmanager.util.crypto.ssl.TrustStoreFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named

/** Модуль для всего, что будет использоваться глобально на уровне
 * приложения: Room, Retrofit  и т.д. */
@Module()
class ApplicationModule {

    @Provides
    fun providePreferences(app: DocManagerApp): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    @Named(BASE_URL)
    fun getBaseUrl(preferences: SharedPreferences) = preferences.getString(
        Settings.SERVER_ADDRESS,
        Network.DEFAULT_URL
    ) ?: Network.DEFAULT_URL


    @Provides
    @Named(RESPONSE_DIR)
    fun provideResponseDir(app: DocManagerApp): String {
        return "${app.getExternalFilesDir(RESPONSE_DIRECTORY)}"
    }

    @Provides
    @Named(STAMP_DIR)
    fun provideSignatureStampDir(app: DocManagerApp): String {
        return "${app.getExternalFilesDir(STAMP_DIRECTORY)}"
    }

    @Provides
    fun provideRetrofit(app: DocManagerApp, preferences: SharedPreferences): Retrofit {
        //<editor-fold defaultstate="collapsed" desc="Vals and methods">
        val sslCertificatePath = "${app.applicationInfo.dataDir}${File.separator}${CERT_FILENAME}"

        fun getUrl(): String {
            return preferences.getString(
                Settings.SERVER_ADDRESS,
                Network.DEFAULT_URL
            ) ?: Network.DEFAULT_URL
        }

        fun OkHttpClient.Builder.addLoggingInterceptor() {
            @Suppress("ConstantConditionIf")
            if (false) {
                /** Turn off to avoid OutOfMemoryError with a huge response.*/
                addInterceptor(
                    HttpLoggingInterceptor()
                        .also { it.level = HttpLoggingInterceptor.Level.BODY }
                )
            }
        }

        fun OkHttpClient.Builder.applySSL() {
            when (File(sslCertificatePath).exists() && preferences.getBoolean(
                USE_SSL_CERT_SETTING,
                false
            )) {
                true -> {
                    TrustStoreFactory.getClientTrustStoreFactory(
                        sslCertificatePath,
                        when (preferences.getBoolean(
                            USE_SSL_CERT_SETTING,
                            false
                        )) {
                            true -> preferences.getString(
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
        }

        fun Retrofit.Builder.okHttpClient() {
            OkHttpClient.Builder().apply {
                connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
                addLoggingInterceptor()
                applySSL()
            }.build().also { client(it) }
        }
        // </editor-fold>
        return Retrofit.Builder().apply {
            baseUrl(getUrl())
            okHttpClient()
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            addConverterFactory(ScalarsConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }.build()
    }

    @Provides
    fun provideBaseApi(retrofit: Retrofit): BaseApi = retrofit.create(BaseApi::class.java)

    @Provides
    fun provideGetStampsApi(retrofit: Retrofit): GetSignatureStampApi =
        retrofit.create(GetSignatureStampApi::class.java)

    @Provides
    fun provideSyncApi(retrofit: Retrofit): SyncApi =
        retrofit.create(SyncApi::class.java)

    @Provides
    fun provideRoom(app: DocManagerApp) = Room.databaseBuilder(
        app,
        Database::class.java,
        "${app.getExternalFilesDir(DB_DIRECTORY)}${File.separator}db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideWorkManager(app: DocManagerApp) = WorkManager.getInstance(app)
}