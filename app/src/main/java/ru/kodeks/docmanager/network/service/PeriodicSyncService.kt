package ru.kodeks.docmanager.network.service

import android.app.job.JobParameters
import android.app.job.JobService
import ru.kodeks.docmanager.network.operations.SyncOperation
import ru.kodeks.docmanager.network.request.builder.SyncRequestBuilder
import ru.kodeks.docmanager.util.DocManagerApp


class PeriodicSyncService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        DocManagerApp.instance.executors.networkIO().execute {
            var reschedule = false
            try {
                SyncOperation(
                    SyncRequestBuilder().build()
                ).execute()
            } catch (e: Exception) {
                reschedule = true
            }
            jobFinished(params, reschedule)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }
}
