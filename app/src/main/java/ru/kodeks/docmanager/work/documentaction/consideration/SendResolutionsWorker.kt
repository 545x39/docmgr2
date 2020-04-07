package ru.kodeks.docmanager.work.documentaction.consideration

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/** Резолюции. */
class SendResolutionsWorker constructor(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters){
    override fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}