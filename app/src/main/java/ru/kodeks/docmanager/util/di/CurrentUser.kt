package ru.kodeks.docmanager.util.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.kodeks.docmanager.constants.Settings.ENCRYPT_PASSWORD
import ru.kodeks.docmanager.crypto.passwordencrypter.Encrypter
import ru.kodeks.docmanager.util.DocManagerApp
import ru.kodeks.docmanager.util.PasswordEncryptionException
import javax.inject.Inject
import javax.inject.Singleton

@Component(modules = [CurrentUserModule::class])
interface CurrentUserComponent {

    fun currentUser(): CurrentUser
}

@Module
class CurrentUserModule{

    @Provides
    fun provideUser(): CurrentUser{
        return Stub()
    }
}

class Stub @Inject constructor() : CurrentUser() {
    override var uid: String? = ""
    override var login: String? = "meyksin"
    override var password: String? = "11111"
    init {
        post = ""
    }
}

@Singleton
abstract class CurrentUser {
    abstract var uid: String?
    abstract var login: String?
    lateinit var name: String
    lateinit var post: String
    abstract var password: String?
    val encryptedPassword: String?
        get() = password?.let {
            try {
                when (DocManagerApp.instance.preferences.getBoolean(ENCRYPT_PASSWORD, false)) {
                    true -> Encrypter().encrypt(it)
                    false -> it
                }
            } catch (e: PasswordEncryptionException) {
                it
            }
        }
}