package ru.kodeks.docmanager.repository.resource

sealed class SyncStateResource<T>
    (
    val data: T? = null,
    val message: String? = null,
    val error: Throwable? = null
) {
    class Active<T>(data: T? = null, message: String) :
        SyncStateResource<T>(data, message = message)
    //    class NotEnqueued<T>(data: T? = null) : SyncStateResource<T>(data)
    class Inactive<T>(data: T? = null, error: Throwable) : SyncStateResource<T>(data, error = error)

    class Success<T>(data: T? = null) : SyncStateResource<T>(data)
    class SuccesssWithErrors<T>(data: T? = null) : SyncStateResource<T>(data)
    class Failure<T>(data: T? = null, error: Throwable) : SyncStateResource<T>(data, error = error)
}

