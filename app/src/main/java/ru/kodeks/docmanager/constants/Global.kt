package ru.kodeks.docmanager.constants


object LogTag {
    /**
     * Дефолтный тэг.
     */
    const val TAG = "TAG"
}

sealed class GlobalConstant

object Paths : GlobalConstant() {
    //TODO Move it from here
    const val DEVICE_TYPE = "Android"
    ////
    const val APP_FILES_DIRECTORY_NAME = "DocManager"
    const val RESPONSE_DIRECTORY = "responses"
    const val DB_DIRECTORY = "db"
}

object ResponseFileNames : GlobalConstant(){
    const val RESPONSE_FILENAME = "response.json"
    const val SYNC_RESPONSE_FILENAME = "sync_response.json"
    const val BACKGROUND_SYNC_RESPONSE_FILENAME = "background_sync_response.json"
    const val ATTACHMENT_DEFERRED_RESPONSE_FILENAME = "attachment_response.json"
}