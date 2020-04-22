package ru.kodeks.docmanager.exceptions

import timber.log.Timber

open class LoggingException(message: String? = null, cause: Throwable? = null) :
    Exception(message, cause) {
    init {
        log()
    }

    private fun log() {
        Timber.e(this)
    }
}

class NoInternetException(message: String? = null, cause: Throwable? = null) :
    LoggingException(message, cause)

class PasswordEncryptionException(message: String? = null, cause: Throwable? = null) :
    LoggingException(message, cause)

class ErrorInResponseDataException(message: String? = null) :
    LoggingException(message)