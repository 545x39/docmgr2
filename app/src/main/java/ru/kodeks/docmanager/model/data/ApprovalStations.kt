package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Статусы маршрута согласования.*/
sealed class ApprovalRouteStates {
    /**  Не определено / все*/
    val UNDEFINED = 0

    /** На согласовании - активен*/
    val ONAPPROVAL = 1

    /** На согласовании - активен-просрочен*/
    val OVERDUEONAPPROVAL = 2

    /** Подписан*/
    val SIGNED = 3

    /** Подписан с задержкой*/
    val OVERDUE_SIGNED = 4

    /** Возвращено на доработку - направлено на доработку*/
    val DISAPPROVED = 5

    /** Отменено (снято с согласования)*/
    val CANCELLED = 6

    /** Согласовано, направлено на подпись*/
    val APPROVED_NOT_SIGNED = 7

    /** Согласовано с задержкой, направлено на подпись*/
    val OVERDUE_APPROVED_NOT_SIGNED = 8
}

/**  Статусы этапа согласования.*/
sealed class ApprovalStagesStates {
    /** Не определено*/
    val UNDEFINED = 0

    /**  Документ по движению поступил. Статус устанавливается при назначении пользователя в движение (новое движение)*/
    val RECEIVED = 1

    /**  Документ по движению согласован всеми инстанциями*/
    val APPROVED = 3

    /**  Документ по движению закрыт - согласование завершено.*/
    val CLOSED = 4

    /**  Документ по движению еще не поступил. Статус устанавливается из "шаблона" маршрута и предлагается пользователю
    как готовое движение с готовым заполненным исполнителем. Движения с данным статусом не должны отображаться в интерфейсе пользователя.*/
    val NOT_RECEIVED_YET = 5

    /**  Документ по движению направлен на доработку.*/
    val REJECTED = 6
}

/**  Типы этапа согласования. Используются для особого отображения определнных инстанций.*/
sealed class ApprovalStageTypes {

    /**  Обычная.*/
    val REGULAR = 0

    /** Автор процесса согласования.*/
    val AUTHOR = 1

    /** Комиссия.*/
    val COMMISSION = 2

    /** Председатель комиссии.*/
    val CHAIRMAN = 3

    /** Регистрация. <i>В настоящий момент в маршрут не входит.</i>*/
    val REGISTRATION = 4

    /** Руководитель структурного подразделения автора.*/
    val AUTHOR_CHIEF = 5

    /** <i>Не используется.</i>*/
    val other = 6

    /** Подпись.*/
    val SIGNER = 7

    /** Начальник управления.*/
    val DEPARTMENT_CHIEF = 8

    /** Участник подэтапа (подмаршрута).*/
    val FORWARDED = 9

    /** Возврат на доработку.*/
    val RETURNED = 10
}

/** Типы результатов согласования.*/
sealed class ApprovalResults {

    /**  Не определено (согласование ещё не выполнялось)*/
    val UNDEFINED = 0

    /**  Запущен маршрут согласования*/
    val APPROVAL_STARTED = 1

    /** Согласовано*/
    val APPROVED = 2

    /** Согласовано с замечаниями*/
    val APPROVEDWITHREMARKS = 3

    /** Не согласовано*/
    val NOT_APPROVED = 4

    /** Направить на доработку*/
    val TO_BE_RETURNED = 5

    /** Согласовано - особое мнение*/
    val DISSENTING_OPINION = 6

    /** Перенаправлено на согласование*/
    val FORWARDED = 7

    /** Подписано*/
    val SIGNED = 8
}

/**  Операции согласования, предназначены для использования в битовой маске. Используется при серверном вычислении
доступных операций согласования в инстанциях согласования, см. <see cref="ApprovalStation"/>.*/
sealed class ApprovalOperations {

    /** Отправить на согласование*/
    val START = 1

    /** Повторное согласование*/
    val RESTART = 2

    /** Редактировать маршрут согласования*/
    val EDIT = 4

    /** Согласовать документ*/
    val APPROVE = 8

    /** Подписать документ*/
    val SIGN = 16

    /** Перенаправить документ*/
    val REDIRECT = 32

    /** Вернуть документ на доработку*/
    val RETURN_TO_AUTHOR = 64

    /** Просмотреть визу*/
    val VIEW_VISA = 128

    /** Редактировать визу*/
    val EDIT_VISA = 256

    /** Снять с согласования*/
    val CANCEL_APPROVMENT = 512

    /** Удалить подмаршрут*/
    val DELETE_FORWARDING = 1024
}

