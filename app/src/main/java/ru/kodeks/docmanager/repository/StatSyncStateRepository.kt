package ru.kodeks.docmanager.repository

import retrofit2.Retrofit
import ru.kodeks.docmanager.persistence.Database
import javax.inject.Inject

class StatSyncStateRepository @Inject constructor(var database: Database, var retrofit: Retrofit) {

}