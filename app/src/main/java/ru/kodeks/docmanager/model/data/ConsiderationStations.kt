package ru.kodeks.docmanager.model.data

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/** Статус (состояние) маршрута рассмотрения */
sealed class ConsiderationRouteStates() {
    /** Маршут инвалидный. Статус устанавливается в случае сбоя.*/
    val INVALID = 0
    /** Маршрут активный. Нормальное состояние маршрута без просрочек.*/
    val ACTIVE = 1
    /** Маршрут активный, просроченный. Устанавливается в случае просрочки какой либо инстанцией выполнения результата.*/
    val ACTIVE_EXPIRED = 2
    /** Маршрут неактивный (приостановленный, неактивный подмаршрут).*/
    val INACTIVE = 3
    /** Маршрут неактивный (приостановленный), просроченный.*/
    val INACTIVEE_XPIRED = 4
    /** Маршрут завершен.*/
    val COMPLETED = 5
    /** Маршрут завершен с просроченными инстанциями.*/
    val COMPLETED_EXPIRED = 6
    /** Маршрут инициализирован, но не загружен. Статус устанавливается в конструкторе до загрузки.*/
    val INITIALIZED = 100
}

/** Типы срочности рассмотрения документа*/
sealed class UrgencyTypes {
    /** Неопределено / несрочно*/
    val UNDEFINED = 0
    /** Весьма срочно*/
    val VERY_URGENT = 1
    /** Срочно*/
    val URGENT = 2
}

/** Тип резолюции.*/
sealed class ResolutionTypes {
    /** Резолюция*/
    val RESOLUTION = 1
    /** Отчет по резолюции*/
    val REPORT = 2
}

/** Операции рассмотрения, предназначены для использования в битовой маске. Используется при серверном вычислении доступных операций рассмотрения в инстанциях рассмотрения, см. <see cref="ConsiderationStation"/>.
<b>Qx</b> - номер нибла/тетрады - последовательности из 4х байт, т.е. int32 состоит из 8 ниблов.*/
/** Flags]*/
sealed class ConsiderationOptions {
    /** Действия с резолюцией*/

    /**  <b>[Q1] РЕЗОЛЮЦИЯ</b>: Можно создать*/
    val CAN_CREATE_RESOLUTION = 1

    /**  <b>[Q1] РЕЗОЛЮЦИЯ</b>: Можно отредактировать*/
    val CAN_EDIT_RESOLUTION = 2

    /**  <b>[Q1] РЕЗОЛЮЦИЯ</b>: Можно просмотреть*/
    val CAN_VIEW_RESOLUTION = 4

    /** <b>[Q1] РЕЗОЛЮЦИЯ</b>: Действие заблокировано до выполнения определенных условий*/
    val RESOLUTION_WAITING = 8

    /** Действия со своим отчетом*/
    /**  <b>[Q2] ОТЧЕТ</b>: Можно создать*/
    val CAN_CREATE_REPORT = 16

    /** <b>[Q2] ОТЧЕТ</b>: Можно отредактировать*/
    val CAN_EDIT_REPORT = 32

    /** <b>[Q2] ОТЧЕТ</b>: Можно просмотреть*/
    val CAN_VIEW_REPORT = 64

    /** <b>[Q2] ОТЧЕТ</b>: Действие заблокировано до выполнения определенных условий*/
    val REPORT_WAITING = 128

    /** Действия с входящим отчетом*/
    /** <b>[Q6] 0x100000 ВХОДЯЩИЙ ОТЧЕТ</b>: Можно отклонить входящий отчет*/
    val CAN_REJECT_REPORT = 1048576

    /** <b>[Q6] 0x200000 ВХОДЯЩИЙ ОТЧЕТ</b>: Можно принять входящий отчет и отменить резолюцию (она пропадет из списка движений)*/
    val CAN_ACCEPT_AND_CANCEL_REPORT = 2097152

    /** <b>[Q6] 0x400000 ВХОДЯЩИЙ ОТЧЕТ</b>: Можно принять входящий отчет и закрыть резолюцию (она будет отображаться в движениях как отработанная)*/
    val CAN_ACCEPT_AND_CLOSE_REPORT = 4194304

    /**  Флаги из класса ResolutionIn*/
    /**  <b>[Q3]</b> Можно ли закрыть маршрут*/
    val CAN_BE_CLOSED = 256

