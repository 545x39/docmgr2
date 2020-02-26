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
        Log.d(TAG, "Started parsing response")
        syncResponse = gson.fromJson(FileReader(responseFile), SyncResponse::class.java)
        Log.d(TAG, "Response read. Persisting it to the DB...")
        syncResponse?.apply {
            persistInitData()
            persistSettings()
            persistClassifiers()
//            persistGlobalObjects(globalObjects)
            persistWorkbench()
            persistOrganizations()
//            persistDocuments()
            persistUnboundDocuments()
        }
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
            //TODO S.N.A.F.U., deal with it.
        }
    }

    private fun SyncResponse.persistOrganizations() {
        organizationsList?.forEach { organization ->
            Database.INSTANCE.organizationsDao().insert(organization)
            Log.d(TAG, "Inserted organization: ${organization.fullName}")
            organization.addresses?.forEach { address ->
                address.apply {
                    orgUid = organization.uid
                    Database.INSTANCE.organizationAddressDao().insert(this)
                }
            }
        }
    }

    private fun SyncResponse.parseErrors() {
        //TODO
    }

    private fun Workbench.persistDesktops() {
        desktops?.let {
            //TODO
        }
    }

    private fun Workbench.persistWidgetTypes() {
        widgetTypes?.let {
            //TODO
        }
    }

    private fun Workbench.persistWidgetCategories() {
        widgetCategories?.let {
            //TODO
        }
    }

    private fun SyncResponse.persistDocuments() {
        this.documents?.forEach {
            it.persistNotes()
        }
    }

    private fun Document.persistNotes() {
        notes?.forEach { Database.INSTANCE.docNoteDao().insert(it) }
    }

    /** Это уиды тех документов, все привязки которых к виджетам следует удалить. Сами документы остаются в базе.*/
    private fun SyncResponse.persistUnboundDocuments() {
        ///TODO
    }

    private fun SyncResponse.persistGlobalObjects(globalObjects: List<GlobalObject>?) {
        globalObjects?.forEach {
            Log.d(TAG, "persisting gl_obj: ${it.surname} ${it.firstName} ${it.patronymic}")
            Database.INSTANCE.globalObjectDao().insert(it)
            it.children?.apply { persistGlobalObjects(this) }
        }
    }

    /**
     * Сюда передаётся коллекция классификаторов для сохранения их элементов в "плоском" виде.
     */
    private fun SyncResponse.persistClassifiers() {
        val list = ArrayList<ClassifierItem>()
        classifiers?.values?.forEach { classifier ->
            classifier.items?.forEach {
                it.parentId = classifier.classifierId
                list.add(it)
            }
        }
        Database.INSTANCE.classifiersDao().insertAll(list)
    }

    private fun SyncResponse.persistInitData() {
        Database.INSTANCE.initDao().insert(
            InitData(
                login = DocManagerApp.instance.user.login!!,
                password = DocManagerApp.instance.user.password!!,
                userUid = user?.uid ?: "",
                serverVersion = serverVersion ?: "",
                sequence = version?.main ?: 0,
                globalSequence = version?.global ?: 0,
                settingsSequence = version?.settings ?: 0,
                userRights = user?.rights.orEmpty()
            )
        )
    }

    private fun SyncResponse.persistSettings() {
        settings?.let {
            for (setting: Setting in it) {
                Database.INSTANCE.settingsDao().insert(setting)
            }
        }
    }
}