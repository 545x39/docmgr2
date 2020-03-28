package ru.kodeks.docmanager.ui.fragments.auth

import androidx.lifecycle.ViewModel
import ru.kodeks.docmanager.network.api.FlowableApi
import javax.inject.Inject


class AuthViewModel @Inject constructor(var api: FlowableApi) : ViewModel() {

//    fun test(){
//        val request: SyncRequest = InitRequestBuilder().build()
//        api.post(request)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()
//    }
}