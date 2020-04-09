package ru.kodeks.docmanager.work.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.kodeks.docmanager.di.providerfactory.ChildWorkerFactory
import javax.inject.Inject

const val SYNC_RESPONE_TAG = "SYNC_RESPONSE_TAG"

class ParseResponseWorker @Inject constructor(
    var preferences: SharedPreferences,
    context: Context,
    workerParameters: WorkerParameters
) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        //TODO implement

        return Result.success()
    }

    //<editor-fold desc="FACTORY" defaultstate="collapsed">
    class Factory @Inject constructor(
        var preferences: SharedPreferences
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): Worker {
            return ParseResponseWorker(preferences, appContext, params)
        }
    }
    //</editor-fold>
}