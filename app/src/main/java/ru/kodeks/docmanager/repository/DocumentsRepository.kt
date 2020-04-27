package ru.kodeks.docmanager.repository

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import ru.kodeks.docmanager.db.Database
import ru.kodeks.docmanager.model.data.Document
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class DocumentsRepository @Inject constructor(
    database: Database,
    preferences: SharedPreferences
) : AbstractRepository(database, preferences) {

    private var documentsLiveData: MutableLiveData<List<Document>> = MutableLiveData()

    suspend fun getDocuments(): List<Document> {
        val list = database.documentDao().getDocuments()
        Timber.e("DOCUMENTS: ${documentsLiveData.value?.size}, time: time")
        return  list
    }

    init {
        CoroutineScope(IO).launch {
            val time = measureTimeMillis {
                documentsLiveData.postValue(getDocuments())
            }
        }
    }
}