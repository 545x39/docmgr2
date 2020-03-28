package ru.kodeks.docmanager.network.resource

//https://developer.android.com/jetpack/docs/guide#best-practices
/** A generic class that contains data and status about loading
 * this data.*/
sealed class SyncStatusResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : SyncStatusResource<T>(data)
    class Loading<T>(data: T? = null) : SyncStatusResource<T>(data)
    class Error<T>(message: String, data: T? = null) : SyncStatusResource<T>(data, message)
}
