@file:Suppress("DEPRECATION")

package ru.kodeks.docmanager.network.ssl

import android.annotation.SuppressLint
import android.content.SharedPreferences
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.constants.Settings.SslSettings.SSL_CERTIFICATE_PASSWORD
import ru.kodeks.docmanager.constants.Settings.SslSettings.USE_SSL_CERT_SETTING
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.inject.Inject
import javax.net.ssl.*


const val CERT_FILENAME = "downloaded_ssl_cert.p12"
const val TYPE = "PKCS12"
const val PROVIDER = "BC"

fun loadKeyStore(keyStorePath: String, keyStorePassword: String?): KeyStore? {
    var keyStore: KeyStore? = null
    try {
        if (!File(keyStorePath).exists()) {
            return null
        }
        BufferedInputStream(FileInputStream(keyStorePath)).use { caInput ->
            keyStore = KeyStore.getInstance(TYPE, PROVIDER)
            keyStore!!.load(caInput, keyStorePassword?.toCharArray() ?: "".toCharArray())
        }
    } catch (e: Exception) {
        keyStore = null
    }
    return keyStore
}

//////////////////////////
@Suppress("DEPRECATION")
class ApacheClientStoreSocketFactory @Throws(
    NoSuchAlgorithmException::class,
    KeyManagementException::class,
    KeyStoreException::class,
    UnrecoverableKeyException::class
)
constructor(trustedKeyStore: KeyStore) :
    org.apache.http.conn.ssl.SSLSocketFactory(trustedKeyStore) {
    @Inject
    lateinit var app: DocManagerApp

    @Inject
    lateinit var preferences: SharedPreferences
    private var sslContext: SSLContext = SSLContext.getInstance("TLS")

    init {
        try {
            var password: String? = null
            val keyStorePath = app.applicationInfo.dataDir + File.separator + CERT_FILENAME
            if (preferences.getBoolean(USE_SSL_CERT_SETTING, false)) {
                password = preferences.getString(SSL_CERTIFICATE_PASSWORD, "")
            }
            val keyStore = loadKeyStore(keyStorePath, password)

            /** Create a TrustManager that trusts the CAs in our KeyStore  */
            //            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            val tmf = TrustManagerFactory.getInstance("PKIX"/*tmfAlgorithm//, "BC"*/)
            tmf.init(keyStore)
            val kmf =
                KeyManagerFactory.getInstance("PKIX"/*KeyManagerFactory.getDefaultAlgorithm()//,"BC"*/)
            kmf.init(keyStore, "".toCharArray())
            kmf.init(keyStore, password!!.toCharArray())
            sslContext.init(
                kmf.keyManagers,
                arrayOf<TrustManager>(ClientKeyStoreTrustManager(keyStore)),
                SecureRandom()
            )
        } catch (e: Exception) {
            //            Log.d("TAG", "LOADING SSL FAILED:   " + StringTools.stackTraceToString(e));
        }
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(socket: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return sslContext.socketFactory.createSocket(socket, host, port, autoClose)
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        return sslContext.socketFactory.createSocket()
    }
}

//////////////////////////
class ApacheUnsafeSocketFactory @Throws(
    NoSuchAlgorithmException::class,
    KeyManagementException::class,
    KeyStoreException::class,
    UnrecoverableKeyException::class
)
constructor(trustedKeyStore: KeyStore) :
    org.apache.http.conn.ssl.SSLSocketFactory(trustedKeyStore) {
    private var sslContext = SSLContext.getInstance("TLS")

    init {
        val tm = TrustAllCertsTrustManager()
        sslContext.init(null, arrayOf<TrustManager>(tm), null)
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(socket: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return sslContext.socketFactory.createSocket(socket, host, port, autoClose)
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        return sslContext.socketFactory.createSocket()
    }
}

class TrustStoreFactory {

    companion object {
        fun getClientTrustStoreFactory(
            keyStorePath: String,
            keyStorePassword: String?
        ): SSLSocketFactory? {
            runCatching {
                val keyStore = loadKeyStore(keyStorePath, keyStorePassword) ?: return null

                /** Create a TrustManager that trusts the CAs in our KeyStore  */
//                val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
                val tmf = TrustManagerFactory.getInstance("PKIX" /*tmfAlgorithm*/)
                tmf.init(keyStore)
                /** Create an SSLContext that uses our TrustManager  */
                val sslContext = SSLContext.getInstance("TLS")
                val kmf =
                    KeyManagerFactory.getInstance("PKIX"/*KeyManagerFactory.getDefaultAlgorithm()*/)
                kmf.init(keyStore, "".toCharArray())
                kmf.init(keyStore, keyStorePassword?.toCharArray() ?: "".toCharArray())
                sslContext.init(
                    kmf.keyManagers,
                    arrayOf<TrustManager>(ClientKeyStoreTrustManager(keyStore)),
                    SecureRandom()
                )
                return sslContext.socketFactory
            }.onFailure {
                Timber.d(it)
            }
            return null
        }

        fun getUnsafeSocketFactory(): SSLSocketFactory? {
            runCatching {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(TrustAllCertsTrustManager())
                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                return sslContext.socketFactory
            }.onFailure {
                Timber.d(it)
            }
            return null
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
class TrustAllCertsTrustManager : X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }
}

class ClientKeyStoreTrustManager constructor(vararg additionalKeyStores: KeyStore?) :
    X509TrustManager {
    private var x509TrustManagers = ArrayList<X509TrustManager>()

    init {
        val factories = ArrayList<TrustManagerFactory>()
        try {
            // The default Trustmanager with default keystore
            val original =
                TrustManagerFactory.getInstance("PKIX" /*TrustManagerFactory.getDefaultAlgorithm()*/)
            original.init(null as KeyStore?)
            factories.add(original)
            for (keyStore in additionalKeyStores) {
                val additionalCerts =
                    TrustManagerFactory.getInstance("PKIX"/*TrustManagerFactory.getDefaultAlgorithm()*/)
                additionalCerts.init(keyStore)
                factories.add(additionalCerts)
            }

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        /**
         * Iterate over the returned trustmanagers, and hold on to any that are X509TrustManagers
         */
        for (tmf in factories)
            for (tm in tmf.trustManagers)
                if (tm is X509TrustManager) {
                    x509TrustManagers.add(tm)
                }
        if (x509TrustManagers.size == 0) {
            throw RuntimeException("Couldn't find any X509TrustManagers")
        }
    }

    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        for (tm in x509TrustManagers) {
            try {
                tm.checkClientTrusted(chain, authType)
                return
            } catch (e: CertificateException) {

            }

        }
        throw CertificateException()
    }

    /**
     * Loop over the trust managers until we find one that accepts our server
     */
    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
//        for (certificate in chain) {
//            Timber.d("CERT:: $certificate")
//        }
        for (tm in x509TrustManagers) {
            try {
                tm.checkServerTrusted(chain, authType)
                return
            } catch (e: Exception) {
                //
            }

        }
        throw CertificateException()
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        val list = ArrayList<X509Certificate>()
        for (tm in x509TrustManagers)
            list.addAll(listOf(*tm.acceptedIssuers))
        return list.toTypedArray()
    }
}