package ru.kodeks.docmanager.network.request.builder


import android.content.SharedPreferences
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.constants.DataFilter.CLASSIFIERS
import ru.kodeks.docmanager.constants.DataFilter.DOCUMENTS
import ru.kodeks.docmanager.constants.DataFilter.GLOBAL_OBJECTS
import ru.kodeks.docmanager.constants.DataFilter.SETTINGS
import ru.kodeks.docmanager.constants.DataFilter.WORKBENCH
import ru.kodeks.docmanager.constants.DataFilter.WORKBENCH_META
import ru.kodeks.docmanager.constants.Settings.PREVIEW_MODE_PREFERENCE_KEY
import ru.kodeks.docmanager.constants.Settings.Timeouts.PREFERENCE_GLOBAL_CATALOG_LAST_UPDATE_TIME
import ru.kodeks.docmanager.constants.Settings.Timeouts.PREFERENCE_GLOBAL_CATALOG_UPDATE_PERIOD
import ru.kodeks.docmanager.model.data.User
import ru.kodeks.docmanager.model.io.RequestBase
import ru.kodeks.docmanager.model.io.SyncRequest
import java.lang.Boolean.parseBoolean
import javax.inject.Inject


abstract class RequestBuilder<T : RequestBase> {

    @Inject
    lateinit var appUser: ru.kodeks.docmanager.User
    private var request: T

    @Inject
    lateinit var app: DocManagerApp

    @Inject
    lateinit var preferences: SharedPreferences

    init {
        @Suppress("LeakingThis")
        request = initRequest().apply {
            user = User(
                login = appUser.login,
                password = appUser.encryptedPassword
//                device = "Android",
//                deviceModel = "${Build.BRAND} ${Build.DEVICE}",
//                androidVersion = Build.VERSION.RELEASE,
//                version = BuildConfig.VERSION_NAME,
//                deviceUid = DeviceUuidFactory(app).deviceUuid.toString()
            )
            preview = parseBoolean(preferences.getString(PREVIEW_MODE_PREFERENCE_KEY, "false"))
        }
    }

    abstract fun initRequest(): T

    fun build(): T {
        return request
    }
}

abstract class DataFilterRequestBuilder<T : RequestBase> : RequestBuilder<T>() {
    protected open fun dataFilter() =
        CLASSIFIERS or DOCUMENTS or SETTINGS or WORKBENCH or WORKBENCH_META
}

class SyncRequestBuilder : DataFilterRequestBuilder<SyncRequest>() {

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
            true -> super.dataFilter() or GLOBAL_OBJECTS
            false -> super.dataFilter()
        }
    }
}