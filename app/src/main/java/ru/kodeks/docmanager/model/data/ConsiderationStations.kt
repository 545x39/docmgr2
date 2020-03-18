package ru.kodeks.docmanager.model.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/** Статус (состояние) маршрута рассмотрения */
object ConsiderationRouteStates {
    /** Маршут инвалидный. Статус устанавливается в случае сбоя.*/
    const val INVALID = 0

    /** Маршрут активный. Нормальное состояние маршрута без просрочек.*/
    const val ACTIVE = 1

    /** Маршрут активный, просроченный. Устанавливается в случае просрочки какой либо инстанцией выполнения результата.*/
    const val ACTIVE_EXPIRED = 2

    /** Маршрут неактивный (приостановленный, неактивный подмаршрут).*/
    const val INACTIVE = 3

    /** Маршрут неактивный (приостановленный), просроченный.*/
    const val INACTIVEE_XPIRED = 4

    /** Маршрут завершен.*/
    const val COMPLETED = 5

    /** Маршрут завершен с просроченными инстанциями.*/
    const val COMPLETED_EXPIRED = 6

    /** Маршрут инициализирован, но не загружен. Статус устанавливается в конструкторе до загрузки.*/
    const val INITIALIZED = 100
}

/** Типы срочности рассмотрения документа*/
object UrgencyTypes {
    /** Неопределено / несрочно*/
    const val UNDEFINED = 0

    /** Весьма срочно*/
    const val VERY_URGENT = 1

    /** Срочно*/
    const val URGENT = 2
}

/** Тип резолюции.*/
object ResolutionTypes {
    /** Резолюция*/
    const val RESOLUTION = 1

    /** Отчет по резолюции*/
    const val REPORT = 2
}

/** Операции рассмотрения, предназначены для использования в битовой маске. Используется при серверном вычислении доступных операций рассмотрения в инстанциях рассмотрения, см. <see cref="ConsiderationStation"/>.
<b>Qx</b> - номер нибла/тетрады - последовательности из 4х байт, т.е. int32 состоит из 8 ниблов.*/
/** Flags]*/
object ConsiderationOptions {
    /** Действия с резолюцией*/

    /**  РЕЗОЛЮЦИЯ: Можно создать*/
    const val CAN_CREATE_RESOLUTION = 1

    /**  <b>РЕЗОЛЮЦИЯ: Можно отредактировать*/
    const val CAN_EDIT_RESOLUTION = 2

    /**  РЕЗОЛЮЦИЯ: Можно просмотреть*/
    const val CAN_VIEW_RESOLUTION = 4

    /** РЕЗОЛЮЦИЯ: Действие заблокировано до выполнения определенных условий*/
    const val RESOLUTION_WAITING = 8

    /** Действия со своим отчетом*/
    /**  ОТЧЕТ: Можно создать*/
    const val CAN_CREATE_REPORT = 16

    /** ОТЧЕТ: Можно отредактировать*/
    const val CAN_EDIT_REPORT = 32

    /** ОТЧЕТ: Можно просмотреть*/
    const val CAN_VIEW_REPORT = 64

    /** ОТЧЕТ: Действие заблокировано до выполнения определенных условий*/
    const val REPORT_WAITING = 128

    /** Действия с входящим отчетом*/
    /** 0x100000 ВХОДЯЩИЙ ОТЧЕТ: Можно отклонить входящий отчет*/
    const val CAN_REJECT_REPORT = 1048576

    /** 0x200000 ВХОДЯЩИЙ ОТЧЕТ: Можно принять входящий отчет и отменить резолюцию (она пропадет из списка движений)*/
    const val CAN_ACCEPT_AND_CANCEL_REPORT = 2097152

    /** 0x400000 ВХОДЯЩИЙ ОТЧЕТ: Можно принять входящий отчет и закрыть резолюцию (она будет отображаться в движениях как отработанная)*/
    const val CAN_ACCEPT_AND_CLOSE_REPORT = 4194304

    /**  Флаги из класса ResolutionIn*/
    /** Можно ли закрыть маршрут*/
    const val CAN_BE_CLOSED = 256

