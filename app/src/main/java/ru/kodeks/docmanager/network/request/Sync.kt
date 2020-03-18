package ru.kodeks.docmanager.network.request

import ru.kodeks.docmanager.constants.DataFilter
import ru.kodeks.docmanager.constants.Settings.Timeouts.PREFERENCE_GLOBAL_CATALOG_LAST_UPDATE_TIME
import ru.kodeks.docmanager.constants.Settings.Timeouts.PREFERENCE_GLOBAL_CATALOG_UPDATE_PERIOD
import ru.kodeks.docmanager.model.data.Widget
import ru.kodeks.docmanager.model.io.SyncRequest
import ru.kodeks.docmanager.network.request.builder.DataFilterRequestBuilder

open class SyncRequestBuilder : DataFilterRequestBuilder<SyncRequest>() {

    override fun initRequest() =
        SyncRequest(
            dataFilter = dataFilter()
            //    , version = version(), widgets = widgets()
        )

//    private fun widgets(): List<Widget> {
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
//
//    private fun version(): Version {
//        return database.sequences.let {
//            Version(main = it[0], global = it[1], settings = it[2])
//        }
//    }

    override fun dataFilter(): Int {
        fun timeToUpdateGlobalCatalog(): Boolean {
            return System.currentTimeMillis() - preferences.getLong(
                PREFERENCE_GLOBAL_CATALOG_LAST_UPDATE_TIME,
                0
            ) >
                    Integer.parseInt(
                        preferences.getString(
                            PREFERENCE_GLOBAL_CATALOG_UPDATE_PERIOD,
                            "12"
                        )!!
                    ) * 60 * 60 * 1000
        }

        return when (timeToUpdateGlobalCatalog()) {
            true -> super.dataFilter() or DataFilter.GLOBAL_OBJECTS
            false -> super.dataFilter()
        }
    }
}

class InitRequestBuilder : SyncRequestBuilder() {

    override fun initRequest() =
        SyncRequest(
            dataFilter = dataFilter(),
//            version = Version(main = "0", global = "0", settings = "0"),
            widgets = widgets()
        )

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
}