package ru.kodeks.docmanager.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.work.*
import ru.kodeks.docmanager.const.NAME_SYNC
import ru.kodeks.docmanager.const.PathsAndFileNames
import ru.kodeks.docmanager.persistence.Database
import ru.kodeks.docmanager.repository.resource.SyncStateResource
import ru.kodeks.docmanager.work.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncStateRepository @Inject constructor(
    database: Database,
    preferences: SharedPreferences,
    private val workManager: WorkManager
) : AbstractRepository(database, preferences) {

    private var syncState: MediatorLiveData<SyncStateResource<WorkInfo>> = MediatorLiveData()

    fun getSyncState(): LiveData<SyncStateResource<WorkInfo>> {
        return syncState
    }

    fun sync(login: String, password: String) {
        val credentials = credentials(login, password)
        val getSimpleSignatureStampWorker = getSimpleSignatureStampWorker(credentials)
        val getQualifiedSignatureStampWorker = getQualifiedSignatureStampWorker(credentials)
        val syncWorker = syncWorker(credentials)
        val parserWorker = parserWorker(credentials)

        //<editor-fold desc="subscribeObservers()" defaultstate="collapsed">
        fun susbscribeObservers() {
            syncState.addSource(
                workManager.getWorkInfosByTagLiveData(DOWNLOAD_STAMP_TAG), Observer { infos ->
                    infos.forEach {
                        when (it.state) {
                            WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING ->
                                syncState.postValue(SyncStateResource.InProgress(it))
                            else -> {
                            }
                        }
                    }
                })
            syncState.addSource(
                workManager.getWorkInfoByIdLiveData(syncWorker.id), Observer {
                    when (it.state) {
                        WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                            syncState.postValue(SyncStateResource.InProgress(it))
                        }
                        WorkInfo.State.FAILED -> syncState.postValue(SyncStateResource.Failure())
                        else -> {
                        }
                    }
                })
            syncState.addSource(
                workManager.getWorkInfoByIdLiveData(parserWorker.id), Observer {
                    when (it.state) {
                        WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                            syncState.postValue(SyncStateResource.InProgress(it))
                        }
                        WorkInfo.State.SUCCEEDED -> syncState.postValue(SyncStateResource.Success())
                        WorkInfo.State.FAILED -> syncState.postValue(SyncStateResource.Failure())
                        else -> {
                        }
                    }
                })
        }
        //</editor-fold>
//        workManager.cancelAllWork()
        workManager.beginUniqueWork(NAME_SYNC, ExistingWorkPolicy.KEEP, checkStateWorker())
            .then(listOf(getSimpleSignatureStampWorker, getQualifiedSignatureStampWorker))
            .then(syncWorker)
            .then(parserWorker)
            .apply {
                susbscribeObservers()
            }.enqueue()
    }

    //        val testWorker = testWorker(credentials)
    //            .then(testWorker)
    private fun credentials(login: String, password: String): Data {
        return workDataOf(PathsAndFileNames.LOGIN to login, PathsAndFileNames.PASSWORD to password)
    }
}


