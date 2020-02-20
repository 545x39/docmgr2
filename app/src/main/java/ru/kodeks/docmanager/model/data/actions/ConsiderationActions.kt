package ru.kodeks.docmanager.model.data.actions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.model.data.ObjectBase
import ru.kodeks.docmanager.model.data.FileUploadInfo

data class ErrandAction(
    /** Идентификатор подписавшего резолюцию*/
        @SerializedName("signerUid")
        @Expose
        var signerUid: String? = null,
    /** Документ на контроле*/
        @SerializedName("isControlled")
        @Expose
        var isControlled: Boolean? = null,
    /** Идентификатор документа.*/
        @SerializedName("Uid")
        @Expose
        var uid: String? = null,
    /** Определяет, взяты ли данные инстанции из проекта резолюции*/
        @SerializedName("isProject")
        @Expose
        var isProject: Boolean? = null,
    /** Признак "Личный контроль"*/
        @SerializedName("isPersonalControl")
        @Expose
        var isPersonalControl: Boolean? = null,
    /** Тип важности (Byte)*/
        @SerializedName("importance")
        @Expose
        var importance: Int? = null,
    /** Автор документа*/
        @SerializedName("author")
        @Expose
        var authorUid: String? = null,
    /** Наименование, краткое содержание документа*/
        @SerializedName("name")
        @Expose
        var name: String? = null,
    /** Регистрационный номер документа*/
        @SerializedName("regNum")
        @Expose
        var regNumber: String? = null,
    /** Числовое значение регистрационного номера*/
        @SerializedName("regNo")
        @Expose
        var regNo: String? = null,
    /** Дата регистрации документа (DateTime)*/
        @SerializedName("regDate")
        @Expose
        var regDate: String? = null,
        /** Список групп инстанций (исполнителей).*/
    /** \b Внимание: С клиента обязательно нужно передавать все группы со всеми исполнителями (даже если мы что-то собираемся удалить) -
        это нужно для того, чтобы отследить конфликт между клиентскими и серверными данными (мало ли на сервере данные свежее).*/
        @SerializedName("groups")
        @Expose
        var groups: List<StationGroupAction>? = null,
    /** Операция, см. <see cref="GroupedStationVerb"/>. Нулевое значение недопустимо.(Byte)*/
        @SerializedName("verb")
        @Expose
        var verb: Int? = null,
    /** Удалить! Старая коллекция для передачи @собачек губернатора. Сопутствующие вложения, которые будут прикреплены к инстанции рассмотрения.*/
        @SerializedName("files")
        @Expose
        override var files: List<FileUploadInfo>? = null
) : BaseDocumentAction<ErrandAction>(), IFilesUpload

/** РЕЗОЛЮЦИИ*/

/** Действия*/

/** Действие с корневой резолюцией.*/
sealed class GroupedStationVerb {

    /** Пустое значение (ошибка)*/
    val UNKNOWN = 0

    /** Редактирование (поддерживается только редактирование проектов резолюций)*/
    val UPDATE = 1

    /** Создание*/
    val ADD = 2

    /** Утверждение проекта резолюции <b>по старой схеме</b>. Сейчас надо использовать утверждение проекта через действия с отдельной группой,
    см. <see cref="StationGroupVerb"/>.
    Несколько групп проекта резолюции переводятся в статус \b 1 \link API.DataModel.StationStates API.DataModel.StationStates::Received\endlink (Поступил).*/
    val APPROVE_RESOLUTION_PROJECT_LEGACY = 3
}

/**  Действие над группой исполнителей.*/

sealed class StationGroupVerb {

    /** Пустое значение (ошибка)*/
    val UNKNOWN = 0

    /** Обновление*/
    val UPDATE = 1

    /** Добавление*/
    val ADD = 2

    /** Удаление*/
    val DELETE = 3

    /** Утверждение проекта резолюции.*/
    /** Данная группа проекта резолюции переводится в статус \b 1 \link API.DataModel.StationStates API.DataModel.StationStates::Received\endlink (Поступил),
    а остальные удаляются (<i>PRUZ = 1</i>).*/
    val APPROVE_RESOLUTION_PROJECT = 4

