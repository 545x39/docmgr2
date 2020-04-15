package ru.kodeks.docmanager.work.documentaction.approval

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/** Подписание. */
class SendSignedDocumentsWorker constructor(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override  suspend fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}