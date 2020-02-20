package ru.kodeks.docmanager.crypto.passwordencrypter

import ru.kodeks.docmanager.util.PasswordEncryptionException

private const val SALT = "#4aDf8EJ7"
private val aes: AES = AES()

class Encrypter {

    @Throws(PasswordEncryptionException::class)
    fun encrypt(password: String, addSalt: Boolean = true): String {
        fun getEncrypted(unencryptedPassword: String): String {
            try {
                return aes.toBase64(aes.encrypt(unencryptedPassword))
            } catch (e: Exception) {
                throw PasswordEncryptionException(cause = e)
            }
        }

        return when(addSalt){
            true -> SALT + getEncrypted(password)
            false -> getEncrypted(password)
        }
    }

    @Throws(PasswordEncryptionException::class)
    fun decrypt(encryptedPassword: String): String {
        try {
            return aes.decrypt(aes.toBytes(encryptedPassword))
        } catch (e: Exception) {
            throw PasswordEncryptionException(cause = e)
        }
    }
}