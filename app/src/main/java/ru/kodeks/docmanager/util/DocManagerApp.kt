package ru.kodeks.docmanager.util

import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import ru.kodeks.docmanager.constants.Paths.DB_DIRECTORY
import ru.kodeks.docmanager.constants.Paths.RESPONSE_DIRECTORY
import ru.kodeks.docmanager.util.di.CurrentUser
import ru.kodeks.docmanager.util.di.DaggerCurrentUserComponent
import ru.kodeks.docmanager.util.di.DaggerPreferencesComponent


class DocManagerApp : MultiDexApplication() {

    var executors = AppExecutors()
    lateinit var user: CurrentUser
    lateinit var preferences: SharedPreferences
    val responseDirectory: String
        get() {
            return "${getExternalFilesDir(RESPONSE_DIRECTORY)}"
        }
    val dbDirectory: String
        get() {
            return "${getExternalFilesDir(DB_DIRECTORY)}"
        }

    override fun onCreate() {
        super.onCreate()
        instance = this
        user = DaggerCurrentUserComponent.create().currentUser()
        preferences = DaggerPreferencesComponent.create().preferences()
//        SyncJobScheduler.INSTANCE //Запустит периодические обновления.

    }

    companion object {
        lateinit var instance: DocManagerApp
            private set
    }
}