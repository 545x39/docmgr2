package ru.kodeks.docmanager

import android.content.SharedPreferences
import ru.kodeks.docmanager.constants.Settings
import ru.kodeks.docmanager.crypto.passwordencrypter.Encrypter
import javax.inject.Inject

//@Singleton
class User
//@Inject
constructor() {
    @Inject
    lateinit var preferences: SharedPreferences
    var login: String = ""
    var password: String = ""
    var uid: String? = ""
    var name: String = ""
    var post: String = ""
    var rights: List<Int> = listOf()
    val encryptedPassword: String
        get() = password.let { password ->
            kotlin.runCatching {
                when (//DocManagerApp.instance.
                    preferences.getBoolean(
                        Settings.ENCRYPT_PASSWORD,
                        false
                    )) {
                    true -> Encrypter().encrypt(password)
                    false -> password
                }
            }.getOrDefault(password)
        }

    constructor(login: String, password: String) : this() {
        this.login = login
        this.password = password
    }
}