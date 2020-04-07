package ru.kodeks.docmanager.work.documentaction.consideration

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/** Проекты резолюций. */
class SendResolutionProjectsWorker constructor(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}