    /**  Определяет, получена ли резолюция/отчет по замещению*/
    const val IS_SUBSTITUTION = 512

    /** Определяет, есть ли проект резолюции на след. инстанцию*/
    const val HAS_PROJECT = 1024

    /** Определяет, имеется ли фотография подписавшего резолюцию*/
    const val HAS_PHOTO = 2048

    /** Можно ли переносить (изменять) срок по документу*/
    const val CAN_CHANGE_CONTROL_DATE = 4096

    /** Признак «Личный контроль»*/
    const val IS_PERSONAL_CONTROL = 8192

    /** Признак «Важно»*/
    const val IS_IMPORTANT = 16384

    /** Признак «Очень важно»*/
    const val IS_VERY_IMPORTANT = 32768

    /** Проекты резолюций, попадающие к пользователю по полю SignerUID*/
    /**  0x10000 ПРОЕКТ РЕЗОЛЮЦИИ: Подписать/утвердить (передать на исполнение - статус 1)*/
    const val APPROVE_RESOLUTION_PROJECT = 65536

    /** 0x20000 ПРОЕКТ РЕЗОЛЮЦИИ: Отклонить (проставить ApprovalResultID = 4)*/
    const val REJECT_RESOLUTION_PROJECT = 131072

    /** 0x40000 ПРОЕКТ РЕЗОЛЮЦИИ: Согласовать (перевести на утверждение шефу, т.е. поменять ResolutionAuthorUid)*/
    const val PROMOTE_RESOLUTION_PROJECT = 262144
}

