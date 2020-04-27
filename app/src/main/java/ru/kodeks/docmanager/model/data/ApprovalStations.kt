package ru.kodeks.docmanager.model.data

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.const.SignatureType.NONE
import ru.kodeks.docmanager.db.typeconverter.IntListToStringTypeConverter
import ru.kodeks.docmanager.model.data.ApprovalOperations.NO_OPERATIONS
import ru.kodeks.docmanager.model.data.ApprovalStageTypes.REGULAR
import ru.kodeks.docmanager.model.data.ApprovalStagesStates.UNDEFINED

/** Статусы маршрута согласования.*/
object ApprovalRouteStates {
    /**  Не определено / все*/
    const val UNDEFINED = 0

    /** На согласовании - активен*/
    const val ONAPPROVAL = 1

    /** На согласовании - активен-просрочен*/
    const val OVERDUEONAPPROVAL = 2

    /** Подписан*/
    const val SIGNED = 3

    /** Подписан с задержкой*/
    const val OVERDUE_SIGNED = 4

    /** Возвращено на доработку - направлено на доработку*/
    const val DISAPPROVED = 5

    /** Отменено (снято с согласования)*/
    const val CANCELLED = 6

    /** Согласовано, направлено на подпись*/
    const val APPROVED_NOT_SIGNED = 7

    /** Согласовано с задержкой, направлено на подпись*/
    const val OVERDUE_APPROVED_NOT_SIGNED = 8
}

/**  Статусы этапа согласования.*/
object ApprovalStagesStates {
    /** Не определено*/
    const val UNDEFINED = 0

    /**  Документ по движению поступил. Статус устанавливается при назначении пользователя в движение (новое движение)*/
    const val RECEIVED = 1

    /**  Документ по движению согласован всеми инстанциями*/
    const val APPROVED = 3

    /**  Документ по движению закрыт - согласование завершено.*/
    const val CLOSED = 4

    /**  Документ по движению еще не поступил. Статус устанавливается из "шаблона" маршрута и предлагается пользователю
    как готовое движение с готовым заполненным исполнителем. Движения с данным статусом не должны отображаться в интерфейсе пользователя.*/
    const val NOT_RECEIVED_YET = 5

    /**  Документ по движению направлен на доработку.*/
    const val REJECTED = 6
}

/**  Типы этапа согласования. Используются для особого отображения определнных инстанций.*/
object ApprovalStageTypes {
    /**  Обычная.*/
    const val REGULAR = 0

    /** Автор процесса согласования.*/
    const val AUTHOR = 1

    /** Комиссия.*/
    const val COMMISSION = 2

    /** Председатель комиссии.*/
    const val CHAIRMAN = 3

    /** Регистрация. <i>В настоящий момент в маршрут не входит.</i>*/
    const val REGISTRATION = 4

    /** Руководитель структурного подразделения автора.*/
    const val AUTHOR_CHIEF = 5

    /** <i>Не используется.</i>*/
    const val OTHER = 6

    /** Подпись.*/
    const val SIGNER = 7

    /** Начальник управления.*/
    const val DEPARTMENT_CHIEF = 8

    /** Участник подэтапа (подмаршрута).*/
    const val FORWARDED = 9

    /** Возврат на доработку.*/
    const val RETURNED = 10
}

/** Типы результатов согласования.*/
object ApprovalResults {
    /**  Не определено (согласование ещё не выполнялось)*/
    const val UNDEFINED = 0

    /**  Запущен маршрут согласования*/
    const val APPROVAL_STARTED = 1

    /** Согласовано*/
    const val APPROVED = 2

    /** Согласовано с замечаниями*/
    const val APPROVEDWITHREMARKS = 3

    /** Не согласовано*/
    const val NOT_APPROVED = 4

    /** Направить на доработку*/
    const val TO_BE_RETURNED = 5

    /** Согласовано - особое мнение*/
    const val DISSENTING_OPINION = 6

    /** Перенаправлено на согласование*/
    const val FORWARDED = 7

    /** Подписано*/
    const val SIGNED = 8
}

