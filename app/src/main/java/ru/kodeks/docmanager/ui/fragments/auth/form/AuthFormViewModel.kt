package ru.kodeks.docmanager.ui.fragments.auth.form

import androidx.lifecycle.ViewModel
import ru.kodeks.docmanager.repository.UserRepository
import javax.inject.Inject

class AuthFormViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var userRepository: UserRepository

    fun autoLogin(enable: Boolean){
        userRepository.setAutoLogin(enable)
    }
}