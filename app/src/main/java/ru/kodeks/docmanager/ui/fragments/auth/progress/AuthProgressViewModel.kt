package ru.kodeks.docmanager.ui.fragments.auth.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import ru.kodeks.docmanager.ui.main.BaseViewModel
import javax.inject.Inject

class AuthProgressViewModel @Inject constructor() : BaseViewModel() {

    fun getProgress(): LiveData<Int> {
        return progress
    }

    private var progress: MutableLiveData<Int> = MutableLiveData()

    private var job: Job = CoroutineScope(Default).launch {
        var value = 0
        while (true) {
            if (value == 100) {
                value = 0
            }
            value += 1
            withContext(Main) {
                progress.value = value
            }
            delay(25)
        }
    }

    override fun onCleared() {
        job.cancel()
    }
}