/**  Операции согласования, предназначены для использования в битовой маске. Используется при серверном вычислении
доступных операций согласования в инстанциях согласования, см. <see cref="ApprovalStation"/>.*/
object ApprovalOperations {

    const val NO_OPERATIONS = 0

    /** Отправить на согласование*/
    const val START = 1

    /** Повторное согласование*/
    const val RESTART = 2

    /** Редактировать маршрут согласования*/
    const val EDIT = 4

    /** Согласовать документ*/
    const val APPROVE = 8

    /** Подписать документ*/
    const val SIGN = 16

    /** Перенаправить документ*/
    const val REDIRECT = 32

    /** Вернуть документ на доработку*/
    const val RETURN_TO_AUTHOR = 64

    /** Просмотреть визу*/
    const val VIEW_VISA = 128

    /** Редактировать визу*/
    const val EDIT_VISA = 256

    /** Снять с согласования*/
    const val CANCEL_APPROVMENT = 512

    /** Удалить подмаршрут*/
    const val DELETE_FORWARDING = 1024
}

/** Базовый объект для сущностей процесса согласования.*/
open class ApprovalObjectBase(
    /**  Идентификатор.*/
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    /**  Состояние модификации, см. <see cref="ModificationStates"/>.*/
    @SerializedName("modificationState")
    @Expose
    @ColumnInfo(name = "modification_state")
    var modificationState: Int? = null,
    /** Название.*/
    @SerializedName("name")
    @Expose
    var name: String? = null,
    /** Идентификатор последнего модифицировавшего пользователя (в маршрутах не передается).(Guid)*/
    @SerializedName("userUid")
    @Expose
    @ColumnInfo(name = "user_uid")
    var userUid: String? = null,
    /** Дата создания.(DateTime)*/
    @SerializedName("created")
    @Expose
    var created: String? = null,
    /** Дата последнего изменения.(DateTime)*/
    @SerializedName("modified")
    @Expose
    var modified: String? = null
) : ObjectBase()