    /**  <b>[Q3]</b> Определяет, получена ли резолюция/отчет по замещению*/
    val IS_SUBSTITUTION = 512

    /** <b>[Q3]</b> Определяет, есть ли проект резолюции на след. инстанцию*/
    val HAS_PROJECT = 1024

    /** <b>[Q3]</b> Определяет, имеется ли фотография подписавшего резолюцию*/
    val HAS_PHOTO = 2048

    /** <b>[Q4]</b> Можно ли переносить (изменять) срок по документу*/
    val CAN_CHANGE_CONTROL_DATE = 4096

    /** <b>[Q4]</b> Признак «Личный контроль»*/
    val IS_PERSONAL_CONTROL = 8192

    /** <b>[Q4]</b> Признак «Важно»: <i>ImportanceType == ImportanceTypesEnum.Important</i>*/
    val IS_IMPORTANT = 16384

    /** <b>[Q4]</b> Признак «Очень важно»: <i>ImportanceType == ImportanceTypesEnum.VeryImportant</i>*/
    val IS_VERY_IMPORTANT = 32768


    /** Проекты резолюций, попадающие к пользователю по полю SignerUID*/
    /**  <b>[Q5] 0x10000 ПРОЕКТ РЕЗОЛЮЦИИ</b>: Подписать/утвердить (передать на исполнение - статус 1)*/
    val APPROVE_RESOLUTION_PROJECT = 65536

    /** <b>[Q5] 0x20000 ПРОЕКТ РЕЗОЛЮЦИИ</b>: Отклонить (проставить ApprovalResultID = 4)*/
    val REJECT_RESOLUTION_PROJECT = 131072

    /** <b>[Q5] 0x40000 ПРОЕКТ РЕЗОЛЮЦИИ</b>: Согласовать (перевести на утверждение шефу, т.е. поменять ResolutionAuthorUid)*/
    val PROMOTE_RESOLUTION_PROJECT = 262144
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
    /** Тип резолюции, см. <see cref="ResolutionTypes"/>(Byte)*/
    @SerializedName("resolutionType")
    @Expose
    @ColumnInfo(name = "resolution_type")
    var resolutionType: Int? = null,
    /** Идентификатор автора резолюции/ (Guid)*/
    @SerializedName("resolutionAuthorUid")
    @Expose
    @ColumnInfo(name = "resolution_author_uid")
    var resolutionAuthorUid: String? = null,
    /** Контрольный срок (в минутах)*/
    @SerializedName("controlDuration")
    @Expose
    @ColumnInfo(name = "control_duration")
    var controlDuration: Int? = null,
    /** Контрольная дата исполнения.(DateTime)*/
    @SerializedName("controlDate")
    @ColumnInfo(name = "control_date")
    @Expose
    var controlDate: String? = null,
    /** Дата передачи. (DateTime)*/
    @SerializedName("sendingDate")
    @Expose
    @ColumnInfo(name = "sending_date")
    var sendingDate: String? = null,
    /** Дата просмотра.(DateTime)*/
    @SerializedName("viewDate")
    @Expose
    @ColumnInfo(name = "view_date")
    var viewDate: String? = null,
    /** Дата исполнения (дата отработки документа).(DateTime)*/
    @SerializedName("workOffDate")
    @Expose
    @ColumnInfo(name = "work_off_date")
    var workOffDate: String? = null,
    /** Дата закрытия резолюции.(DateTime)*/
    @SerializedName("completeDate")
    @Expose
    @ColumnInfo(name = "complete_date")
    var completeDate: String? = null,
    /** Статусы инстанции, см. <see cref="StationStates"/>(Byte)*/
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
    /** Идентификатор автора. (Guid)*/
    @SerializedName("authorUid")
    @Expose
    @ColumnInfo(name = "author_uid")
    var authorUid: String? = null,
    /** Идентификатор оператора, производившего модификацию.(Guid)*/
    @SerializedName("userUid")
    @Expose
    @ColumnInfo(name = "user_uid")
    var userUid: String? = null,
    /** Дата создания инстанции.(DateTime)*/
    @SerializedName("created")
    @Expose
    var created: String? = null,
    /** Дата последней модификации инстанции.(DateTime)*/
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
    /** Доступные операции и опции рассмотрения для текущего пользователя с учетом замещений. Битовая маска, см. <see cref="ConsiderationOptions"/>.
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
