package ru.kodeks.docmanager.network

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import ru.kodeks.docmanager.network.service.PeriodicSyncService
import ru.kodeks.docmanager.util.DocManagerApp

const val JOB_INFO_ID = 1234

class SyncJobScheduler private constructor() {
    lateinit var scheduler: JobScheduler

    init {
        reset()
    }

    private fun reset() {
        val newInterval = 60 * 1000L * 2//PropertiesManager.getPropInt(SettingsActivity.PREFERENCE_UPDATE_PERIOD_PREF)
        val jobInfo: JobInfo = JobInfo.Builder(JOB_INFO_ID, ComponentName(DocManagerApp.instance, PeriodicSyncService::class.java))
                .setPeriodic(newInterval)
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        scheduler = DocManagerApp.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val pendingJobs = scheduler.allPendingJobs
        when (pendingJobs.isNotEmpty()) {
            true -> {
                if (pendingJobs[0].intervalMillis != newInterval) {
                    scheduler.cancelAll()
                    scheduler.schedule(jobInfo)
                }
            }
            false -> scheduler.schedule(jobInfo)
        }
    }

    companion object {
        var INSTANCE = SyncJobScheduler()
        private set

        fun reset() {
            INSTANCE.reset()
        }
    }
}