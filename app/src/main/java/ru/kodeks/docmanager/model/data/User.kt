package ru.kodeks.docmanager.model.data

import android.content.SharedPreferences
import android.os.Build
import androidx.room.*
import ru.kodeks.docmanager.BuildConfig
import ru.kodeks.docmanager.constants.Settings
import ru.kodeks.docmanager.crypto.passwordencrypter.Encrypter
import ru.kodeks.docmanager.persistence.typeconverters.IntListToStringTypeConverter
import javax.inject.Inject

@Entity(tableName = "user")
@TypeConverters(IntListToStringTypeConverter::class)
class User(
    @ColumnInfo(name = "login")
    var login: String = "",
    @ColumnInfo(name = "password")
    var password: String = "",
    @PrimaryKey
    @ColumnInfo(name = "user_uid")
    var uid: String = "",
    @ColumnInfo(name = "sequence")
    var sequence: Int = 0,
    @ColumnInfo(name = "global_sequence")
    var globalSequence: Int = 0,
    @ColumnInfo(name = "settings_sequence")
    var settingsSequence: Int = 0,
    @ColumnInfo(name = "user_rights")
    var rights: List<Int> = listOf(),
    @ColumnInfo(name = "server_version")
    var serverVersion: String = ""
) {
    @Inject
    @Ignore
    lateinit var preferences: SharedPreferences
    val encryptedPassword: String
        get() = password.let { password ->
            runCatching {
                when (preferences.getBoolean(
                    Settings.ENCRYPT_PASSWORD,
                    false
                )) {
                    true -> Encrypter().encrypt(password)
                    false -> password
                }
            }.getOrDefault(password)
        }

    @Ignore
    val device = "Android"

    @Ignore
    val deviceModel = "${Build.BRAND} ${Build.DEVICE}"

    @Ignore
    val androidVersion: String = Build.VERSION.RELEASE

    @Ignore
    val version = BuildConfig.VERSION_NAME

//    @Ignore
//    val deviceUid = ""//DeviceUuidFactory(app).deviceUuid.toString()
}