package ru.kodeks.docmanager.ui.fragments.auth.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import ru.kodeks.docmanager.model.data.User
import ru.kodeks.docmanager.repository.resource.UserStateResource
import ru.kodeks.docmanager.ui.main.BaseViewModel
import javax.inject.Inject

open class AuthViewModel @Inject constructor() : BaseViewModel() {

    private var progress: MutableLiveData<Int> = MutableLiveData()

    fun getProgress(): LiveData<Int> {
        return progress
    }

    fun getAutoLoginUser(): Boolean {
        return userRepository.autoLogin
    }

    fun autoLogin(enable: Boolean) {
        userRepository.setAutoLogin(enable)
    }

    /** Вызывается при нажатии на кнопку "Войти".*/
    fun logIn(login: String, password: String) {
        fun onWrongCredentials(error: String = "Логин и пароль не могут быть пустыми") {
            userRepository.error(IllegalArgumentException(error))
        }

        fun onInitialized(user: User?) {
            // Что-то ввели в поля логина и пароля, можно запускать.
            if (!login.isBlank() && !password.isBlank()) {
                //Ввели правильные данные
                if (user?.login == login && user.password == password) {
                    userRepository.loggedIn(user)
                } else {
                    //Ввели неправлиьные данные
                    onWrongCredentials("Неверный логин или пароль!")
                }
            } else {
                // Какие-то из полей пустые
                onWrongCredentials()
            }
        }

        fun onNotInitialized() {
            when (!login.isBlank() && !password.isBlank()) {
                true -> syncRepository.sync(login, password)
                false -> onWrongCredentials()
            }
        }

        when (val value = userRepository.getUser().value) {
            is UserStateResource.NotInitialized -> {
                onNotInitialized()
            }
            is UserStateResource.LoggedOut -> {
                onInitialized(value.user)
            }
        }
    }

    private var job: Job = CoroutineScope(Dispatchers.Default).launch {
        var value = 0
        while (true) {
            if (value == 100) {
                value = 0
            }
            value += 1
            withContext(Dispatchers.Main) {
                progress.value = value
            }
            delay(25)
        }
    }

    override fun onCleared() {
        job.cancel()
    }
}