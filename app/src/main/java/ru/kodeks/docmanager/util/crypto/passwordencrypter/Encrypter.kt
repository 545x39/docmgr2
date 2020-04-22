package ru.kodeks.docmanager.util.crypto.passwordencrypter

import ru.kodeks.docmanager.const.ENCRYPTED_PASSWORD_PREFIX
import ru.kodeks.docmanager.exceptions.PasswordEncryptionException

private val aes: AES = AES()

class Encrypter {

    @Throws(PasswordEncryptionException::class)
    fun encrypt(password: String, addSalt: Boolean = true): String {
        fun getEncrypted(unencryptedPassword: String): String {
            try {
                return aes.toBase64(aes.encrypt(unencryptedPassword))
            } catch (e: Exception) {
                throw PasswordEncryptionException(
                    cause = e
                )
            }
        }

        return when(addSalt){
            true -> ENCRYPTED_PASSWORD_PREFIX + getEncrypted(password)
            false -> getEncrypted(password)
        }
    }

    @Throws(PasswordEncryptionException::class)
    fun decrypt(encryptedPassword: String): String {
        try {
            return aes.decrypt(aes.toBytes(encryptedPassword))
        } catch (e: Exception) {
            throw PasswordEncryptionException(
                cause = e
            )
        }
    }
}