package ru.kodeks.docmanager.repository

import androidx.lifecycle.MutableLiveData
import retrofit2.Retrofit
import ru.kodeks.docmanager.model.data.User
import ru.kodeks.docmanager.network.resource.UserStateResource
import ru.kodeks.docmanager.persistence.Database
import javax.inject.Inject

open class BaseRepository{

    @Inject
    lateinit var database: Database

    @Inject
    lateinit var retrofit: Retrofit

    var user: User? = null

    init {
//        if(database.initDao().)
    }


    protected lateinit var userLiveData: MutableLiveData<UserStateResource>


}