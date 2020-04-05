package ru.kodeks.docmanager.ui.fragments.auth.form

import ru.kodeks.docmanager.ui.fragments.auth.base.BaseAuthViewModel
import javax.inject.Inject

class AuthFormViewModel @Inject constructor() : BaseAuthViewModel() {

    fun autoLogin(enable: Boolean){
        userRepository.setAutoLogin(enable)
    }
}