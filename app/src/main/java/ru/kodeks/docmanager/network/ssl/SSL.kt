@file:Suppress("DEPRECATION")

package ru.kodeks.docmanager.network.ssl

import android.util.Log
import ru.kodeks.docmanager.util.tools.stackTraceToString
import ru.kodeks.docmanager.util.DocManagerApp
import ru.kodeks.docmanager.constants.LogTag.TAG
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
import javax.net.ssl.*


const val SSL_CERTIFICATE_PASSWORD = "SSLCertificatePassword"
const val USE_SSL_CERT_SETTING = "useSSLCertificate"
const val CERT_FILENAME = "downloaded_ssl_cert.p12"
const val TYPE = "PKCS12"
const val PROVIDER = "BC"
var SSL_CERTIFICATE_FILE =
    DocManagerApp.instance.applicationInfo.dataDir + File.separator + CERT_FILENAME

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

    private var sslContext: SSLContext = SSLContext.getInstance("TLS")

    init {
        val preferences = DocManagerApp.instance.preferences
        try {
            var password: String? = null
            val keyStorePath =
                DocManagerApp.instance.applicationInfo.dataDir + File.separator + CERT_FILENAME
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
            try {
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
            } catch (e: Exception) {
                Log.d(TAG, stackTraceToString(e))
                return null
            }
        }

        fun getUnsafeSocketFactory(): SSLSocketFactory? {
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(TrustAllCertsTrustManager())
                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                return sslContext.socketFactory
            } catch (e: Exception) {
                //Log.e("TAG", "getUnsafeOkHttpClient: " + StringTools.stackTraceToString(e))
            }
            return null
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////
class TrustAllCertsTrustManager constructor() : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

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
        //TODO COMMENT THIS LOGGING
        for (certificate in chain) {
//            Log.d(TAG, "CERT:: $certificate")
        }
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
            list.addAll(Arrays.asList(*tm.acceptedIssuers))
        return list.toTypedArray()
    }
}