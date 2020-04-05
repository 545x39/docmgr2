package ru.kodeks.docmanager.network.resource

sealed class SyncStateResource
//<T>
    (
//    val data: T? = null,
    val message: String? = null,
    val error: Throwable? = null
) {
    class InProgress
//    <T>
        (
//        data: T? = null,
        message: String) :
        SyncStateResource
//        <T>
    (
//            data,
            message = message)

    class Error
//    <T>
        (
//        data: T? = null,
        error: Throwable) : SyncStateResource
//    <T>
        (
//        data,
        error = error)
    class SuccessS
//    <T>
        (
//        data: T? = null
    ) : SyncStateResource
//    <T>
        (
//        data
    )
}
