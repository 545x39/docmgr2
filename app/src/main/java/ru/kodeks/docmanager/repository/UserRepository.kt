package ru.kodeks.docmanager.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kodeks.docmanager.const.Settings.AUTO_LOGIN
import ru.kodeks.docmanager.const.Settings.LAST_ENTERED_LOGIN
import ru.kodeks.docmanager.const.Settings.LAST_ENTERED_PASSWORD
import ru.kodeks.docmanager.db.Database
import ru.kodeks.docmanager.model.data.User
import ru.kodeks.docmanager.repository.resource.UserStateResource
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(database: Database, preferences: SharedPreferences) :
    AbstractRepository(database, preferences) {

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

    fun setState(state: UserStateResource) {
        cachedUser.postValue(state)
    }

    private fun setCredenntials(login: String?, password: String) {
        preferences.edit().putString(LAST_ENTERED_LOGIN, login.orEmpty())
            .putString(LAST_ENTERED_PASSWORD, password.orEmpty())
            .apply()
    }

    private fun getLatEnteredCredentials(): Pair<String, String> {
        return preferences.getString(LAST_ENTERED_LOGIN, "") to
                preferences.getString(LAST_ENTERED_PASSWORD, "")
    }

    /** Начальное состояние пользователя. */
    init {
        CoroutineScope(Dispatchers.IO).launch {
            when (val user = database.userDAO().getUser()) {
                null -> {
                    notInitialized()
                }
                /**There is an initialized user in the database */
                else -> {
                    with(getLatEnteredCredentials()) {
                        when (user.login == first
                                && user.password == second
                                && autoLogin
                            ) {
                            true -> loggedIn(user)
                            false -> loggedOut(user)
                        }
                    }
                }
            }
        }
    }

    private fun notInitialized() {
        cachedUser.postValue(UserStateResource.NotInitialized())
    }

    fun loggedIn(user: User? = null) {
        Timber.e("loggedIn")
        val newValue = user ?: cachedUser.value?.user
        setCredenntials(newValue?.login, newValue?.password.orEmpty())
        cachedUser.postValue(UserStateResource.LoggedIn(newValue))
    }

    fun loggedOut(user: User? = null) {
        setCredenntials("", "")
        val newValue = user ?: cachedUser.value?.user
        cachedUser.postValue(UserStateResource.LoggedOut(newValue))
    }

    fun error(throwable: Throwable) {
        val newValue = cachedUser.value
        newValue?.let {
            it.error = throwable
            cachedUser.postValue(it)
        }
    }
}