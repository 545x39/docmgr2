package ru.kodeks.docmanager.ui.auth

import androidx.lifecycle.ViewModel
import ru.kodeks.docmanager.network.api.AuthApi
import javax.inject.Inject

class AuthActivityViewModel @Inject constructor(private val api: AuthApi) : ViewModel() {

}