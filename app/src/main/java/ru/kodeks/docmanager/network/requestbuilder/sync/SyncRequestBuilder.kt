package ru.kodeks.docmanager.network.requestbuilder.sync

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import ru.kodeks.docmanager.const.DataFilter
import ru.kodeks.docmanager.const.Settings.Timeouts.PREFERENCE_GLOBAL_CATALOG_LAST_UPDATE_TIME
import ru.kodeks.docmanager.const.Settings.Timeouts.PREFERENCE_GLOBAL_CATALOG_UPDATE_PERIOD
import ru.kodeks.docmanager.model.data.Version
import ru.kodeks.docmanager.model.data.Widget
import ru.kodeks.docmanager.model.io.SyncRequest
import ru.kodeks.docmanager.network.requestbuilder.DEFAULT_DATA_FILTER
import ru.kodeks.docmanager.network.requestbuilder.RequestBuilder
import ru.kodeks.docmanager.persistence.Database

abstract class SyncRequestBuilder constructor(
    val database: Database,
    val preferences: SharedPreferences
) : RequestBuilder<SyncRequest>() {
    override fun request(): SyncRequest {
        return SyncRequest().apply {
            dataFilter = dataFilter()
            version = version()
            widgets = widgets()
        }
    }

    private fun version(): Version {
        var version = Version(0, 0, 0)
        CoroutineScope(IO).launch {
            database.userDAO().getUser()?.let {
                version = Version(it.sequence, it.globalSequence, it.settingsSequence)
            }
        }
        return version
    }

    private fun dataFilter(): Int {
        fun timeToUpdateGlobalCatalog(): Boolean {
            return System.currentTimeMillis() - preferences.getLong(
                PREFERENCE_GLOBAL_CATALOG_LAST_UPDATE_TIME, 0
            ) >
                    Integer.parseInt(
                        preferences.getString(
                            PREFERENCE_GLOBAL_CATALOG_UPDATE_PERIOD,
                            "12"
                        )!!
                    ) * 60 * 60 * 1000
        }
        return when (timeToUpdateGlobalCatalog()) {
            true -> DEFAULT_DATA_FILTER or DataFilter.GLOBAL_OBJECTS
            false -> DEFAULT_DATA_FILTER
        }
    }

    //TODO STUB
    private fun widgets(): List<Widget> {
        val widgetList: ArrayList<Widget> = ArrayList()
        arrayListOf(
            16,
            137,
            139,
            17,
            150,
            160,
            163,
            196,
            1971,
            208,
            209,
            210,
            164,
            32,
            33,
            188,
            191,
            187
        ).let {
            for (i in 0 until it.size - 1) {
                val sequence = 0L
                widgetList.add(Widget(sequence = sequence).apply { type = it[i] })
            }
            return widgetList
        }
    }


//TODO example from an old code

//        private fun widgets(): List<Widget> {
//        val widgetList: ArrayList<Widget> = ArrayList()
//        database.activeWidgetTypes?.let {
//            if (!it.moveToFirst()) {
//                it.close()
//                return emptyList()
//            }
//            for (i in 0 until it.count) {
//                var sequence = it.getLong(it.getColumnIndex(SEQUENCE))
//                if (preferences.getInt(SEQUENCE_OFFSET_PREFERENCE_KEY, 0) != 0) {
//                    sequence -= preferences.getInt(SEQUENCE_OFFSET_PREFERENCE_KEY, 0)
//                }
//                widgetList.add(Widget(sequence = sequence).apply {
//                    type = it.getInt(it.getColumnIndex(TYPE_ID))
//                })
//                it.moveToNext()
//            }
//            it.close()
//            return widgetList
//        } ?: return emptyList()
//    }
}