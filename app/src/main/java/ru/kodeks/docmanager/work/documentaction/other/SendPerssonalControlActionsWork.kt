package ru.kodeks.docmanager.work.documentaction.other

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/** Изменение личного контроля. */
class SendPerssonalControlActionsWork constructor(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters){
    override fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}
