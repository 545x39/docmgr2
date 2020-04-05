package ru.kodeks.docmanager.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import ru.kodeks.docmanager.constants.Settings.AUTO_LOGIN
import ru.kodeks.docmanager.model.data.User
import ru.kodeks.docmanager.network.resource.UserStateResource
import ru.kodeks.docmanager.persistence.Database
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    var database: Database,
    var preferences: SharedPreferences
) {

    @Inject
    lateinit var syncStateRepository: SyncStateRepository

    val autoLogin: Boolean get() = preferences.getBoolean(AUTO_LOGIN, false)

    fun setAutoLogin(enable: Boolean) {
        preferences.edit().putBoolean(AUTO_LOGIN, enable).apply()
    }

    /** It's going to observe SyncStatus to uplate user as sync finishes.*/
    private var cachedUser: MediatorLiveData<UserStateResource> = MediatorLiveData()

    /** The point is to let observers subscribe on immutable version of cachedUser only.*/
    fun getUser(): LiveData<UserStateResource> {
        return cachedUser
    }

    /** login: String = "meyksin", password: String = "11111"*/
    fun logIn(login: String? = null, password: String? = null) {

        suspend fun onNotInitialized() {
            if (login == null && password == null) {
                notInitialized()
            } else {
                if (login?.isNotBlank() == true && password?.isNotBlank() == true) {
                    initialize(login, password)
                } else {
                    error(IllegalArgumentException("Логин и пароль не могут быть пустыми"))
                }
            }
        }

        suspend fun onInitialized(user: User) {
            if (login == null && password == null) {
                if (autoLogin) {
                    loggedIn(user)
                } else {
                    loggedOut()
                }
            } else {
                if (login.equals(user.login) && password.equals(user.password)) {
                    loggedIn(user)
                } else {
                    error(IllegalArgumentException("Неверный логин или пароль!"))
                }
            }
        }

        CoroutineScope(IO).launch {
            when (val user = database.userDAO().getUser()) {
                null -> {
                    onNotInitialized()
                }
                /**There is an initialized user in the database */
                else -> {
                    onInitialized(user)
                }
            }
        }
    }

    private fun notInitialized() {
        cachedUser.postValue(UserStateResource.NotInitialized())
    }

    fun logOut() {
        loggedOut()
    }

    private fun loggedIn(user: User) {
        cachedUser.postValue(UserStateResource.LoggedIn(user))
    }

    private fun loggedOut() {
        cachedUser.postValue(UserStateResource.LoggedOut(cachedUser.value?.user))
    }

    private fun error(throwable: Throwable) {
        cachedUser.postValue(UserStateResource.Error(cachedUser.value?.user, throwable))
    }

    private fun initialize(login: String, password: String) {
        cachedUser.postValue(UserStateResource.Synchronizing(message = "Ожидание ответа от сервера"))
        //TODO Здесь вся логика инициализации: обновление статуса инициализации, сам синк и обновление после завершения.
    }
}