    /** Отклонение проекта резолюции.*/
    /** Группа исполнителей отмечается результатом согласования \b 4 \link API.DataModel.ApprovalResults API.DataModel.ApprovalResults::NotApproved\endlink,
    комментарий пишется в журнал изменений.*/
    val REJECT_RESOLUTION_PROJECT = 5

    /** Согласование проекта резолюции.*/
    /** У инстанций меняется resolutionAuthorUid на шефа текущего пользователя (определяется настройкой).
    Группа исполнителей отмечается результатом согласования \b 1 \link API.DataModel.ApprovalResults API.DataModel.ApprovalResults::ApprovalStarted\endlink.*/
    val REQUEST_RESOLUTION_PROJECT_APPROVAL = 6

    /** Подпись губернатора. При этом проект резолюции сохраняется и следом отклоняется*/
    val GOVERNOR_SIGN = 7
}

/** Действие над исполнителем резолюции (инстанцией).*/

sealed class StationExecutiveVerb {

    /** Пустое значение (ошибка)*/
    val UNKNOWN = 0

    /** Обновление*/
    val UPDATE = 1

    /** Добавление*/
    val ADD = 2

    /** Удаление*/
    val DELETE = 3

    /** Отмена*/
    val CANCEL = 4
}

/** Корневой объект, содержащий информацию для создания/редактирования резолюции (логическое объединение инстанций, отсутствующее в БД),
см. <see cref="IoModel.ChangeResolutionsRequest"/>.
Нужен для агрегирования признаков, являющихся общими для всех дочерних инстанций.
Если какие-то признаки не переданы, но в БД уже есть такая резолюция, то значения признаков берутся из сохраненной копии.
Класс назван в честь одноименного объекта ядра, чтобы проще было разбираться в коде.*/
data class GroupedStationAction(
    /** Идентификатор подписавшего резолюцию (Guid)*/
        @SerializedName("signerUid")
        @Expose
        var signerUid: String? = null,
    /** Документ на контроле*/
        @SerializedName("isControlled")
        @Expose
        var isControlled: Boolean? = null,
    /** Идентификатор контролера исполнения документа. В старом протоколе не использовался. В вебе берется из карточки контроля. (Guid)*/
        @SerializedName("controllerUid")
        @Expose
        var controllerUid: String? = null,
    /** Идентификатор вида контроля (контрольного поручения). В старом протоколе не использовался. В вебе берется из карточки контроля.*/
        @SerializedName("controlAssignmentTypeId")
        @Expose
        var controlAssignmentTypeId: Int? = null,
    /** Идентификатор видеоролика резолюции*/
        @SerializedName("clipId")
        @Expose
        var clipId: Int? = null,
    /** Определяет, взяты ли данные инстанции из проекта резолюции*/
        @SerializedName("isProject")
        @Expose
        var isProject: Boolean? = null,
    /** Признак "Личный контроль"*/
        @SerializedName("isPersonalControl")
        @Expose
        var isPersonalControl: Boolean? = null,
    /** Тип важности (Byte)*/
        @SerializedName("importance")
        @Expose
        var importance: Int? = null,
    /** Идентификатор подборки, в рамках которой выполняется подписание проекта губернатором.*/
        @SerializedName("compilationId")
        @Expose
        var compilationId: Int? = null,
        /** Список групп инстанций (исполнителей).*/
    /** \b Внимание: С клиента обязательно нужно передавать все группы со всеми исполнителями (даже если мы что-то собираемся удалить) -
        это нужно для того, чтобы отследить конфликт между клиентскими и серверными данными (мало ли на сервере данные свежее).*/
        @SerializedName("groups")
        @Expose
        var groups: List<StationGroupAction>? = null,
    /** Операция, см. <see cref="GroupedStationVerb"/>. Нулевое значение недопустимо.(Byte)*/
        @SerializedName("verb")
        @Expose
        var verb: Int? = null,
    /** Удалить! Старая коллекция для передачи @собачек губернатора. Сопутствующие вложения, которые будут прикреплены к инстанции рассмотрения.*/
        @SerializedName("files")
        @Expose
        override var files: List<FileUploadInfo>? = null
) : SignedStationAction<GroupedStationAction>(), IFilesUpload

