package ru.kodeks.docmanager.util

import android.content.Context
import android.content.SharedPreferences
import androidx.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.kodeks.docmanager.BuildConfig
import ru.kodeks.docmanager.constants.Paths.DB_DIRECTORY
import ru.kodeks.docmanager.constants.Paths.RESPONSE_DIRECTORY
import ru.kodeks.docmanager.di.DaggerApplicationComponent
import ru.kodeks.docmanager.di.StubUser
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocManagerApp : DaggerApplication() {
    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var user: StubUser

    @Inject
    lateinit var preferences: SharedPreferences
    val responseDirectory: String
        get() {
            return "${getExternalFilesDir(RESPONSE_DIRECTORY)}"
        }
    val dbDirectory: String
        get() {
            return "${getExternalFilesDir(DB_DIRECTORY)}"
        }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(object : DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element)
                        ?.plus(": ${element.methodName}:${element.lineNumber}")
                }
            })
        }
        //        SyncJobScheduler.INSTANCE //Запустит периодические обновления.
    }

    /** This is the copy of a MultiDexApplication.class content that actually makes
     * application support multidex. Doing so is necessary because DaggerApplication
     * extends Application, no multidex support.*/
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        lateinit var instance: DocManagerApp
            private set
    }
}