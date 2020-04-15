package ru.kodeks.docmanager.repository.resource

import ru.kodeks.docmanager.model.data.User

/** Ресурс, описывающий возможные состояния пользователя: Не проинициализирован, залогинен, разлогинен.*/
sealed class UserStateResource(
    val user: User? = null,
    var message: String = "",
    var error: Throwable? = null
) {
    /** База пуста, требудется начальная синхронизация.*/
    class NotInitialized(user: User? = null) : UserStateResource(user)

    /** Пользователь залогинен и может работать.*/
    class LoggedIn(user: User? = null, message: String = "", error: Throwable? = null) :
        UserStateResource(user, message, error)

    /** База есть, но не поднят флаг "запомнить пароль".*/
    class LoggedOut(user: User? = null, message: String = "", error: Throwable? = null) :
        UserStateResource(user, message, error)

}