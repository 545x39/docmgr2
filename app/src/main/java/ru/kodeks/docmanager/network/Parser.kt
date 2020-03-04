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
import kotlin.system.measureTimeMillis

class Parser(private val responseFile: String = "${DocManagerApp.instance.responseDirectory}${File.separator}${ResponseFileNames.SYNC_RESPONSE_FILENAME}") {
    private var syncResponse: SyncResponse? = null

    @Suppress("SpellCheckingInspection")
    private val gson = Gson()

    /** Базовый метод для обработыки ошибок как верхнего уровня, так и в дочерних объектах.*/
    private fun ObjectBase.checkForErrors() {
        errors?.apply {
            //TODO S.N.A.F.U. Deal with it.
        }
    }

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

    private fun SyncResponse.persistDocuments() {
        val time = measureTimeMillis {
            documents?.let {
                Database.INSTANCE.documentDao().insertAll(it)
                it
            }?.map {//TODO Try doing it asychronously
                it.apply {
                    persistWidgetLinks()
                    persistAttachments()
                    persistConsiderationStations()
                    persistApprovalRoutes()
                    persistNotes()
                }
            }
        }
        Log.d(TAG, "Persisted documents in $time ms.")
    }

    private fun SyncResponse.persistOrganizations() {
        organizationsList?.forEach { organization ->
            Database.INSTANCE.organizationsDao().insert(organization)
            organization.addresses?.map {
                it.orgUid = organization.uid
                it
            }?.let { Database.INSTANCE.organizationAddressDao().insertAll(it) }
        }
        Log.d(TAG, "Persisted organizations.")
    }

    private fun Workbench.persistWidgetCategories() {
        widgetCategories?.let {
            Database.INSTANCE.widgetCategoryDao().insertAll(it)
            Log.d(TAG, "Persisted widget categories.")
        }
    }

    private fun Workbench.persistWidgetTypes() {
        val time = measureTimeMillis {
            widgetTypes?.let {
                Database.INSTANCE.widgetTypeDao().insertAll(it)
            }
        }
        Log.d(TAG, "Persisted widget types in $time ms.")
    }

    private fun Workbench.persistDesktops() {
        desktops?.let { it ->
            it.forEach {
                Database.INSTANCE.desktopDao().insert(it)
                it.persistWidgets()
                it.persistShortcuts()
            }
        }
    }

    private fun Desktop.persistWidgets() {
        widgets?.map {
            it.desktopId = this.id
            it
        }?.also { Database.INSTANCE.widgetDao().insertAll(it) }
    }

    private fun Desktop.persistShortcuts() {
        shortcuts?.let { Database.INSTANCE.shortcutDao().insertAll(it) }
    }

    private fun Document.persistApprovalRoutes() {
        approvalRoutes?.map {
            it.docUid = this.uid
            it
        }?.let {
            Database.INSTANCE.approvalRouteDao().insertAll(it)
            it
        }?.map {
            it.persistDocLinks()
            it.persistAttachments()
            it.persistApprovalStages()
        }
    }

    private fun ApprovalRoute.persistApprovalStages() {
        stages?.map {
            it.routeId = this.id
            it.docUid = this.docUid
            it
        }?.let {
            Database.INSTANCE.approvalStageDao().insertAll(it)
            it
        }?.map { it.persistApprovalStations() }
    }

    private fun ApprovalStage.persistApprovalStations() {
        stations?.map {
            it.docUid = this.docUid
            it.stageId = this.id
            it
        }?.let {
            Database.INSTANCE.approvalStationDao().insertAll(it)
            it
        }?.map { it.persistAttachments() }
    }

    private fun ApprovalStation.persistAttachments() {
        files?.map {
            it.docUid = this.docUid
            it
        }?.let { Database.INSTANCE.attachmentsDao().insertAll(it) }
    }

    private fun ApprovalRoute.persistAttachments() {
        files?.map {
            it.docUid = this.docUid
            it
        }?.let { Database.INSTANCE.attachmentsDao().insertAll(it) }
    }

    private fun ApprovalRoute.persistDocLinks() {
        docLinks?.map {
            it.doc_uid = this.docUid
            it
        }?.let { Database.INSTANCE.documentLinksDao().insertAll(it) }
    }

    private fun Document.persistAttachments() {
        attachments?.map {
            it.docUid = this.uid
            it
        }?.let { Database.INSTANCE.attachmentsDao().insertAll(it) }
    }

    private fun Document.persistWidgetLinks() {
        widgetLinks?.map {
            it.docUid = this.uid
            if (it.docType == null) {
                it.docType = this.docType
            }
            it
        }?.let { Database.INSTANCE.documentWidgetLinkDao().insertAll(it) }
    }

    private fun Document.persistConsiderationStations() {
        considerationStations?.map {
            it.docUid = this.uid
            it
        }?.let {
            Database.INSTANCE.considerationStationDao().insertAll(it)
            it
        }?.map {
            it.persistDocLinks()
            it.persistAttachments()
        }
    }

    private fun ConsiderationStation.persistDocLinks() {
        docLinks?.map {
            it.doc_uid = this.docUid.orEmpty()
            it
        }?.let { Database.INSTANCE.documentLinksDao().insertAll(it) }
    }

    private fun ConsiderationStation.persistAttachments() {
        files?.map {
                it.docUid = this.docUid.orEmpty()
                it
            }
            ?.let { Database.INSTANCE.attachmentsDao().insertAll(it) }
    }

    private fun Document.persistNotes() {
        notes?.let { Database.INSTANCE.docNoteDao().insertAll(it) }
    }

    /** Это уиды тех документов, все привязки которых к виджетам следует удалить. Сами документы остаются в базе.*/
    private fun SyncResponse.persistUnboundDocuments() {
        ///TODO
    }

    private fun SyncResponse.persistGlobalObjects() {
        val time = measureTimeMillis {
            val list = mutableListOf<GlobalObject>()
            globalObjects?.map {
                list.add(it)
                it.children?.map { child -> list.add(child) }
            }
            Database.INSTANCE.globalObjectDao().insertAll(list)
        }
        Log.d(TAG, "Persisted global objects in $time ms.")
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
