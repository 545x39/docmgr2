package ru.kodeks.docmanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import ru.kodeks.docmanager.repository.SyncStateRepository
import ru.kodeks.docmanager.repository.UserRepository
import ru.kodeks.docmanager.repository.resource.SyncStateResource
import ru.kodeks.docmanager.repository.resource.UserStateResource
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {

    @Inject
    protected lateinit var userRepository: UserRepository

    @Inject
    protected lateinit var syncRepository: SyncStateRepository

    fun getUser(): LiveData<UserStateResource> {
        return userRepository.getUser()
    }

    fun getSyncState(): LiveData<SyncStateResource<WorkInfo>> {
        return syncRepository.getSyncState()
    }
}