/** Группа исполнителей внутри <see cref="GroupedStationAction"/>. Объединяет исполнителей общим сроком исполнения, текстом резолюции и еще несколькими полями.
Класс назван в честь одноименного объекта ядра, чтобы проще было разбираться в коде.*/
data class StationGroupAction(
        /** Идентификатор группы инстанций (уникален в пределах родительской резолюции).(Byte)*/
    /** Нумерация должна начинаться с 1 и все новые группы исполнителей должны иметь уникальные номера, начиная с максимального существующего + 1.
        Если новая группа с заданным кодом уже существует на сервере, то сервис выдаст ошибку <see cref="API.ErrorType"/>.\b DataVersionConflict.*/
        @SerializedName("id")
        @Expose
        var id: Int? = null,
    /** Текст резолюции*/
        @SerializedName("resolution")
        @Expose
        var resolution: String? = null,
    /** Контрольная дата исполнения (DateTime)*/
        @SerializedName("controlDate")
        @Expose
        var controlDate: String? = null,
    /** Признак резолюции - "Для информирования/Для сведения*/
        @SerializedName("isInformational")
        @Expose
        var isInformational: Boolean? = null,
    /** Признак - Требует ответа*/
        @SerializedName("isAnswerRequired")
        @Expose
        var isAnswerRequired: Boolean? = null,
    /** Комментарий к отклонению/согласованию проекта резолюции*/
        @SerializedName("comment")
        @Expose
        var comment: String? = null,
    /** Операция, см. <see cref="StationGroupVerb"/>. Нулевое значение недопустимо.(Byte)*/
        @SerializedName("verb")
        @Expose
        var verb: Int? = null,
    /** Список исполнителей. Если на сервере есть хоть один исполнитель, который не пришел с клиента, то сервис выдаст ошибку
        <see cref="API.ErrorType"/>.\b DataVersionConflict (при условии, что это не утверждение проекта!).*/
        @SerializedName("executives")
        @Expose
        var executives: List<StationExecutiveAction>? = null,
    /** Сопутствующие вложения, которые будут прикреплены к инстанции рассмотрения (используется для передачи @собачек губернатора).*/
        @SerializedName("files")
        @Expose
        var files: List<FileUploadInfo>? = null
) : ObjectBase()

/** Исполнитель внутри группы исполнителей <see cref="StationGroupAction"/>. Этот объект соответствует инстанции, которая будет создана/удалена/изменена в БД.*/
data class StationExecutiveAction(
        /** Идентификатор исполнителя (Guid)*/
        @SerializedName("uid")
        @Expose
        var uid: String? = null,
        /** Числовой идентификатор инстанции. Для новых инстанций нужно передавать \b 0 или вообще пропускать это поле.*/
        @SerializedName("stationId")
        @Expose
        var stationId: Int? = null,
        /** Дата создания исполнителя (считывается только у новых исполнителей, у которых stationId не является положительным числом) (DateTime)*/
        @SerializedName("createDate")
        @Expose
        var createDate: String? = null,
        /** Признак ответственного исполнителя*/
        @SerializedName("isMain")
        @Expose
        var isMain: Boolean? = null,
        /** Признак фиксированной инстанции*/
        @SerializedName("isFixed")
        @Expose
        var isFixed: Boolean? = null,
        /** Операция, см. <see cref="StationExecutiveVerb"/>. Нулевое значение недопустимо.(Byte)*/
        @SerializedName("verb")
        @Expose
        var verb: Int? = null
) : ObjectBase()

/**  ОТЧЕТЫ */

/** Действие над отчетом.*/
sealed class ResolutionReportStationVerb {

    /** Пустое значение (ошибка)*/
    val UNKNOWN = 0

    /** Обновление*/
    val UPDATE = 1

    /** Добавление*/
    val ADD = 2

