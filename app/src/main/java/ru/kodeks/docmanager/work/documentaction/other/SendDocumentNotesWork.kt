package ru.kodeks.docmanager.work.documentaction.other

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/** Отправка пометок. */
class SendDocumentNotesWork constructor(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}