package ru.kodeks.docmanager.network

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
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
    private lateinit var job: Job

    @Suppress("SpellCheckingInspection")
    private val gson = Gson()

    /** Базовый метод для обработыки ошибок как верхнего уровня, так и в дочерних объектах.*/
    private fun ObjectBase.checkForErrors() {
        errors?.apply {
            //TODO S.N.A.F.U. Deal with it.
        }
    }

    private fun runParallel(vararg functions: Array<() -> Unit>) {
        CoroutineScope(IO).launch {
            functions.map { async { it } }.awaitAll()
        }
    }

    fun parse() {
        job = CoroutineScope(IO).launch {
            Log.d(TAG, "Started parsing response")
            syncResponse = gson.fromJson(FileReader(responseFile), SyncResponse::class.java)
            Log.d(TAG, "Response read. Persisting it to the DB...")
            syncResponse?.apply {
                val time = measureTimeMillis {
                    listOf(
                        async(start = CoroutineStart.LAZY) { persistInitData() },
                        async(start = CoroutineStart.LAZY) { persistSettings() },
                        async(start = CoroutineStart.LAZY) { persistClassifiers() },
                        async(start = CoroutineStart.LAZY) { persistGlobalObjects() },
                        async(start = CoroutineStart.LAZY) { persistWorkbench() },
                        async(start = CoroutineStart.LAZY) { persistOrganizations() },
                        async(start = CoroutineStart.LAZY) {
                            persistDocuments()
                            unboundDocuments()
                        }
                    ).map {
                        it.start()
                        it
                    }.awaitAll()
                }
                Log.d(TAG, "PERSISTING RESPONSE TOOK: $time")
            }
        }
    }

    private suspend fun SyncResponse.persistWorkbench() {
        workbench?.apply {
            persistWidgetCategories()
            persistWidgetTypes()
            persistDesktops()
        }
    }

    private suspend fun SyncResponse.persistDocuments() {
        val time = measureTimeMillis {
            documents?.let {
                Database.INSTANCE.documentDao().insertAll(it)
                it
            }?.parallelMap {
                it.apply {
                    withContext(IO) {
                        listOf(
                            async(start = CoroutineStart.LAZY) { persistWidgetLinks() },
                            async(start = CoroutineStart.LAZY) { persistAttachments() },
                            async(start = CoroutineStart.LAZY) { persistConsiderationStations() },
                            async(start = CoroutineStart.LAZY) { persistApprovalRoutes() },
                            async(start = CoroutineStart.LAZY) { persistNotes() }
                        ).map { func ->
                            func.start()
                            func
                        }.awaitAll()
                    }
                }
            }
        }
        Log.d(TAG, "Persisted documents in $time ms.")
    }

    private suspend fun SyncResponse.persistOrganizations() {
        val time = measureTimeMillis {
            organizationsList?.let {
                Database.INSTANCE.organizationsDao().insertAll(it)
                it
            }?.parallelMap {
                it.addresses?.parallelMap { address ->
                    address.orgUid = it.uid
                    address
                }?.let { addresses ->
                    Database.INSTANCE.organizationAddressDao().insertAll(addresses)
                }
            }
        }
        Log.d(TAG, "Persisted organizations int $time ms.")
    }

    private fun Workbench.persistWidgetCategories() {
        widgetCategories?.let {
            Database.INSTANCE.widgetCategoryDao().insertAll(it)
            Log.d(TAG, "Persisted widget categories.")
        }
    }

    private suspend fun Workbench.persistWidgetTypes() {
        val time = measureTimeMillis {
            widgetTypes?.let {
                Database.INSTANCE.widgetTypeDao().insertAll(it)
            }
        }
        Log.d(TAG, "Persisted widget types in $time ms.")
    }

    private suspend fun Workbench.persistDesktops() {
        desktops?.let {
            Database.INSTANCE.desktopDao().insertAll(it)
            it
        }?.parallelMap {
            it.persistWidgets()
            it.persistShortcuts()
        }
    }

    private suspend fun Desktop.persistWidgets() {
        widgets?.map {
            it.desktopId = this.id
            it
        }?.also { Database.INSTANCE.widgetDao().insertAll(it) }
    }

    private suspend fun Desktop.persistShortcuts() {
        shortcuts?.let { Database.INSTANCE.shortcutDao().insertAll(it) }
    }

    private suspend fun Document.persistApprovalRoutes() {
        approvalRoutes?.parallelMap {
            it.docUid = this.uid
            it
        }?.let {
            Database.INSTANCE.approvalRouteDao().insertAll(it)
            it
        }?.parallelMap {
            withContext(IO) {
                listOf(
                    async(start = CoroutineStart.LAZY) { it.persistDocLinks() },
                    async(start = CoroutineStart.LAZY) { it.persistAttachments() },
                    async(start = CoroutineStart.LAZY) { it.persistApprovalStages() }
                ).map {
                    it.start()
                    it
                }.awaitAll()
            }
        }
    }

    private suspend fun ApprovalRoute.persistApprovalStages() {
        stages?.parallelMap {
            it.routeId = this.id
            it.docUid = this.docUid
            it
        }?.let {
            Database.INSTANCE.approvalStageDao().insertAll(it)
            it
        }?.parallelMap { it.persistApprovalStations() }
    }

    private suspend fun ApprovalStage.persistApprovalStations() {
        stations?.parallelMap {
            it.docUid = this.docUid
            it.stageId = this.id
            it
        }?.let {
            Database.INSTANCE.approvalStationDao().insertAll(it)
            it
        }?.parallelMap { it.persistAttachments() }
    }

    private suspend fun ApprovalStation.persistAttachments() {
        files?.parallelMap {
            it.docUid = this.docUid
            it
        }?.let { Database.INSTANCE.attachmentsDao().insertAll(it) }
    }

    private suspend fun ApprovalRoute.persistAttachments() {
        files?.parallelMap {
            it.docUid = this.docUid
            it
        }?.let { Database.INSTANCE.attachmentsDao().insertAll(it) }
    }

    private suspend fun ApprovalRoute.persistDocLinks() {
        docLinks?.parallelMap {
            it.doc_uid = this.docUid
            it
        }?.let { Database.INSTANCE.documentLinksDao().insertAll(it) }
    }

    private suspend fun Document.persistAttachments() {
        attachments?.parallelMap {
            it.docUid = this.uid
            it
        }?.let { Database.INSTANCE.attachmentsDao().insertAll(it) }
    }

    private suspend fun Document.persistWidgetLinks() {
        widgetLinks?.parallelMap {
            it.docUid = this.uid
            if (it.docType == null) {
                it.docType = this.docType
            }
            it
        }?.let { Database.INSTANCE.documentWidgetLinkDao().insertAll(it) }
    }

    private suspend fun Document.persistConsiderationStations() {
        considerationStations?.parallelMap {
            it.docUid = this.uid
            it
        }?.let {
            Database.INSTANCE.considerationStationDao().insertAll(it)
            it
        }?.parallelMap {
            withContext(IO) {
                listOf(
                    async(start = CoroutineStart.LAZY) { it.persistDocLinks() },
                    async(start = CoroutineStart.LAZY) { it.persistAttachments() }
                ).map {
                    it.start()
                    it
                }.awaitAll()
            }
        }
    }

    private suspend fun ConsiderationStation.persistDocLinks() {
        docLinks?.parallelMap {
            it.doc_uid = this.docUid.orEmpty()
            it
        }?.let { Database.INSTANCE.documentLinksDao().insertAll(it) }
    }

    private suspend fun ConsiderationStation.persistAttachments() {
        files?.parallelMap {
                it.docUid = this.docUid.orEmpty()
                it
            }
            ?.let { Database.INSTANCE.attachmentsDao().insertAll(it) }
    }

    private suspend fun Document.persistNotes() {
        notes?.let { Database.INSTANCE.docNoteDao().insertAll(it) }
    }

    /** Это уиды тех документов, все привязки которых к виджетам следует удалить. Сами документы остаются в базе.*/
    private suspend fun SyncResponse.unboundDocuments() {
        newUnboundDocUids?.let { Database.INSTANCE.documentWidgetLinkDao().deleteAll(it) }
    }

    private suspend fun SyncResponse.persistGlobalObjects() {
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
    private suspend fun SyncResponse.persistClassifiers() {
        val list = ArrayList<ClassifierItem>()
        classifiers?.values?.map { classifier ->
            classifier.items?.map {
                it.parentId = classifier.classifierId
                list.add(it)
            }
        }
        Database.INSTANCE.classifiersDao().insertAll(list)
        Log.d(TAG, "Persisted classifiers.")
    }

    private suspend fun SyncResponse.persistInitData() {
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

    private suspend fun SyncResponse.persistSettings() {
        settings?.let { Database.INSTANCE.settingsDao().insertAll(it) }
    }
}

suspend fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B): List<B> = coroutineScope {
    map {
        async { f(it) }
    }.awaitAll()
}