    /** Принимает и закрывает отчет, закрывает его родительскую резолюцию*/
    val ACCEPT_CLOSE = 3

    /** Принимает и закрывает отчет, отменяет его родительскую резолюцию*/
    val ACCEPT_CANCEL = 4

    /** Отклоняет отчет*/
    val REJECT = 5
}

/** Действие с отчетом по резолюции, см. <see cref="IoModel.ChangeResolutionReportsRequest"/>.*/
data class ResolutionReportStationAction(
        /** Идентификатор инстанции отчета (для новых отчетов не заполняется). При создании отчета в это поле сервис складывает ИД вновь созданной инстанции с отчетом.*/
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        /** Идентификатор исполнителя. Передавать нужно только при создании отчета. При остальных действиях с отчетом можно не передавать,
        тогда он подтянется из серверного объекта <b>GroupedResolutionInfo</b>.(Guid)*/
        @SerializedName("receiverUid")
        @Expose
        var receiverUid: String? = null,
        /** Текст резолюции. Передавать нужно только при создании и изменении отчета.*/
        @SerializedName("resolution")
        @Expose
        var resolution: String? = null,
        /** Идентификатор подписавшего резолюцию. Обычно передается текущий пользователь. Учитывается при создании и изменении отчета.(Guid)*/
        @SerializedName("signerUid")
        @Expose
        var signerUid: String? = null,
        /** Причина отклонения, передавать нужно только при отклонении отчета.*/
        @SerializedName("rejectReason")
        @Expose
        var rejectReason: String? = null,
        /** Дата создания отчета (считывается только у нового отчета).(DateTime)*/
        @SerializedName("createDate")
        @Expose
        var createDate: String? = null,
        @SerializedName("verb")
        @Expose
        var verb: Int? = null,
        /** Файлы, которые нужно прикрепить к отчету. Учитываются только при создании и изменении отчета.*/
        @SerializedName("files")
        @Expose
        override var files: List<FileUploadInfo>? = null
        /** Операция, см. <see cref="ResolutionReportStationVerb"/>. Нулевое значение недопустимо.(Byte)*/
) : SignedStationAction<ResolutionReportStationAction>(), IFilesUpload

/**  ОТПРАВКА В АРХИВ */

/** Действие по архивации входящей резолюции для информирования, см. <see cref="IoModel.ArchiveDocumentsRequest"/>.*/

/** <h3>Ошибки</h3>
/// Помимо кодов ошибок, отнаследованных от \ref BaseStationAction<TAction>, при выполнении данного действия может возникать ряд логических ошибок (коды 6XX):
/// - \b 630 \link API.ErrorType API.ErrorType::DocArchiveError \endlink - Непредвиденная ошибка при архивировании инстанции по входящей резолюции для информирования.*/

data class ArchiveDocumentAction(
        /** Идентификатор подборки, в рамках которой выполняется ознакомление с документом.*/
        @SerializedName("compilationId")
        @Expose
        var compilationId: Int? = null,
        /** Вложение к действию (@собачка губернатора).*/
        @SerializedName("file")
        @Expose
        //FileUploadInfo File
        override var file: FileUploadInfo? = null
) : BaseStationAction<ArchiveDocumentAction>(), IFileUpload

/** СНЯТИЕ С ЛИЧНОГО КОНТРОЛЯ */

/** Действие над статусом личного контроля.*/
sealed class PersonalControlVerb {
    /** Пустое значение (ошибка)*/
    val UNKNOWN = 0

    /** Снятие с личного контроля*/
    val REMOVE_FROM_CONTROL = 1

    /** Установка признака личного контроля*/
    val ADD_TO_CONTROL = 2
}

/** Действие по изменению статуса личного контроля, см. <see cref="IoModel.PersonalControlChangeRequest"/>.*/
data class PersonalControlAction(
        /** Операция, см. @see cref="PersonalControlVerb"/>. Нулевое значение недопустимо. (Byte)*/
        @SerializedName("verb")
        @Expose
        val verb: Int? = null
) : BaseDocumentAction<PersonalControlAction>()