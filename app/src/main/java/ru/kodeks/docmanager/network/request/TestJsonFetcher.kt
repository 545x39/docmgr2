package ru.kodeks.docmanager.network.request

import android.util.Log
import ru.kodeks.docmanager.constants.LogTag.TAG
import ru.kodeks.docmanager.network.Network
import ru.kodeks.docmanager.util.DocManagerApp
import ru.kodeks.docmanager.util.tools.stackTraceToString


    fun getTestJson() {
        try {
            DocManagerApp.instance.executors.networkIO().execute {
                val url = "https://my-json-server.typicode.com/545x39/jsontest/docs/"
//                url = "http://jsonplaceholder.typicode.com/users"
                val response = Network.INSTANCE.api.get(url).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        /////
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Got exception:\n ${stackTraceToString(e)}")
        }
    }
