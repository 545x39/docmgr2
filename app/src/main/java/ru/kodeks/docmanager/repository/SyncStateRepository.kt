package ru.kodeks.docmanager.repository

import androidx.lifecycle.MutableLiveData
import retrofit2.Retrofit
import ru.kodeks.docmanager.network.resource.SyncStateResource
import ru.kodeks.docmanager.persistence.Database
import javax.inject.Inject

class SyncStateRepository @Inject constructor(var database: Database
//                                                  , var retrofit: Retrofit
) {

    private var syncStater: MutableLiveData<SyncStateResource> = MutableLiveData()

    @Inject
    lateinit var retrofit: Retrofit
}