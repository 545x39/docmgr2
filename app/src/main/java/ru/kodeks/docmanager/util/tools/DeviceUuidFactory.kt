package ru.kodeks.docmanager.util.tools

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import java.io.UnsupportedEncodingException
import java.util.*

class DeviceUuidFactory @SuppressLint("HardwareIds") constructor(val context: Context) {


    private val prefsFile = "device_id.xml"
    private val prefsDeviceId = "device_id"
    @Suppress("DEPRECATION")
    val deviceUuid: UUID?
        @SuppressLint("HardwareIds")
        get() {
            synchronized(DeviceUuidFactory::class.java) {
                var uuid: UUID
                val prefs = context.getSharedPreferences(prefsFile, 0)
                val id = prefs.getString(prefsDeviceId, null)
                if (id != null) { // Use the ids previously computed and stored in the prefs file
                    uuid = UUID.fromString(id)
                } else {
                    val androidId = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
                    // Use the Android ID unless it's broken, in which case
                    // fallback on deviceId,
                    // unless it's not available, then fallback on a random
                    // number which we store to a prefs file
                    try {
                        if ("9774d56d682e549c" != androidId) {
                            uuid = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8")))
                        } else {
                            var deviceId: String? = null
                            try {
                                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                    deviceId =
                                        (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                                }
                            } catch (e: Exception) {
                                //
                            }
                            uuid =
                                if (deviceId != null) UUID.nameUUIDFromBytes(
                                    deviceId.toByteArray(charset("utf8"))
                                ) else UUID.randomUUID()
                        }
                    } catch (e: UnsupportedEncodingException) {
//                        throw RuntimeException(e)
                        uuid = UUID.randomUUID()
                    }
                    // Write the value out to the prefs file
                    prefs.edit().putString(
                        prefsDeviceId,
                        uuid.toString()
                    ).apply()
                }
                return uuid
            }
        }
}