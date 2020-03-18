package ru.kodeks.docmanager

import android.content.Context
import androidx.multidex.MultiDex
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.kodeks.docmanager.di.components.DaggerApplicationComponent
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Singleton

@Singleton
class DocManagerApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
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
}