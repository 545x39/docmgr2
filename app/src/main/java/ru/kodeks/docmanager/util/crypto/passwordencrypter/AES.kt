package ru.kodeks.docmanager.util.crypto.passwordencrypter

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val CHARSET = "utf-8"
private const val DELIMITER = ";"
/** private static final String ALGORITHM = "AES";
 * "AES/CBC/NoPadding";"AES/CFB/NoPadding"; "AES/CBC/PKCS5Padding"; (DOESN'T WORK)
 */
@Suppress("SpellCheckingInspection")
private const val ALGORITHM = "AES/CBC/PKCS7Padding"

private val KEY = byteArrayOf(0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x10, 0x11, 0x1, 0x16, 0x14, 0x15, 0x16)
private val IV = byteArrayOf(0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16)

class AES {

    @Throws(Exception::class)
    fun getEncryptedString(stringToEncrypt: String): String {
        return toBase64(encrypt(stringToEncrypt))
    }

    @Throws(Exception::class)
    fun encrypt(stringToEncrypt: String): ByteArray {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(), IvParameterSpec(IV))
        return cipher.doFinal(stringToEncrypt.toByteArray(charset(CHARSET)))
    }

    fun toBase64(encryptedByteValue: ByteArray): String {
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT)
    }

    @Throws(UnsupportedEncodingException::class)
    fun toString(decryptedByteValue: ByteArray): String {
        return String(decryptedByteValue, charset(CHARSET))
    }

    fun toByteArrayString(bytes: ByteArray): String {
        val builder = StringBuilder()
        for (i in bytes.indices) {
            builder.append(bytes[i].toInt()).append(DELIMITER)
        }
        return builder.toString()
    }

    fun byteArrayStringToBytes(byteArrayString: String): ByteArray {
        val splitString = byteArrayString.split(DELIMITER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val bytes = ByteArray(splitString.size)
        for (i in bytes.indices) {
            bytes[i] = java.lang.Byte.parseByte(splitString[i])
        }
        return bytes
    }

    fun toBytes(base64String: String): ByteArray {
        return Base64.decode(base64String, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun getDecryptedString(value: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(), IvParameterSpec(IV))
        val decryptedValue64 = Base64.decode(value, Base64.DEFAULT)
        val decryptedByteValue = cipher.doFinal(decryptedValue64)
        return toString(decryptedByteValue)
    }

    @Throws(Exception::class)
    fun decrypt(value: String): ByteArray {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(), IvParameterSpec(IV))
        return cipher.doFinal(toBytes(value))
        //        return toString(decryptedByteValue);
    }

    @Throws(Exception::class)
    fun decrypt(bytes: ByteArray): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(), IvParameterSpec(IV))
        return String(cipher.doFinal(bytes), charset(CHARSET))
    }

    private fun getKeySpec(): Key {
        return SecretKeySpec(KEY, ALGORITHM)
    }

}

