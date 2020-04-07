package ru.kodeks.docmanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.work.*
import ru.kodeks.docmanager.work.SYNC_REQUEST_CONSTRAINTS
import ru.kodeks.docmanager.work.SYNC_RESPONSE_PARSER_CONSTRAINTS
import ru.kodeks.docmanager.work.checkstate.CheckStateWorker
import ru.kodeks.docmanager.work.sync.ParseResponseWorker
import ru.kodeks.docmanager.work.sync.SYNC_REQUEST_TAG
import ru.kodeks.docmanager.work.sync.SYNC_RESPONE_TAG
import ru.kodeks.docmanager.work.sync.SyncRequestWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

const val NAME_SYNC = "SYNC_WORK"

@Singleton
class SyncStateRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var workManager: WorkManager


    fun getSyncWorkInfo(): LiveData<MutableList<WorkInfo>> {
        return syncWorkInfo
    }
    private val syncWorkInfo: MediatorLiveData<MutableList<WorkInfo>> = MediatorLiveData()

    fun sync(login: String, password: String) {
        val checkStateWorker = OneTimeWorkRequestBuilder<CheckStateWorker>()
            .setConstraints(SYNC_REQUEST_CONSTRAINTS)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            ).build()
        val syncRequestWorker = OneTimeWorkRequestBuilder<SyncRequestWorker>()
            .setInputData(workDataOf("login" to login, "password" to password))
            .setConstraints(SYNC_REQUEST_CONSTRAINTS).addTag(SYNC_REQUEST_TAG)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            ).build()
        val syncResponseParseWorker = OneTimeWorkRequestBuilder<ParseResponseWorker>()
            .setConstraints(SYNC_RESPONSE_PARSER_CONSTRAINTS).addTag(SYNC_RESPONE_TAG)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            ).build()
        workManager.beginUniqueWork(NAME_SYNC, ExistingWorkPolicy.KEEP, checkStateWorker)
            .then(syncRequestWorker)
//            .then(syncResponseParseWorker)
            .apply {
                syncWorkInfo.addSource(workInfosLiveData, Observer { syncWorkInfo.postValue(it) })
            }.enqueue()
    }

}