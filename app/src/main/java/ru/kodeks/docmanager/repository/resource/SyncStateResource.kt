package ru.kodeks.docmanager.repository.resource

sealed class SyncStateResource<T>
    (
    val data: T? = null,
    val message: String? = null,
    val error: Throwable? = null
) {
    class InProgress<T>(data: T? = null, message: String? = null) :
        SyncStateResource<T>(data, message = message)

    /** Успешное завершение операции означает успешное получение и разбор ответа сервера. Для обработки
     * логических ошибок (например, конфликтов синхронизации) предусмотрена возможность передачи Throwable.*/
    class Success<T>(data: T? = null, error: Throwable? = null) : SyncStateResource<T>(data, error = error)

    /** В данном случае Throwable предназначен не для логических ошибок.*/
    class Failure<T>(data: T? = null, error: Throwable? = null) : SyncStateResource<T>(data, error = error)
}

//sealed class SyncStateResource(val message: String? = null, val error: Throwable? = null) {
//    class InProgress(message: String) : SyncStateResource(message = message)
//    class Inactive(error: Throwable) : SyncStateResource(error = error)
//    class Success(error: Throwable) : SyncStateResource(error = error)
//    class Failure(error: Throwable) : SyncStateResource(error = error)
//}