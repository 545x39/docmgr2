package ru.kodeks.docmanager.persistence.parser

import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import ru.kodeks.docmanager.const.PathsAndFileNames.SYNC_RESPONSE_FILENAME
import ru.kodeks.docmanager.di.const.RESPONSE_DIR
import ru.kodeks.docmanager.model.data.*
import ru.kodeks.docmanager.model.io.SyncResponse
import ru.kodeks.docmanager.persistence.Database
import timber.log.Timber
import java.io.File
import java.io.FileReader
import javax.inject.Inject
import javax.inject.Named
import kotlin.system.measureTimeMillis

class Parser @Inject constructor(
    var database: Database,
    @Named(RESPONSE_DIR)
    var responseDir: String
) {

    private var syncResponse: SyncResponse? = null
    private lateinit var job: Job
    private var login: String = ""
    private var password: String = ""

    @Suppress("SpellCheckingInspection")
    private val gson = Gson()

    /** Базовый метод для обработки ошибок как верхнего уровня, так и в дочерних объектах.*/
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

    suspend fun parse(
        responseFile: String = "$responseDir${File.separator}${SYNC_RESPONSE_FILENAME}",
        login: String = "",
        password: String = ""
    ) {
        this.login = login
        this.password = password
        job = CoroutineScope(IO).launch {
            Timber.d("Started parsing response")
            syncResponse = gson.fromJson(FileReader(responseFile), SyncResponse::class.java)
            Timber.d("Response read. Persisting it to the DB...")
            syncResponse?.apply {
                val time = measureTimeMillis {
                    listOf(
                        async(start = CoroutineStart.LAZY) { persistUser() },
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
                Timber.d("PERSISTING RESPONSE TOOK: $time")
            }
        }
        job.join()
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
                database.documentDao().insertAll(it)
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
        Timber.d("Persisted documents in $time ms.")
    }

    private suspend fun SyncResponse.persistOrganizations() {
        val time = measureTimeMillis {
            organizationsList?.let {
                database.organizationsDao().insertAll(it)
                it
            }?.parallelMap {
                it.addresses?.parallelMap { address ->
                    address.orgUid = it.uid
                    address
                }?.let { addresses ->
                    database.organizationAddressDao().insertAll(addresses)
                }
            }
        }
        Timber.d("Persisted organizations int $time ms.")
    }

    private fun Workbench.persistWidgetCategories() {
        widgetCategories?.let {
            database.widgetCategoryDao().insertAll(it)
            Timber.d("Persisted widget categories.")
        }
    }

    private suspend fun Workbench.persistWidgetTypes() {
        val time = measureTimeMillis {
            widgetTypes?.let {
                database.widgetTypeDao().insertAll(it)
            }
        }
        Timber.d("Persisted widget types in $time ms.")
    }

    private suspend fun Workbench.persistDesktops() {
        desktops?.let {
            database.desktopDao().insertAll(it)
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
        }?.also { database.widgetDao().insertAll(it) }
    }

    private suspend fun Desktop.persistShortcuts() {
        shortcuts?.let { database.shortcutDao().insertAll(it) }
    }

    private suspend fun Document.persistApprovalRoutes() {
        approvalRoutes?.parallelMap {
            it.docUid = this.uid
            it
        }?.let {
            database.approvalRouteDao().insertAll(it)
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
            database.approvalStageDao().insertAll(it)
            it
        }?.parallelMap { it.persistApprovalStations() }
    }

    private suspend fun ApprovalStage.persistApprovalStations() {
        stations?.parallelMap {
            it.docUid = this.docUid
            it.stageId = this.id
            it
        }?.let {
            database.approvalStationDao().insertAll(it)
            it
        }?.parallelMap { it.persistAttachments() }
    }

    private suspend fun ApprovalStation.persistAttachments() {
        files?.parallelMap {
            it.docUid = this.docUid
            it
        }?.let { database.attachmentsDao().insertAll(it) }
    }

    private suspend fun ApprovalRoute.persistAttachments() {
        files?.parallelMap {
            it.docUid = this.docUid
            it
        }?.let { database.attachmentsDao().insertAll(it) }
    }

    private suspend fun ApprovalRoute.persistDocLinks() {
        docLinks?.parallelMap {
            it.doc_uid = this.docUid
            it
        }?.let { database.documentLinksDao().insertAll(it) }
    }

    private suspend fun Document.persistAttachments() {
        attachments?.parallelMap {
            it.docUid = this.uid
            it
        }?.let { database.attachmentsDao().insertAll(it) }
    }

    private suspend fun Document.persistWidgetLinks() {
        widgetLinks?.parallelMap {
            it.docUid = this.uid
            if (it.docType == null) {
                it.docType = this.docType
            }
            it
        }?.let { database.documentWidgetLinkDao().insertAll(it) }
    }

    private suspend fun Document.persistConsiderationStations() {
        considerationStations?.parallelMap {
            it.docUid = this.uid
            it
        }?.let {
            database.considerationStationDao().insertAll(it)
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
        }?.let { database.documentLinksDao().insertAll(it) }
    }

    private suspend fun ConsiderationStation.persistAttachments() {
        files?.parallelMap {
            it.docUid = this.docUid.orEmpty()
            it
        }
            ?.let { database.attachmentsDao().insertAll(it) }
    }

    private suspend fun Document.persistNotes() {
        notes?.let { database.docNoteDao().insertAll(it) }
    }

    /** Это уиды тех документов, все привязки которых к виджетам следует удалить. Сами документы остаются в базе.*/
    private suspend fun SyncResponse.unboundDocuments() {
        newUnboundDocUids?.let { database.documentWidgetLinkDao().deleteAll(it) }
    }

    private suspend fun SyncResponse.persistGlobalObjects() {
        val time = measureTimeMillis {
            val list = mutableListOf<GlobalObject>()
            globalObjects?.map {
                list.add(it)
                it.children?.map { child -> list.add(child) }
            }
            database.globalObjectDao().insertAll(list)
        }
        Timber.d("Persisted global objects in $time ms.")
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
        database.classifiersDao().insertAll(list)
        Timber.d("Persisted classifiers.")
    }

    private suspend fun SyncResponse.persistUser() {
        database.userDAO().insert(
            User(
                login = this@Parser.login,
                password = this@Parser.password,
                uid = user?.uid.orEmpty(),
                serverVersion = serverVersion.orEmpty(),
                sequence = version?.main ?: 0,
                globalSequence = version?.global ?: 0,
                settingsSequence = version?.settings ?: 0,
                rights = user?.rights.orEmpty()
            )
        )
    }

    private suspend fun SyncResponse.persistSettings() {
        settings?.let { database.settingsDao().insertAll(it) }
    }
}

suspend fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B): List<B> = coroutineScope {
    map {
        async { f(it) }
    }.awaitAll()
}