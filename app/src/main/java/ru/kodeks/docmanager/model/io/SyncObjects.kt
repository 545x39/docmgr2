package ru.kodeks.docmanager.model.io

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.const.JsonNames.CLASSIFIERS
import ru.kodeks.docmanager.const.JsonNames.DATA_FILTER
import ru.kodeks.docmanager.const.JsonNames.SETTINGS
import ru.kodeks.docmanager.const.JsonNames.VERSION
import ru.kodeks.docmanager.model.data.*

/** Категория данных для фильтрации ответа, битовая маска. Используется для кастомизации начального слепка данных системы.
Например, передав в сервис значение 16, последний вернет только базовую информацию о пользователе, версию системы (сиквенсы)
и документы.*/
object DataFilter {
    /**
     * Все данные. Значение по умолчанию (можно не передавать или передать 0).
     */
    const val ALL = 0

    /**
     * Настройки пользователя.
     */
    const val SETTINGS = 1

    /**
     * Справочники (не включая ГК): связи и записи из \b CL_ELEMENTS.
     */
    const val CLASSIFIERS = 2

    /**
     * Глобальный каталог.
     */
    const val GLOBAL_OBJECTS = 4

    /**
     * Метаинформация по виджетам (типы и категории, а также метаинформация по рабочим столам и виджетам).
     */
    const val WORKBENCH_META = 8

    /**
     * Рабочие столы и виджеты (наполнение объектов метаинформацией зависит от флага <see cref="WorkbenchMeta"></see>).
     */
    const val WORKBENCH = 16

    /**
     * Документы вместе со всеми дочерними объектами: пометки, инстанции, вложения, связанные документы и привязки к виджетам.
     */
    const val DOCUMENTS = 32

    /**
     * Список УИДов документов в каждом виджете (\link DataModel.Widget Widget::oldDocUids\endlink), которые не изменились с переданного сиквенса.
     * Для инициальных виджетов этот список всегда пуст.
     */
    const val OLD_DOC_UIDS = 64

    /**
     * Личные папки и метаданные по дочерним файлам и документам.
     */
    const val PRIVATE_FOLDERS = 128

    /**
     * Категории и подборки документов для губернатора.
     */
    const val COMPILATIONS = 256

    /**
     * Общие папки и метаданные по дочерним файлам и документам.
     */
    const val SHARED_FOLDERS = 512

    /**
     * Счетчики документов по виджетам (без 16 не работает)
     */
    const val WIDGET_DOC_COUNTERS = 1024
}

/** Запрос на синхронизацию данных (как начальную, так и инкрементальную). Другими словами, запрос на инит и апдейт.
Ответ: (SyncResponse").
Пример 1: начальная синхронизация
<pre><code>
 *    {
 *      "user": {
 *        "login": "gromov",
 *        "password": "11111",
 *        "device": "iOS",
 *        "version": "1.2.0"
 *      }
 *    }
</code></pre>
<h3>Пример 2: быстрая проверка синхронизации - запрашиваем только настройки</h3>
<pre><code>
 *    {
 *      "user": {
 *        "login": "gromov",
 *        "password": "11111",
 *        "device": "iOS",
 *        "version": "1.2.0"
 *      },
 *      "dataFilter": 2,
 *      "runDeferred": false
 *    }
</code></pre>
<h3>Пример 3: инкрементальная синхронизация - запрашиваем только документы для одного виджета по определенному сиквенсу</h3>
<pre><code>
 *    {
 *      "user": {
 *        "login": "gromov",
 *        "password": "11111",
 *        "device": "iOS",
 *        "version": "1.2.0"
 *      },
 *      "version": {
 *        "main": 860900,
 *        "global": 455617,
 *        "settings": 1
 *      },
 *      "dataFilter": 32,
 *      "widgets": [{
 *          "type" : 16,
 *          "sequence" : 860900
 *        }, {
 *          "type" : 17,
 *          "sequence" : 860900
 *      }]
 *    }
</code></pre>*/
class SyncRequest(

    /** Фильтр данных <see cref="IoModel.DataFilter"/>, битовая маска (сервис вернет только запрошенные данные).
    По умолчанию возвращаются все данные (значение 0 или отсутствующее значение).*/
    @SerializedName(DATA_FILTER)
    @Expose
    var dataFilter: Int? = null,

    /** Версия данных (сиквенсы) клиента, соответствующая последнему успешному обновлению данных.
    В случае начальной синхронизации можно передать 0 во все сиквенсы или не заполнять этот объект вообще.*/
    @SerializedName(VERSION)
    @Expose
    var version: Version? = null,

    /** Список виджетов для точной синхронизации на основе индивидуальных сиквенсов каждого виджета.
    В виджетах сервер анализирует только поля type и sequence. Если поле sequence не заполнено,
    то его значение принимается равным "version" main.
    Если клиент передал данную коллекцию, то "initialWidgetTypes" и "incrementalWidgetTypes" не анализируются.
    Если списки initialWidgetTypes, incrementalWidgetTypes и "widgets" не передаются
    (они null, а не []), но запрошено получение документов, то сервис считает, что нужно грузить все документы
    по всем доступным (неудаленным) для пользователя виджетам.*/
    @SerializedName("widgets")
    @Expose
    var widgets: List<Widget>? = null,

    /** <i>Устаревшее поле, следует использовать <see cref="widgets"/></i>. Список типов виджетов для полной загрузки данных.
    Если списки <see cref="initialWidgetTypes"/>, <see cref="incrementalWidgetTypes"/> и <see cref="widgets"/> \e не передаются
    (они \e null, а не \e []), но запрошено получение документов,
    то сервис считает, что нужно грузить \e все документы по \e всем доступным (неудаленным) для пользователя виджетам.
    Когда версия документов пустая <see cref="version"/>.\b docs, этот список взаимозаменяем с <see cref="incrementalWidgetTypes"/>.
    Данный список имеет приоритет над <see cref="incrementalWidgetTypes"/>, т.е. если виджет оказался в обоих списках,
    то по нему будет возвращен полный набор документов.
    При обновлениях в данный список нужно включить только те виджеты, которые при предыдущих обновлениях были выключены.*/
    @SerializedName("initialWidgetTypes")
    @Expose
    var initialWidgetTypes: List<Int>? = null,//List<short>

    /** <i>Устаревшее поле, следует использовать <see cref="widgets"/></i>. Список типов виджетов для инкрементальной загрузки данных на основе сиквенса <see cref="version"/>.\b main.
    В большинстве случаев в запрос на обновление включается только этот список.*/
    @SerializedName("incrementalWidgetTypes")
    @Expose
    var incrementalWidgetTypes: List<Int>? = null//List<short>
) : RequestBase()

