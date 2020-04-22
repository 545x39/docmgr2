package ru.kodeks.docmanager.work.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay
import ru.kodeks.docmanager.const.SYNC_PROGRESS
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import javax.inject.Inject

class TestWorker @Inject constructor(
    context: Context, workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        setProgressAsync(workDataOf(SYNC_PROGRESS to "1"))
        delay(1000)
        setProgressAsync(workDataOf(SYNC_PROGRESS to "2"))
        delay(1000)
        setProgressAsync(workDataOf(SYNC_PROGRESS to "3"))
        delay(1000)
        setProgressAsync(workDataOf(SYNC_PROGRESS to "4"))
        delay(1000)
        setProgressAsync(workDataOf(SYNC_PROGRESS to "5"))
        delay(1000)
        return Result.success()
    }

    class Factory @Inject constructor() : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker {
            return TestWorker(appContext, params)
        }
    }
}
