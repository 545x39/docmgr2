package ru.kodeks.docmanager.ui.fragments.auth.base

import ru.kodeks.docmanager.ui.main.BaseViewModel
import javax.inject.Inject

open class BaseAuthViewModel @Inject constructor() : BaseViewModel() {

    fun getAutoLoginUser(): Boolean {
        return userRepository.autoLogin
    }

    fun logIn(login: String? = null, password: String? = null) {
        userRepository.logIn(login, password)
    }
}