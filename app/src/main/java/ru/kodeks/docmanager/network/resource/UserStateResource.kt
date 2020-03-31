package ru.kodeks.docmanager.network.resource

import ru.kodeks.docmanager.model.data.User

/** Ресурс - в данном случае sealed-класс, агрегирующий данные (в данном случае
 * пользователя) и предоставляющий ряд подклассов, соответствующих  всем
 * возможным состояниям агрегируемых данных.*/
sealed class UserStateResource(
    val user: User? = null,
    val message: String = "",
    val error: Throwable? = null
) {
    /** База пуста, требудется начальная синхронизация.*/
    class NotInitialized(user: User? = null) : UserStateResource(user)

    /** Первичная синхронизация в процессе.*/
    class Synchronizing(user: User? = null, message: String) : UserStateResource(user, message)

    /** Пользователь залогинен и может работать.*/
    class LoggedIn(user: User? = null) : UserStateResource(user)

    /** База есть,*/
    class LoggedOut(user: User? = null) : UserStateResource(user)

    /** Ошибки: не удалось проинициализировать пользователя, сменился пароль и т.п.*/
    class Error(user: User? = null, error: Throwable) : UserStateResource(user, error = error)
}