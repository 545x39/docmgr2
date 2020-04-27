package ru.kodeks.docmanager

import android.content.Context
import androidx.multidex.MultiDex
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.kodeks.docmanager.di.DaggerApplicationComponent
import ru.kodeks.docmanager.di.factory.WorkerProviderFactory
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocManagerApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }

    @Inject
    lateinit var workerProviderFactory: WorkerProviderFactory

    override fun onCreate() {
        super.onCreate()
        initWorkManager()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element)
                        ?.plus(": ${element.methodName}:${element.lineNumber}")
                }
            })
        }
    }

    private fun initWorkManager() {
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(workerProviderFactory)
                .build()
        )
    }

    /** This is the copy of a MultiDexApplication.class content that actually makes
     * application support multidex. Doing so is necessary because DaggerApplication
     * extends Application, no multidex support.*/
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}