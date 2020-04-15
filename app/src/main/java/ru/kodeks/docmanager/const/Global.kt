package ru.kodeks.docmanager.const

sealed class GlobalConstant

object PathsAndFileNames : GlobalConstant() {
    const val APP_FILES_DIRECTORY_NAME = "DocManager"
    const val RESPONSE_DIRECTORY = "responses"
    const val STAMP_DIRECTORY = "stamps"
    const val DB_DIRECTORY = "db"
    const val SYNC_RESPONSE_FILENAME = "sync_response.json"
    const val LOGIN = "login"
    const val PASSWORD = "password"
}

object Network {
    const val DEFAULT_URL = "http://172.16.33.86/servicemobile/"
    const val DEFERRED_REQUEST_RECONNECT_PERIOD = 2000L
    const val DEFERRED_REQUEST_LIFETIME = 45 * 60 * 1000
    const val MAX_REQUEST_ATTEMPTS = 3
    const val CONNECT_TIMEOUT = 120L
    const val READ_TIMEOUT = 120L
}