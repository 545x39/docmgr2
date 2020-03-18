package ru.kodeks.docmanager.di.modules

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.User
import ru.kodeks.docmanager.constants.Network
import ru.kodeks.docmanager.constants.Network.CONNECT_TIMEOUT
import ru.kodeks.docmanager.constants.Network.READ_TIMEOUT
import ru.kodeks.docmanager.constants.PathsAndFileNames.RESPONSE_DIRECTORY
import ru.kodeks.docmanager.constants.Settings
import ru.kodeks.docmanager.constants.Settings.SslSettings.SSL_CERTIFICATE_PASSWORD
import ru.kodeks.docmanager.constants.Settings.SslSettings.USE_SSL_CERT_SETTING
import ru.kodeks.docmanager.network.ssl.CERT_FILENAME
import ru.kodeks.docmanager.network.ssl.ClientKeyStoreTrustManager
import ru.kodeks.docmanager.network.ssl.TrustAllCertsTrustManager
import ru.kodeks.docmanager.network.ssl.TrustStoreFactory
import ru.kodeks.docmanager.persistence.Database
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/** Модуль для всего, что будет использоваться глобально на уровне приложения: Room, Retrofit  и т.д.*/
@Module
class ApplicationModule {

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var user: User

    @Provides
    fun provideRetrofit(app: DocManagerApp, preferences: SharedPreferences): Retrofit {
        val sslCertificatePath = "${app.applicationInfo.dataDir}${File.separator}${CERT_FILENAME}"
        val url = preferences.getString(
            Settings.SERVER_ADDRESS,
            Network.DEFAULT_URL
        ) ?: Network.DEFAULT_URL
        return with(Retrofit.Builder()) {
            baseUrl(url)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            addConverterFactory(ScalarsConverterFactory.create())
            client(with(OkHttpClient.Builder()) {
                connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
                @Suppress("ConstantConditionIf")
                if (false) {
                    /** Turn off to avoid OutOfMemoryError with a huge response.*/
                    addInterceptor(
                        HttpLoggingInterceptor()
                            .also { it.level = HttpLoggingInterceptor.Level.BODY }
                    )
                }
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
                build()
            })
            build()
        }.create(Retrofit::class.java)
    }

    @Provides
    fun provideRoom(app: DocManagerApp): Database {
        return Room.databaseBuilder(app, Database::class.java, user.login)
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePreferences(app: DocManagerApp): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    @Provides
//    @Named("responseDir")
    fun provideResponseDir(app: DocManagerApp): String {
        return "${app.getExternalFilesDir(RESPONSE_DIRECTORY)}"
    }
}