/**   RESPONSE */

/** <see cref="API.ISync"/>. Ответ на синхронизацию. Содержит информацию о текущем состоянии БД (сиквенсы).
\b Запрос: <see cref="SyncRequest"/>.*/
class SyncResponse : DocsListResponseBase<SyncResponse>() {
    /** Фильтр данных <see cref="IoModel.DataFilter"/>, использовавшийся в запросе.
    Если поле не заполнено, то возвращаются все данные.*/
    @SerializedName(DATA_FILTER)
    @Expose
    var dataFilter: Int? = null

    /** Все сиквенсы, описывающие версию БД*/
    @SerializedName(VERSION)
    @Expose
    var version: Version? = null

    /** Ветка приложения (branchName из web.config)*/
    @SerializedName("branch")
    @Expose
    var branchName: String? = null

    /** Настройки пользователя*/
    @SerializedName(SETTINGS)
    @Expose
    var settings: List<Setting>? = null

    /** Справочники (название -> {код, \[элементы\]})*/
    @SerializedName(CLASSIFIERS)
    @Expose
    var classifiers: Map<String, Classifier>? = null

    /** Общее кол-во элементов в Глобальном Каталоге (необходимо для реализации прогресс бара).*/
    @SerializedName("globalObjectsCount")
    @Expose
    var globalObjectsCount: Int? = null

    /** Глобальный Каталог (список содержит узлы корневого уровня).
    В случае обновления у корневых элементов новых поддеревьев передаются родительские УИДы.*/
    @SerializedName("globalObjects")
    @Expose
    var globalObjects: List<GlobalObject>? = null

    /** Информация о рабочем месте (рабочие столы и виджеты)*/
    @SerializedName("workbench")
    @Expose
    var workbench: Workbench? = null

    /** Список УИДов документов, которые обновились, но которые не попали ни в один из загруженных виджетов.
    Информацию по таким документам можно получить с помощью сервиса <see cref="API.IDocs.GetDocs"/>.
    Имеет смысл запрашивать только те документы, которые есть на клиенте.
    \b Внимание: Список заполняется только при обновлениях, когда <see cref="SyncRequest"/>.<see cref="DataModel.DbVersion">version</see>.\b main > 0.*/
    @SerializedName("newUnboundDocUids")
    @Expose
    var newUnboundDocUids: List<String>? = null

    /** Плоский список папок вместе с заголовками дочерних файлов (файлы и документы передаются всегда).
    При обновлении содержит только список изменившихся папок, а при удалении приходит соотв. признак объекта.*/
    @SerializedName("folders")
    @Expose
    var folders: List<Folder>? = null

    /** Плоский список категорий и подборок документов с заголовками дочерних файлов (подборки документов для губернатора - они передаются всегда).
    При обновлении содержит только список изменившихся папок, а при удалении приходит соотв. признак объекта.*/
    @SerializedName("compilations")
    @Expose
    var compilations: List<Compilation>? = null
}

/**   ChkState */
class ChkStateRequest : RequestBase() {
    /** Версия данных (сиквенсы) клиента, соответствующая последнему успешному обновлению данных.
    В случае начальной синхронизации можно передать 0 во все сиквенсы или не заполнять этот объект вообще.*/
    @SerializedName("version")
    @Expose
    var clientName: String? = null //DbVersion Version = new DbVersion();
}


/** <see cref="API.ISync"/>. Ответ на поиск документов. <b>Внимание:</b> заполняются только УИД, тип/вид документа, рег. номер, рег. дата и содержание.*/
class ChkStateResponse : ResponseBase<ChkStateResponse>() {

    /** Количество найденных документов (может превосходить кол-во возвращенных документов).*/
    @SerializedName("aes")
    @Expose
    var aes: Boolean = false

    /** Все сиквенсы, описывающие версию БД*/
    @SerializedName("version")
    @Expose
    var version: Version? = null
}