/** Маршрут согласования.*/
@Entity(
    tableName = "approval_routes",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(
        entity = Document::class,
        parentColumns = ["uid"],
        childColumns = ["document_uid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["document_uid"])]
)
data class ApprovalRoute(
    @ColumnInfo(name = "document_uid")
    var docUid: String = "",
    /** Состояние маршрута, см. <see cref="ApprovalRouteStates"/>.(Byte)*/
    @SerializedName("state")
    @Expose
    var state: Int = ApprovalRouteStates.UNDEFINED,
    /** Описание маршрута.*/
    @SerializedName("description")
    @Expose
    var description: String? = null,
    /** Признак того, что документ направлен для сведения.*/
    @SerializedName("isInfo")
    @Expose
    @ColumnInfo(name = "is_informational")
    var isInfo: Boolean = false,
    /** Контрольная длительность в минутах.*/
    @SerializedName("controlDuration")
    @Expose
    @ColumnInfo(name = "control_duration")
    var controlDuration: Int? = null,
    /** Признак "Учитывать только рабочие дни".*/
    @SerializedName("workdaysOnly")
    @Expose
    @ColumnInfo(name = "work_days_only")
    var workdaysOnly: Boolean = false,
    /** Контрольная дата окончания.(DateTime)*/
    @SerializedName("controlDate")
    @Expose
    @ColumnInfo(name = "control_date")
    var controlDate: String? = null,
    /** Дата начала.(DateTime)*/
    @SerializedName("startDate")
    @Expose
    @ColumnInfo(name = "start_date")
    var startDate: String? = null,
    /** Дата окончания.(DateTime)*/
    @SerializedName("completeDate")
    @Expose
    @ColumnInfo(name = "complete_date")
    var completeDate: String? = null,
    /** Идентификатор документа, закрывшего маршрут.(Guid)*/
    @SerializedName("closingDocUid")
    @Expose
    @ColumnInfo(name = "closing_doc_uid")
    var closingDocUid: String? = null,
    /** УИД исполнителя по документу.(Guid)*/
    @SerializedName("executiveUid")
    @Expose
    @ColumnInfo(name = "executive_uid")
    var executiveUid: String? = null,
    /** Подписант по документу.(Guid)*/
    @SerializedName("signerUid")
    @Expose
    @ColumnInfo(name = "signer_uid")
    var signerUid: String? = null,
    /** Документы, относящиеся к маршруту согласования. Эти документы могут не содержать
    никаких ссылок на виджеты и могут быть пропущены в обновлении.*/
    @SerializedName("docLinks")
    @Expose
    @Ignore
    var docLinks: List<DocumentLink>? = null,
    /** Файлы, привязанные к маршруту.*/
    @SerializedName("files")
    @Expose
    @Ignore
    var files: List<FileAttachment>? = null,
    /** Коллекция всех этапов согласования.*/
    @SerializedName("stages")
    @Expose
    @Ignore
    var stages: List<ApprovalStage>? = null,
    /** Признак отправки на сервис согласования*/
    @SerializedName("sentToCAS")
    @Expose
    @ColumnInfo(name = "sent_to_cas")
    var sentToCAS: Boolean = false
) : ApprovalObjectBase()

/** Базовый класс для этапов и инстанций согласования*/
open class ApprovalChildObject(
    /** Порядок следования внутри родителя.*/
    @SerializedName("order")
    @Expose
    var order: Int? = null,
    /** Кто отправил документ на согласование.(Guid)*/
    @SerializedName("authorUid")
    @Expose
    @ColumnInfo(name = "author_uid")
    var authorUid: String? = null,
    /** Контрольная дата.(DateTime)*/
    @SerializedName("controlDate")
    @Expose
    @ColumnInfo(name = "control_date")
    var controlDate: String? = null,
    /** Дата передачи.(DateTime)*/
    @SerializedName("sendingDate")
    @Expose
    var sendingDate: String? = null,
    /** Дата отработки документа, когда пользователь дал следующее движение.()DateTime*/
    @SerializedName("workOffDate")
    @Expose
    @ColumnInfo(name = "work_off_date")
    var workOffDate: String? = null,
    /** Дата закрытия этапа/инстанции. При закрытии инстанция переводится в статус "документ по движению закрыт"()DateTime.*/
    @SerializedName("completeDate")
    @Expose
    @ColumnInfo(name = "complete_date")
    var completeDate: String? = null,
    /** Признак возможности редактирования.*/
    @SerializedName("isEditable")
    @Expose
    @ColumnInfo(name = "is_editable")
    var isEditable: Boolean = false
) : ApprovalObjectBase()

/** Этап маршрута согласования.*/
@Entity(
    tableName = "approval_stages",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(
        entity = ApprovalRoute::class,
        parentColumns = ["id"],
        childColumns = ["route_id"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["route_id"])]
)
@TypeConverters(IntListToStringTypeConverter::class)
data class ApprovalStage(
    @ColumnInfo(name = "route_id")
    var routeId: Int = 0,
    @ColumnInfo(name = "document_uid")
    var docUid: String = "",
    /** Состояние этапа, см. <see cref="ApprovalStagesStates"/>.(Byte)*/
    @SerializedName("state")
    @Expose
    var state: Int = UNDEFINED,
    /** Тип этапа, см. <see cref="ApprovalStageTypes"/>.(Byte)*/
    @SerializedName("stageType")
    @Expose
    var stageType: Int = REGULAR,
    /** Кто направил документ на этап согласования.(Guid)*/
    @SerializedName("senderUid")
    @Expose
    var senderUid: String? = null,
    /** Контрольная длительность в минутах.*/
    @SerializedName("controlDuration")
    @Expose
    var controlDuration: Int? = null,
    /** Подразделение, из которого формируется этап.(Guid)*/
    @SerializedName("unitUid")
    @Expose
    var unitUid: String? = null,
    /** Флаг невозможности удаления этапа из маршрута или перемещения этапа.*/
    @SerializedName("isFixed")
    @Expose
    var isFixed: Boolean = false,
    /** Дочерние этапы.*/
    @SerializedName("children")
    @Expose
    var children: List<Int>? = null,
    /** Родительские этапы.*/
    @SerializedName("parents")
    @Expose
    var parents: List<Int>? = null,
    /** Инстанции согласования.*/
    @SerializedName("stations")
    @Expose
    @Ignore
    var stations: List<ApprovalStation>? = null,
    /** Требует ЭП*/
    @SerializedName("isDigitalSignRequired")
    @Expose
    @ColumnInfo(name = "is_digital_sign_required")
    var isDigitalSignRequired: Boolean = false
) : ApprovalChildObject()

/** Инстанция маршрута согласования.*/
@Entity(
    tableName = "approval_stations",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(
        entity = ApprovalStage::class,
        parentColumns = ["id"],
        childColumns = ["stage_id"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["stage_id"])]
)
data class ApprovalStation(
    @ColumnInfo(name = "document_uid")
    var docUid: String = "",
    @ColumnInfo(name = "stage_id")
    var stageId: Int = 0,
    /** Статус инстанции, см. <see cref="StationStates"/>.(Byte)*/
    @SerializedName("state")
    @Expose
    var state: Int? = null,
    /** Описание инстанции.*/
    @SerializedName("description")
    @Expose
    var description: String? = null,
    /** Исполнитель/получатель инстанции.(Guid)*/
    @SerializedName("receiverUid")
    @Expose
    @ColumnInfo(name = "receiver_uid")
    var receiverUid: String? = null,
//    /** Исполнитель/получатель инстанции (<b>устаревшее поле</b>).*/
//    @SerializedName("receiverUID")
//    @Expose
//    var receiverUidOld:String? = null//Guid*/
    /** Получатель резолюции в справочнике корреспондентов (организаций).(Guid)*/
    @SerializedName("externalReceiverUid")
    @Expose
    @ColumnInfo(name = "external_receiver_uid")
    var externalReceiverUid: String? = null,
    /** Оператор, который фактически поменял состояние инстанции.(Guid)*/
    @SerializedName("resolutionAuthorUid")
    @Expose
    @ColumnInfo(name = "resolution_author_uid")
    var resolutionAuthorUid: String? = null,
    /** Резолюция или результат согласования.*/
    @SerializedName("resolution")
    @Expose
    var resolution: String? = null,
    /** Результат согласования, см. <see cref="ApprovalResults"/>.(Byte)*/
    @SerializedName("approvalResult")
    @Expose
    @ColumnInfo(name = "approval_result")
    var approvalResult: Int? = null,
    /** Дата просмотра.(DateTime)*/
    @SerializedName("viewDate")
    @Expose
    @ColumnInfo(name = "view_date")
    var viewDate: String? = null,
    /** Главный ответственный исполнитель - должен отображаться жирным шрифтом.*/
    @SerializedName("isMain")
    @Expose
    @ColumnInfo(name = "is_main")
    var isMain: Boolean = false,
    /** Признак того, что инстанция - для информирования*/
    @SerializedName("isInfo")
    @Expose
    @ColumnInfo(name = "is_informational")
    var isInfo: Boolean = false,
    /** Тип ЭП (0 - нет, 1 - простая, 2 - квалицифированная)(Byte)*/
    @SerializedName("signatureType")
    @Expose
    @ColumnInfo(name = "signature_type")
    var signatureType: Int = NONE,
    /** Доступные операции согласования для текущего пользователя с учетом замещений.
    Битовая маска, см. <see cref="ApprovalOperations"/>.*/
    @SerializedName("operations")
    @Expose
    var availableOperations: Int = NO_OPERATIONS,
    /** Расшифрованная отладочная версия <see cref="operations"/>.*/
    @SerializedName("operationsDebug")
    @Expose
    @Ignore
    var availableOperationsDebug: List<Int>? = null,
    /** Файлы, привязанные к инстанции (пока там будет не более 1 файла).*/
    @SerializedName("files")
    @Expose
    @Ignore
    var files: List<FileAttachment>? = null
) : ApprovalChildObject()
