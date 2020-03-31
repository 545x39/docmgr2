package ru.kodeks.docmanager.network.resource

//https://developer.android.com/jetpack/docs/guide#best-practices
/** A generic class that contains data and status about loading
 * this data.*/
sealed class SyncStatusResource<T>(
    val data: T? = null,
//    val message: String? = null
    val error: Throwable? = null
) {
    class UNINITIALIZED<T>(data: T) : SyncStatusResource<T>(data)
    class LOADING<T>(data: T? = null) : SyncStatusResource<T>(data)
    class SUCCESS<T>(data: T) : SyncStatusResource<T>(data)
    class ERROR<T>(data: T? = null, error: Throwable) : SyncStatusResource<T>(data, error)
}
