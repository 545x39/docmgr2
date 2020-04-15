package ru.kodeks.docmanager.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import ru.kodeks.docmanager.persistence.Database
import ru.kodeks.docmanager.repository.resource.SyncStateResource
import ru.kodeks.docmanager.work.checkStateWorker
import ru.kodeks.docmanager.work.getQualifiedSignatureStampWorker
import ru.kodeks.docmanager.work.getSimpleSignatureStampWorker
import ru.kodeks.docmanager.work.sync.SyncWorker
import ru.kodeks.docmanager.work.syncWorker
import javax.inject.Inject
import javax.inject.Singleton

const val NAME_SYNC = "SYNC_WORK"
const val SYNC_PROGRESS = "sync_progress"

@Singleton
class SyncStateRepository @Inject constructor(database: Database, preferences: SharedPreferences) :
    BaseRepository(database, preferences) {

    /** Это вот у него есть Running, Success, SucessWihErrors, Failure и  NotEnqueued.*/
    private var syncState: MediatorLiveData<SyncStateResource<SyncWorker>> = MediatorLiveData()

    fun getSyncState(): LiveData<SyncStateResource<SyncWorker>> {
        return syncState
    }

    @Inject
    lateinit var workManager: WorkManager

    private val syncWorkInfo: MediatorLiveData<String> = MediatorLiveData()


    private val progressLiveData = MediatorLiveData<WorkInfo>()

    fun getProgress(): LiveData<WorkInfo> {
        return progressLiveData
    }

    fun sync(login: String, password: String) {
        val checkStateWorker = checkStateWorker()
        val getSimpleSignatureStampWorker = getSimpleSignatureStampWorker(login, password)
        val getQualifiedSignatureStampWorker = getQualifiedSignatureStampWorker(login, password)
        val syncWorker = syncWorker(login, password)
        workManager.beginWith(checkStateWorker)
            .then(listOf(getSimpleSignatureStampWorker, getQualifiedSignatureStampWorker))
            .then(syncWorker).enqueue()
//        workManager.cancelAllWork()
        workManager.beginUniqueWork(NAME_SYNC, ExistingWorkPolicy.KEEP, syncWorker)
            .apply {
                val workInfoById = workManager.getWorkInfoByIdLiveData(syncWorker.id)
                progressLiveData.addSource(workInfoById, Observer { workInfo ->
                    workInfo.progress.getString(SYNC_PROGRESS)
                        ?.let { progressLiveData.postValue(workInfo) }
                })
            }.enqueue()
    }
}