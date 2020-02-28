package ru.kodeks.docmanager.network

import android.util.Log
import com.google.gson.Gson
import ru.kodeks.docmanager.constants.LogTag.TAG
import ru.kodeks.docmanager.constants.ResponseFileNames
import ru.kodeks.docmanager.model.data.*
import ru.kodeks.docmanager.model.io.SyncResponse
import ru.kodeks.docmanager.persistence.Database
import ru.kodeks.docmanager.util.DocManagerApp
import java.io.File
import java.io.FileReader

class Parser(private val responseFile: String = "${DocManagerApp.instance.responseDirectory}${File.separator}${ResponseFileNames.SYNC_RESPONSE_FILENAME}") {
    private var syncResponse: SyncResponse? = null
    @Suppress("SpellCheckingInspection")
    private val gson = Gson()


    fun parse() {
        val time = System.currentTimeMillis()
        Log.d(TAG, "Started parsing response")
        syncResponse = gson.fromJson(FileReader(responseFile), SyncResponse::class.java)
        Log.d(TAG, "Response read. Persisting it to the DB...")
        syncResponse?.apply {
            persistInitData()
            persistSettings()
            persistClassifiers()
            persistGlobalObjects()
            persistWorkbench()
            persistOrganizations()
            persistDocuments()
            persistUnboundDocuments()
        }
        Log.d(TAG, "PERSISTING RESPONSE TOOK: ${System.currentTimeMillis() - time}")
    }

    private fun SyncResponse.persistWorkbench() {
        workbench?.apply {
            persistWidgetCategories()
            persistWidgetTypes()
            persistDesktops()
        }
    }

    /** Базовый метод для обработыки ошибок как верхнего уровня, так и в дочерних объектах.*/
    private fun ObjectBase.checkForErrors() {
        errors?.apply {
            //TODO S.N.A.F.U. Deal with it.
        }
    }

    private fun SyncResponse.persistOrganizations() {
        organizationsList?.forEach { organization ->
            Database.INSTANCE.organizationsDao().insert(organization)
            organization.addresses?.forEach { address ->
                address.apply {
                    orgUid = organization.uid
                    Database.INSTANCE.organizationAddressDao().insert(this)
                }
            }
        }
        Log.d(TAG, "Persisted organizations.")
    }

    private fun SyncResponse.parseErrors() {
        //TODO
    }

    private fun Workbench.persistWidgetCategories() {
        widgetCategories?.let {
            Database.INSTANCE.widgetCategoryDao().insertAll(it)
            Log.d(TAG, "Persisted widget categories.")
        }
    }

    private fun Workbench.persistWidgetTypes() {
        val time = System.currentTimeMillis()
        widgetTypes?.let {
            Database.INSTANCE.widgetTypeDao().insertAll(it)
            Log.d(TAG, "Persisted widget types in ${System.currentTimeMillis() - time} ms.")
        }
    }

    private fun Workbench.persistDesktops() {
        desktops?.let { it ->
            it.forEach {
                Database.INSTANCE.desktopDao().insert(it)
                it.persistWidgets(it.id)
                it.persistShortcuts()
            }
        }
    }

    private fun Desktop.persistWidgets(desktopId: Int) {
        widgets?.map {
            it.desktopId = desktopId
            it
        }?.also { Database.INSTANCE.widgetDao().insertAll(it) }
    }

    private fun Desktop.persistShortcuts() {
        shortcuts?.let { Database.INSTANCE.shortcutDao().insertAll(it) }
    }

    private fun SyncResponse.persistDocuments() {
        val time = System.currentTimeMillis()
        documents?.let {
            Database.INSTANCE.documentDao().insertAll(it)
            it
        }?.map {
            it.apply {
                persistConsiderationStations()
                persistNotes()
            }
        }
        Log.d(TAG, "Persisted documents in ${System.currentTimeMillis() - time} ms.")
    }

    private fun Document.persistConsiderationStations() {
        considerationStations?.let {
            Database.INSTANCE.considerationStationDao().insertAll(it)
            it
        }?.map {
            //TODO Save doc links
            //TODO Save attachments
        }
    }

    private fun Document.persistNotes() {
        notes?.let { Database.INSTANCE.docNoteDao().insertAll(it) }
    }

    /** Это уиды тех документов, все привязки которых к виджетам следует удалить. Сами документы остаются в базе.*/
    private fun SyncResponse.persistUnboundDocuments() {
        ///TODO
    }

    private fun SyncResponse.persistGlobalObjects() {
        val time = System.currentTimeMillis()
        val list = mutableListOf<GlobalObject>()
        globalObjects?.map {
            list.add(it)
            it.children?.map { child -> list.add(child) }
        }
        Database.INSTANCE.globalObjectDao().insertAll(list)
        Log.d(TAG, "Persisted global objects in ${System.currentTimeMillis() - time} ms.")
    }

    /**
     * Сюда передаётся коллекция классификаторов для сохранения их элементов в "плоском" виде.
     */
    private fun SyncResponse.persistClassifiers() {
        val list = ArrayList<ClassifierItem>()
        classifiers?.values?.map { classifier ->
            classifier.items?.map {
                it.parentId = classifier.classifierId
                list.add(it)
            }
        }
        Database.INSTANCE.classifiersDao().insertAll(list)
        Log.e(TAG, "Persisted classifiers.")
    }

    private fun SyncResponse.persistInitData() {
        Database.INSTANCE.initDao().insert(
            InitData(
                login = DocManagerApp.instance.user.login.orEmpty(),
                password = DocManagerApp.instance.user.password.orEmpty(),
                userUid = user?.uid.orEmpty(),
                serverVersion = serverVersion.orEmpty(),
                sequence = version?.main ?: 0,
                globalSequence = version?.global ?: 0,
                settingsSequence = version?.settings ?: 0,
                userRights = user?.rights.orEmpty()
            )
        )
    }

    private fun SyncResponse.persistSettings() {
        settings?.let { Database.INSTANCE.settingsDao().insertAll(it) }
    }
}