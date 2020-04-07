package ru.kodeks.docmanager.work.sync

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

const val SYNC_RESPONE_TAG = "SYNC_RESPONSE_TAG"

class ParseResponseWorker constructor(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        //TODO implement
        return Result.success()
    }
}