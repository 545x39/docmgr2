package ru.kodeks.docmanager.work.documentaction.approval

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/** Согласование.*/
class SendApprovalActionsWorker constructor(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters){
    override fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}