package ru.kodeks.docmanager.util

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global executor pools for the whole application.
 *
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
@Singleton
class AppExecutors private constructor(
    private val diskIO: Executor,
    private val networkIO: Executor,
    private val urgentDownloadIO: Executor,
    private val mainThreadIO: Executor
) {

    @Inject
    constructor() : this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(3),
        Executors.newSingleThreadExecutor(),
        MainThreadExecutor()
    )

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun urgentDownloadIO(): Executor {
        return urgentDownloadIO
    }

    fun mainThread(): Executor {
        return mainThreadIO
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
