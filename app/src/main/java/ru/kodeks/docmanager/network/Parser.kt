package ru.kodeks.docmanager.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import ru.kodeks.docmanager.constants.JsonNames.SERVER
import ru.kodeks.docmanager.constants.JsonNames.USER
import ru.kodeks.docmanager.constants.LogTag.TAG
import ru.kodeks.docmanager.constants.ResponseFileNames
import ru.kodeks.docmanager.model.data.*
import ru.kodeks.docmanager.model.io.SyncResponse
import ru.kodeks.docmanager.persistence.Database
import ru.kodeks.docmanager.util.DocManagerApp
import java.io.*
import java.io.File

class Parser(val responseFile: String = "${DocManagerApp.instance.responseDirectory}${File.separator}${ResponseFileNames.SYNC_RESPONSE_FILENAME}") {
    private var syncResponse: SyncResponse? = null
    private val gson = Gson()
    private val reader: JsonReader

    init {
        val inputStream: InputStream = FileInputStream(File(responseFile))
        reader = JsonReader(InputStreamReader(inputStream))
        reader.isLenient = true
    }

    fun parse() {
        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            Log.d(TAG, "Parsing $name")
            parseItem(name)
        }
        reader.close()
        Log.d(TAG, "Started parsing response")
        syncResponse = gson.fromJson(FileReader(responseFile), SyncResponse::class.java)
        Log.d(TAG, "Response read. Persisting it to the DB...")
        parseInitData()
        parseSettings()
        parseClassifiers()
        parseGlobalObjects()
        val documents = syncResponse?.documents
        documents?.apply {
            for (document: Document in this) {
                document.notes?.let {
                    for (note: DocNote in it) {
//                        Log.d(TAG, "Inserting note UID ${note.uid}, document UID ${document.uid}")
                        Database.INSTANCE.docNoteDao().insert(note)
                    }
                }
            }
        }
        Log.d(TAG, "Response ITEM: ${syncResponse?.classifiers}")
    }

    private fun parseGlobalObjects() {
        syncResponse?.globalObjects?.let { persistGlobalObjects(it) }
    }

    private fun persistGlobalObjects(globalObjects: List<GlobalObject>) {
        globalObjects.forEach {
            it.children?.apply { persistGlobalObjects(this) }
            Log.d(TAG, "Persisting ${it.surname} ${it.firstName} ${it.patronymic}")
            Database.INSTANCE.golbalObjectDao().insert(it)
        }
    }

    private fun parseClassifiers() {
        syncResponse?.classifiers?.let { persistClassifiers(it) }
    }

    /**
     * Сюда передаётся коллекция классификаторов для сохранения их элементов в "плоском" виде.
     */
    fun persistClassifiers(classifiers: Map<String, Classifier>?) {
        val list = ArrayList<ClassifierItem>()
        classifiers?.values?.forEach { classifier ->
            classifier.items?.forEach {
                it.parentId = classifier.classifierId
                list.add(it)
            }
        }
        Database.INSTANCE.classifiersDao().insertAll(list)
    }

    /////////////////////////////////////////////////////////////////
    private fun parseInitData() {
        syncResponse?.apply {
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

    }

    private fun parseSettings() {
        Log.d(TAG, "Starting to parse settings... Settings count: ${syncResponse?.settings?.size}")
        syncResponse?.settings?.let {
            for (setting: Setting in it) {
                Database.INSTANCE.settingsDao().insert(setting)
            }
            Log.d(TAG, "${syncResponse?.settings?.size ?: 0} settings parsed.")
        }
    }

    private fun parseItem(name: String) {
        when (name) {
//            ERRORS -> {
//            }
            SERVER -> {
                Log.d(TAG, "Server version: ${reader.nextString()}")
            }
//            VERSION -> {
//            }
            USER -> parseUser()
//            SETTINGS -> {
//            }
//            GLOBAL_OBJECTS -> {
//            }
//            GLOBAL_OBJECTS_COUNT -> {
//            }
//            WORKBENCH -> {
//            }
//            DOCS -> parseDocument()
//            DOCS_COUNT -> {
//            }
//            NEW_UNBOUND_DOC_UIDS -> {
//            }
//            LINK_TYPES -> {
//            }
//            ORGANIZATIONS -> {
//            }
//            ORGANIZATIONS_COUNT -> {
//            }
//            DOC_NOTE_ACTIONS -> {
//            }
//            STATION_SIGNATURES -> {
//            }
//            STATIONS_TO_ARCHIVE -> {
//            }
//            DOC_ACTIONS -> {
//            }
//            APPROVAL_ACTIONS -> {
//            }
//            SIGNED_DOCS -> {
//            }
//            GROUPED_STATIONS -> {
//            }
//            REPORT_STATIONS -> {
//            }
//            FOLDERS -> {
//            }
//            COMPILATIONS -> {
//            }
            else -> {
                reader.skipValue()
            }
        }
    }

    private fun parseUser() {
        val user = gson.fromJson<User>(reader, User::class.java)
    }

    private fun parseDocument() {
        reader.skipValue()
    }
}