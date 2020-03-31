package ru.kodeks.docmanager.repository

import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kodeks.docmanager.model.data.User
import ru.kodeks.docmanager.network.resource.UserStateResource
import ru.kodeks.docmanager.persistence.Database
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(var database: Database) {

    constructor(
        login: String = "meyksin",
        password: String = "11111",
        database: Database
    ) : this(database)

    var cachedUser: MediatorLiveData<UserStateResource> = MediatorLiveData()
        private set

    fun logIn() {
        //TODO Тут будет вся логика авторизации
        /** 1. Смотрим нужного пользователя в базе*/
        CoroutineScope(IO).launch {
            when(database.userDAO().getUser()){
                null ->{ withContext(Main) {
                    notInitialized()
                }}
            }
            val usr = database.userDAO().getUser()
            Timber.e("USER: $usr")
//            withContext(Main) {
//                logOut()
//            }
        }
    }

    private fun notInitialized() {
        cachedUser.value = UserStateResource.NotInitialized()
    }

    fun logOut() {
        cachedUser.value = UserStateResource.LoggedOut()
    }

    fun isInitialized(): Boolean {
        return false
    }

    fun syncError(throwable: Throwable) {
        cachedUser.value = UserStateResource.Error(cachedUser.value?.user, throwable)
    }

    fun initialize(user: User) {
        cachedUser.value = UserStateResource.Synchronizing(message = "Ожидание ответа от сервера")
    }

    fun isPasswordSaved(): Boolean {
        return false
    }
}