/**  Инстанция рассмотрения документа (элементарная единица движения в маршруте рассмотрения).*/
@Entity(
    tableName = "consideration_stations",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(
        entity = Document::class,
        parentColumns = ["uid"],
        childColumns = ["doc_uid"],
        onUpdate = CASCADE,
        onDelete = CASCADE
    )],
    indices = [Index(value = ["doc_uid"])]
)
data class ConsiderationStation(
    /** Идентификатор инстанции*/
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    /** Идентификатор родительской инстанции*/
    @SerializedName("parentId")
    @Expose
    @ColumnInfo(name = "parent_id")
    var parentStationId: Int? = null,
    /** UID документа */
    @ColumnInfo(name = "doc_uid")
    var docUid: String? = null,
    /** Порядок следования (Short)*/
    @SerializedName("order")
    @Expose
    var stationOrder: Int? = null,
    /** Группа резолюции (нумерация начинается с 1 и номера уникальны в пределах родительской входящей резолюции) (Byte)*/
    @SerializedName("group")
    @Expose
    var stationGroup: Int? = null,
    /** Название инстанции*/
    @SerializedName("name")
    @Expose
    var name: String? = null,
    /** Описание инстанции*/
    @SerializedName("description")
    @Expose
    var description: String? = null,
    /** Идентификатор исполнителя (Guid)*/
    @SerializedName("receiverUid")
    @Expose
    @ColumnInfo(name = "receiver_uid")
    var receiverUid: String? = null,
    /** Текст резолюции*/
    @SerializedName("resolution")
    @Expose
    var resolution: String? = null,
    /** Тип резолюции, см. "ResolutionTypes"*/
    @SerializedName("resolutionType")
    @Expose
    @ColumnInfo(name = "resolution_type")
    var resolutionType: Int? = null,
    /** Идентификатор автора резолюции*/
    @SerializedName("resolutionAuthorUid")
    @Expose
    @ColumnInfo(name = "resolution_author_uid")
    var resolutionAuthorUid: String? = null,
    /** Контрольный срок (в минутах)*/
    @SerializedName("controlDuration")
    @Expose
    @ColumnInfo(name = "control_duration")
    var controlDuration: Int? = null,
    /** Контрольная дата исполнения.*/
    @SerializedName("controlDate")
    @ColumnInfo(name = "control_date")
    @Expose
    var controlDate: String? = null,
    /** Дата передачи. (DateTime)*/
    @SerializedName("sendingDate")
    @Expose
    @ColumnInfo(name = "sending_date")
    var sendingDate: String? = null,
    /** Дата просмотра.*/
    @SerializedName("viewDate")
    @Expose
    @ColumnInfo(name = "view_date")
    var viewDate: String? = null,
    /** Дата исполнения (дата отработки документа).*/
    @SerializedName("workOffDate")
    @Expose
    @ColumnInfo(name = "work_off_date")
    var workOffDate: String? = null,
    /** Дата закрытия резолюции.*/
    @SerializedName("completeDate")
    @Expose
    @ColumnInfo(name = "complete_date")
    var completeDate: String? = null,
    /** Статусы инстанции, см. "StationStates"*/
    @SerializedName("state")
    @Expose
    var state: Int? = null,
    /** Признак Контролирующая инстанция*/
    @SerializedName("isController")
    @Expose
    @ColumnInfo(name = "is_controller")
    var isController: Boolean? = null,
    /** Признак Инстанция – подразделение*/
    @SerializedName("isSubdivision")
    @Expose
    @ColumnInfo(name = "is_subdivision")
    var isSubdivision: Boolean? = null,
    /** Определяет, запрещено ли удалять инстанцию*/
    @SerializedName("isFixed")
    @Expose
    @ColumnInfo(name = "is_fixed")
    var isFixed: Boolean = false,
    /** Признак Главный ответственный исполнитель*/
    @SerializedName("isMain")
    @Expose
    @ColumnInfo(name = "is_main")
    var isMain: Boolean = false,
    /** Признак "Для информирования*/
    @SerializedName("isInformational")
    @Expose
    @ColumnInfo(name = "is_informational")
    var isInformational: Boolean = false,
    /** Идентификатор результата согласования*/
    @SerializedName("approvalResultId")
    @Expose
    @ColumnInfo(name = "approval_result_id")
    var approvalResultId: Int? = null,
    /** Конечная инстанция*/
    @SerializedName("isFinal")
    @Expose
    @ColumnInfo(name = "is_final")
    var isFinal: Boolean = false,
    /** Идентификатор автора.*/
    @SerializedName("authorUid")
    @Expose
    @ColumnInfo(name = "author_uid")
    var authorUid: String? = null,
    /** Идентификатор оператора, производившего модификацию.*/
    @SerializedName("userUid")
    @Expose
    @ColumnInfo(name = "user_uid")
    var userUid: String? = null,
    /** Дата создания инстанции.*/
    @SerializedName("created")
    @Expose
    var created: String? = null,
    /** Дата последней модификации инстанции.*/
    @SerializedName("modified")
    @Expose
    var modified: String? = null,
    /** Идентификатор видеоролика резолюции*/
    @SerializedName("clipId")
    @Expose
    @ColumnInfo(name = "clip_id")
    @Ignore
    var clipId: Int? = null,
    /** Признак - Требует ответа*/
    @SerializedName("isAnswerRequired")
    @Expose
    @ColumnInfo(name = "is_answer_required")
    var isAnswerRequired: Boolean = false,
    /** Доступные операции и опции рассмотрения для текущего пользователя с учетом замещений. Битовая маска, см. "ConsiderationOptions".
    Возвращается только для входящих инстанций, относящихся к текущему пользователю.*/
    @SerializedName("resolutionInfo")
    @Expose
    @ColumnInfo(name = "resolution_info")
    var resolutionInfo: Int = 0,
    /** Расшифрованная отладочная версия <see cref="resolutionInfo"/>.*/
    @SerializedName("resolutionInfoDebug")
    @Expose
    @Ignore
    var resolutionInfoDebug: List<Int>? = null,
    /** Тип ЭП (0 - нет, 1 - простая, 2 - квалицифированная)*/
    @SerializedName("signatureType")
    @Expose
    @ColumnInfo(name = "signature_type")
    var signatureType: Int? = null,
    /** Документы, относящиеся к инстанции. Эти документы могут не содержать никаких ссылок на виджеты и могут быть пропущены в обновлении.*/
    @SerializedName("docLinks")
    @Expose
    @Ignore
    var docLinks: List<DocumentLink>? = null,
    /** Файлы, привязанные к инстанции (пока там будет не более 1 файла).*/
    @SerializedName("files")
    @Expose
    @Ignore
    var files: List<FileAttachment>? = null
) : ObjectBase()
