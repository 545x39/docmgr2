package ru.kodeks.docmanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.kodeks.docmanager.network.resource.UserStateResource
import ru.kodeks.docmanager.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {

    fun getUser(): LiveData<UserStateResource> {
        Timber.e("USER: ${userRepository.getUser().value}")
        return userRepository.getUser()
    }

    @Inject
    protected lateinit var userRepository: UserRepository
}