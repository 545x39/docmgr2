package ru.kodeks.docmanager.util

import android.util.Log
import ru.kodeks.docmanager.constants.LogTag.TAG
import ru.kodeks.docmanager.util.tools.stackTraceToString
import java.lang.Exception

open class LoggingException(message: String? = null, cause: Throwable? = null) :
    Exception(message, cause) {
    init {
        log()
    }

    private fun log() {
        Log.e(TAG, stackTraceToString(this))
    }
}

class NoInternetException(message: String? = null, cause: Throwable? = null) :
    LoggingException(message, cause)

class PasswordEncryptionException(message: String? = null, cause: Throwable? = null) :
    LoggingException(message, cause)