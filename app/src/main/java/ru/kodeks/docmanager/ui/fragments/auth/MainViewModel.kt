package ru.kodeks.docmanager.ui.fragments.auth

import androidx.lifecycle.ViewModel
import ru.kodeks.docmanager.repository.UserRepository
import javax.inject.Inject


class MainViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var userRepository: UserRepository
//    var cachedUser: LiveData<UserStateResource>

//    fun test(){
//        val request: SyncRequest = InitRequestBuilder().build()
//        api.post(request)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()
//    }
}