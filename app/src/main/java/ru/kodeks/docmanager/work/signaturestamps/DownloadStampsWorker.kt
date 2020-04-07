package ru.kodeks.docmanager.work.signaturestamps

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/** Загрузка штампов */
class DownloadStampsWorker constructor(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters){
    override fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}