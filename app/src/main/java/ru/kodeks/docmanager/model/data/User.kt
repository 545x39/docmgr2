package ru.kodeks.docmanager.model.data

import android.content.SharedPreferences
import android.os.Build
import androidx.room.*
import com.google.gson.annotations.Expose
import ru.kodeks.docmanager.BuildConfig
import ru.kodeks.docmanager.const.Settings
import ru.kodeks.docmanager.persistence.typeconverter.IntListToStringTypeConverter
import ru.kodeks.docmanager.util.crypto.passwordencrypter.Encrypter
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
    @Expose
    @Ignore
    val device = "Android"
    @Expose
    @Ignore
    val deviceModel = "${Build.BRAND} ${Build.DEVICE}"
    @Expose
    @Ignore
    val androidVersion: String = Build.VERSION.RELEASE
    @Expose
    @Ignore
    val version = BuildConfig.VERSION_NAME
//    @Expose
//    @Ignore
//    val deviceUid = ""//DeviceUuidFactory(app).deviceUuid.toString()
}