/** Базовый объект для сущностей процесса согласования.*/
open class ApprovalObjectBase(
        /**  Идентификатор.*/
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        /**  Состояние модификации, см. <see cref="ModificationStates"/>.*/
        @SerializedName("modificationState")
        @Expose
        var modificationState: Int? = null,
        /** Название.*/
        @SerializedName("name")
        @Expose
        var name: String? = null,
        /** Идентификатор последнего модифицировавшего пользователя (в маршрутах не передается).(Guid)*/
        @SerializedName("userUid")
        @Expose
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
data class ApprovalRoute(
    /** Состояние маршрута, см. <see cref="ApprovalRouteStates"/>.(Byte)*/
        @SerializedName("state")
        @Expose
        var state: Int? = null,
    /** Описание маршрута.*/
        @SerializedName("description")
        @Expose
        var description: String? = null,
    /** Признак того, что документ направлен для сведения.*/
        @SerializedName("isInfo")
        @Expose
        var isInfo: Boolean? = null,
    /** Контрольная длительность в минутах.*/
        @SerializedName("controlDuration")
        @Expose
        var controlDuration: Int? = null,
    /** Признак "Учитывать только рабочие дни".*/
        @SerializedName("workdaysOnly")
        @Expose
        var workdaysOnly: Boolean? = null,
    /** Контрольная дата окончания.(DateTime)*/
        @SerializedName("controlDate")
        @Expose
        var controlDate: String? = null,
    /** Дата начала.(DateTime)*/
        @SerializedName("startDate")
        @Expose
        var startDate: String? = null,
    /** Дата окончания.(DateTime)*/
        @SerializedName("completeDate")
        @Expose
        var completeDate: String? = null,
    /** Идентификатор документа, закрывшего маршрут.(Guid)*/
        @SerializedName("closingDocUid")
        @Expose
        var closingDocUid: String? = null,
    /** УИД исполнителя по документу.(Guid)*/
        @SerializedName("executiveUid")
        @Expose
        var executiveUid: String? = null,
    /** Подписант по документу.(Guid)*/
        @SerializedName("signerUid")
        @Expose
        var signerUid: String? = null,
    /** Документы, относящиеся к маршруту согласования. Эти документы могут не содержать
        никаких ссылок на виджеты и могут быть пропущены в обновлении.*/
        @SerializedName("docLinks")
        @Expose
        var docLinks: List<DocumentLink>? = null,
    /** Файлы, привязанные к маршруту.*/
        @SerializedName("files")
        @Expose
        var files: List<FileAttachment>? = null,
    /** Коллекция всех этапов согласования.*/
        @SerializedName("stages")
        @Expose
        var stages: List<ApprovalStage>? = null,
    /** Признак отправки на сервис согласования*/
        @SerializedName("sentToCAS")
        @Expose
        var sentToCAS: Boolean? = null
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
        var authorUid: String? = null,
        /** Контрольная дата.(DateTime)*/
        @SerializedName("controlDate")
        @Expose
        var controlDate: String? = null,
        /** Дата передачи.(DateTime)*/
        @SerializedName("sendingDate")
        @Expose
        var sendingDate: String? = null,
        /** Дата отработки документа, когда пользователь дал следующее движение.()DateTime*/
        @SerializedName("workOffDate")
        @Expose
        var workOffDate: String? = null,
        /** Дата закрытия этапа/инстанции. При закрытии инстанция переводится в статус "документ по движению закрыт"()DateTime.*/
        @SerializedName("completeDate")
        @Expose
        var completeDate: String? = null,
        /** Признак возможности редактирования.*/
        @SerializedName("isEditable")
        @Expose
        var isEditable: Boolean? = null
) : ApprovalObjectBase()

/** Этап маршрута согласования.*/
data class ApprovalStage(
    /** Состояние этапа, см. <see cref="ApprovalStagesStates"/>.(Byte)*/
        @SerializedName("state")
        @Expose
        var state: Int? = null,
    /** Тип этапа, см. <see cref="ApprovalStageTypes"/>.(Byte)*/
        @SerializedName("stageType")
        @Expose
        var stageType: Int? = null,
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
        var isFixed: Boolean? = null,
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
        var stations: List<ApprovalStation>? = null,
    /** Требует ЭП*/
        @SerializedName("isDigitalSignRequired")
        @Expose
        var isDigitalSignRequired: Boolean? = null
) : ApprovalChildObject()

/** Инстанция маршрута согласования.*/
data class ApprovalStation(

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
    var receiverUid: String? = null,
//    /** Исполнитель/получатель инстанции (<b>устаревшее поле</b>).*/
//    @SerializedName("receiverUID")
//    @Expose
//    var receiverUidOld:String? = null//Guid*/
    /** Получатель резолюции в справочнике корреспондентов (организаций).(Guid)*/
    @SerializedName("externalReceiverUid")
    @Expose
    var externalReceiverUid: String? = null,
    /** Оператор, который фактически поменял состояние инстанции.(Guid)*/
    @SerializedName("resolutionAuthorUid")
    @Expose
    var resolutionAuthorUid: String? = null,
    /** Резолюция или результат согласования.*/
    @SerializedName("resolution")
    @Expose
    var resolution: String? = null,
    /** Результат согласования, см. <see cref="ApprovalResults"/>.(Byte)*/
    @SerializedName("approvalResult")
    @Expose
    var approvalResult: Int? = null,
    /** Дата просмотра.(DateTime)*/
    @SerializedName("viewDate")
    @Expose
    var viewDate: String? = null,
    /** Главный ответственный исполнитель - должен отображаться жирным шрифтом.*/
    @SerializedName("isMain")
    @Expose
    var isMain: Boolean? = null,
    /** Признак того, что инстанция - для информирования*/
    @SerializedName("isInfo")
    @Expose
    var isInfo: Boolean? = null,
    /** Тип ЭП (0 - нет, 1 - простая, 2 - квалицифированная)(Byte)*/
    @SerializedName("signatureType")
    @Expose
    var signatureType: Int? = null,
    /** Доступные операции согласования для текущего пользователя с учетом замещений.
    Битовая маска, см. <see cref="ApprovalOperations"/>.*/
    @SerializedName("operations")
    @Expose
    var availableOperations: Int? = null,
    /** Расшифрованная отладочная версия <see cref="operations"/>.*/
    @SerializedName("operationsDebug")
    @Expose
    var availableOperationsDebug: List<Int>? = null,
    /** Файлы, привязанные к инстанции (пока там будет не более 1 файла).*/
    @SerializedName("files")
    @Expose
    var files: List<FileAttachment>? = null
) : ApprovalChildObject()
