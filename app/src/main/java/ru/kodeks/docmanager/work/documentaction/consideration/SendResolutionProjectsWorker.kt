package ru.kodeks.docmanager.work.documentaction.consideration

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/** Проекты резолюций. */
class SendResolutionProjectsWorker constructor(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}