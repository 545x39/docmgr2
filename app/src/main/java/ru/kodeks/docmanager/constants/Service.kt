package ru.kodeks.docmanager.constants

sealed class Constant

object ServiceMethod : Constant() {
    /**
     * @brief Метод сервиса для синхронизации
     */
    const val SYNC_SVC = "Sync.svc"
    /**
     * @brief Метод сервиса для операций с документами
     */
    const val DOCS_SVC = "Docs.svc"
    /**
     * @brief Метод сервиса для операций с файлами
     */
    const val FILES_SVC = "Files.svc"
    /**
     * @brief Метод сервиса для онлайн-поиска
     */
    const val SEARCH_SVC = "Search.svc"
    /**
     * @brief Метод сервиса для отложенных запросов
     */
    const val GET_DEFERRED_RESPONSE_URL_PATH = "GetDeferredResponse.ashx"
    const val GET_RESOURCE_URL_PATH = "GetResource.ashx"
    const val UPLOAD_FILE_URL_PATH = "UploadFile.ashx"
}

object DataFilter : Constant() {
    /**
     * @brief Все данные. Значение по умолчанию (можно не передавать или передать 0)
     */
    const val ALL = 0
    /**
     * @brief Настройки пользователя.
     */
    const val SETTINGS = 1
    /**
     * @brief Справочники (не включая ГК): связи и записи из CL_ELEMENTS.
     */
    const val CLASSIFIERS = 2
    /**
     * @brief Глобальный каталог.
     */
    const val GLOBAL_OBJECTS = 4
    /**
     * @brief Метаинформация по виджетам (типы и категории, а также метаинформация по
     * рабочим столам и виджетам).
     */
    const val WORKBENCH_META = 8
    /**
     * @brief Рабочие столы и виджеты (наполнение объектов метаинформацией зависит от
     * флага WorkbenchMeta).
     */
    const val WORKBENCH = 16
    /**
     * @brief Документы вместе со всеми дочерними объектами: пометки, инстанции,
     * вложения, связанные документы и привязки к виджетам.
     */
    const val DOCUMENTS = 32
    /**
     * @brief Список УИДов документов в каждом виджете (Widget::oldDocUids), которые не
     * изменились с переданного сиквенса. Для инициальных виджетов этот список
     * всегда пуст.
     */
    const val OLD_DOCUIDS = 64
    /**
     * @brief Личные папки
     */
    const val PERSONAL_FOLDERS = 128
    /**
     * @brief Подборки
     */
    const val COMPILATIONS = 256
    /**
     * @brief Общие папки
     */
    const val SHARED_FOLDERS = 512
}

object JsonNames{
    const val ERRORS = "errors"
    const val DATA_FILTER = "dataFilter"
    const val SERVER = "server"
    const val VERSION = "version"
    const val USER = "user"
    const val SETTINGS = "settings"
    const val GLOBAL_OBJECTS = "globalObjects"
    const val WORKBENCH = "workbench"
    const val GLOBAL_OBJECTS_COUNT = "globalObjectsCount"
    const val DOCS = "docs"
    const val DOCS_COUNT = "docsCount"
    const val NEW_UNBOUND_DOC_UIDS = "newUnboundDocUids"
    const val LINK_TYPES = "linkTypes"
    const val ORGANIZATIONS = "organizations"
    const val CLASSIFIERS = "classifiers"
    const val ORGANIZATIONS_COUNT = "organizationsCount"
    const val DOC_NOTE_ACTIONS = "docNoteActions"
    const val STATION_SIGNATURES = "stationSignatures"
    const val STATIONS_TO_ARCHIVE = "stationsToArchive"
    const val DOC_ACTIONS = "docActions"
    const val APPROVAL_ACTIONS = "approvalActions"
    const val SIGNED_DOCS = "signedDocs"
    const val GROUPED_STATIONS = "groupedStations"
    const val REPORT_STATIONS = "reportStations"
    const val FOLDERS = "folders"
    const val COMPILATIONS = "compilations"
    //
    const val REJECTED_DOCS = "rejectedDocs"
    //
    const val REQUEST_KEY = "requestKey"
    const val DELAY = "delay"
    const val BLOB_LENGTHS = "blobLengths"
    const val PREVIEW = "preview"
}
