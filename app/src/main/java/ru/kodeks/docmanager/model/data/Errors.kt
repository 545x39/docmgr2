package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Тип ошибки. Все новые ошибки нужно снабжать индивидуальным кодом. Все старые ошибки нужно также перевести на использование кодов.*/
object ErrorType {

    /** Ошибки отсутствуют. Код используется в тех местах, где нужен ResultCode.*/
    const val OK = 0

    /** Общие коды*/

    /** Неопознанный тип ошибки. Скорее всего, сервис и сам не понимает, что произошло.*/
    const val UNKNOWN = 1

    /** Не удалось распарсить запрос.*/
    const val BAD_REQUEST = 2

    /** Ошибка доступа. Вероятная причина - некорректная конфигурация сервиса.*/
    const val ACCESS_DENIED = 3

    /** Доступ к системе запрещен (аутентификация не прошла).*/
    const val AUTHENTICATION_ERROR = 4

    /** Функциональность не реализована. Эту ошибку можно увидеть, если сервис работает в режиме отладки (throwNotImplemented = true)
    и в процессе обработки запроса возникает попытка обратиться к нереализованной функциональности.*/
    const val NOT_IMPLEMENTED = 5

    /** Какая-то ошибка при выполнении обращения к БД. Может быть как временная, так и перманентная.*/
    const val GENERIC_DB_ERROR = 6

    /** В запросе присутствует ссылка на отсутствующие бинарные данные, которые идут следом за JSON телом запроса.*/
    const val CONTENT_NOT_FOUND = 7

    /** Пустой запрос*/
    const val EMPTY_REQUEST = 8

    /** Ошибка при сжатии PDF*/
    const val PDF_COMPRESSION_FAILED = 11

    /** Не корректная версия клиента - не попадает в диапазон минимально и максимально совместимых версий*/
    const val INCORRECT_CLIENT_VERSION = 17

    /** Загрузка и чтение данных об объектах СЭДД*/

    /** Ошибка при чтении настроек. Может быть заполнен itemName = setting.Reference.*/
    const val SETTINGS = 20

    /** Ошибка при чтении справочников [не используется, зарезервирован].*/
    const val CLASSIFIERS = 21

    /** Ошибка при чтении из Глобального Каталога. Ключи объектов не заполняются.*/
    const val GLOBAL_OBJECTS = 22

    /** Ошибка при чтении организации. Ключи объектов не заполняются.*/
    const val ORGANIZATIONS = 23

    /** Ошибка при чтении прав пользователя. Заполняется itemUid.*/
    const val UserRights = 30

    /** Ошибка при чтении рабочих столов. Может заполняться itemId.*/
    const val Desktops = 40

    /** Ошибка при заполнении базовой части элементов рабочих столов (шорткаты и виджеты). Может заполняться itemId.*/
    const val DESKTOP_ITEM_BASES = 41

    /** Ошибка при загрузке виджетов. Может заполняться itemId.*/
    const val WIDGETS = 42

    /** Ошибка при загрузке шорткатов. Может заполняться itemId.*/
    const val SHORTCUTS = 43

    /** Ошибка при загрузке документа или списка документов. Ключи объектов не заполняются.*/
    const val DOCUMENTS = 50

    /** Неизвестный тип документа. itemID содержит нераспознанный тип документа. Такую ошибку надо воспринимать как нормальную, просто документ нужно игнорить.*/
    const val UNKNOWN_DOC_TYPE = 51

    /** Документ не найден.*/
    const val DOCUMENT_NOT_FOUND = 52

    /** Ошибка при загрузке пометок документа. Ключи объектов не заполняются.*/
    const val DOC_NOTES = 53

    /** Ошибка при загрузке мультимедиа файлов [не используется].*/
    const val MULTIMEDIA = 54

    /** Ошибка, связанная с получением инстанций. Ключи объектов не заполняются.*/
    const val STATIONS = 60

    /** Ошибка, связанная с заполнением групп исполнителей ().*/
    const val STATION_GROUPS = 61

    /** Ошибка, связанная с исполнителями резолюций.*/
    const val STATION_EXECUTIVES = 62

    /** Ошибка, связанная с папками.*/
    const val FOLDERS = 70

    /** Ошибка, связанная с файлами.*/
    const val FILES = 71

    /** Файл не найден по указанному идентификатору.*/
    const val FILE_NOT_FOUND = 72

    /** Файл является обязательным для выполнения действия.*/
    const val FILE_REQUIRED = 73

    /** Параметры загрузки файлов не настроены или настроены неверно*/
    const val FILE_UPLOAD_CONFIGURATION_ERROR = 74

    /** Одновременная загрузка разных частей файла не поддерживается.*/
    const val CONCURRENT_FILE_UPLOAD_IS_NOT_SUPPORTED = 76

    /**  ЭП */
    /**  Ошибка при сохранении ЭП. Данные при этом были успешно сохранены на сервере, но без ЭП.*/
    const val SIGNATURE_SAVE_ERROR = 300

    /** Нет прав на создание ЭП, но она была передана в сервис. В этом случае данные были успешно сохранены на сервере без ЭП.*/
    const val SIGNATURE_WRITE_DENIED = 301

    /** Есть право на создание ЭП, но она не была передана в сервис (зарезервировано на будущее).*/
    const val SIGNATURE_MISSING = 302

    /** Обнаружено нарушение целостности данных при проверке ЭП (вероятней всего, данные были изменены с момента формирования ЭП).*/
    const val DATA_INTEGRITY_ERROR = 303

    /** Ошибка при криптографической проверке подлинности ЭП.*/
    const val SIGNATURE_VERIFICATION_ERROR = 304

    /** Действия*/

    /** Общие ошибки, связанные с действиями*/

    /** Операция над объектом все еще выполняется. При получении такого кода операцию нужно повторить через не которое время,
    чтобы получить посчитанный в рамках этой операции ответ (он вычисляется ровно 1 раз).*/
    const val OPERATION_IS_RUNNING = 500

    /** Ошибка общего характера при обработке операции (см. детали в теле исключения). Означает, что возникло исключение при обработке конкретной операции.*/
    const val OPERATION_PROCESSING_ERROR = 501

    /** Конфликт версий клиентских и серверных данных.
    Типовой сценарий - клиент забирает данные, модифицирует их и шлет на сервер,
    а в это время данные на сервере тоже успели поменяться. В этом случае, как правило, вместе с данным кодом ошибки с сервера приходит
    актуальная версия серверных данных.*/
    const val DATA_VERSION_CONFLICT = 502

    /**  Логическая ошибка во входных данных, обнаруженная ядром. Как правило текст ошибки составлен на русском языке и предназначен для отображения пользователю.*/
    const val INPUT_DATA_ERROR = 503

    /** Для действия не указан его тип.*/
    const val ACTION_TYPE_MISSING = 504

    /** Какая-то ошибка возникла при анализе одного из дочерних объектов составной операции.*/
    const val ERROR_IN_CHILD_OBJECTS = 510

    /** Не задан тип операции для объекта.*/
    const val NO_ACTION = 511

    /** Тип операции для объекта не соответствует типу операции для родительского объекта.*/
    const val PARENT_ACTION_MISMATCH = 512

    /** Не задан числовой ИД объекта, с которым выполняется действие.*/
    const val NO_OBJECT_ID = 520

    /** Не задан УИД объекта, с которым выполняется действие.*/
    const val NO_OBJECT_UID = 521

    /** Не задан УИД документа.*/
    const val NO_DOCUMENT = 522

    /** Не задан тип документа.*/
    const val NO_DOC_TYPE = 523

    /** Не задан идентификатор входящей (родительской) инстанции.*/
    const val NO_INCOMING_STATION = 530

    /** Входящая инстанция не найдена в маршруте рассмотрения.*/
    const val INCOMING_STATION_NOT_FOUND = 550

    /** Маршрут не допускает запрошенного действия.*/
    const val INVALID_ROUTE_STATE = 560

    /** Резолюции */

    /** Непредвиденная ошибка при создании резолюции.*/
    const val RESOLUTION_CREATION_ERROR = 601

    /** Непредвиденная ошибка при обновлении резолюции.*/
    const val RESOLUTION_UPDATE_ERROR = 602

    /** Непредвиденная ошибка при подписании/утверждении проекта резолюции.*/
    const val RESOLUTION_PROJECT_APPROVAL_ERROR = 603

    /** Маршрут рассмотрения не найден.*/
    const val CONSIDERATION_ROUTE_NOT_FOUND = 604

    /** Признак контроля может менять только главный исполнитель.*/
    const val CANNOT_CHANGE_CONTROL_STATE = 605

    /** Возникла ошибка при обработке данных резолюции.*/
    const val INVALID_RESOLUTION_DATA = 606

    /** Непредвиденная ошибка при отклонении проекта резолюции.*/
    const val RESOLUTION_PROJECT_REJECTION_ERROR = 607

    /** Непредвиденная ошибка при согласовании проекта резолюции.*/
    const val RESOLUTION_PROJECT_APPROVAL_REQUEST_ERROR = 608

    /** Не заполнены группы исполнителей резолюции.*/
    const val STATION_GROUPS_MISSING = 610

    /** Код группы резолюции не указан или не является положительным числом.*/
    const val BAD_STATION_GROUPID = 611

    /** Не заполнен список исполнителей в группе исполнителей.*/
    const val EXECUTIVES_MISSING = 612

    /** В инстанции с одинаковым ИД на сервере и на клиенте разные исполнители.*/
    const val EXECUTIVE_MISMATCH = 613

    /** Не допускается направлять резолюции самому себе.*/
    const val SELF_EXECUTIVE = 614

    /** Несовместимые действия над группами проекта резолюции.*/
    const val PROJECT_GROUPACTIONS_MISMATCH = 615

    /** Утверждение нескольких групп проекта резолюции не допускается.*/
    const val MULTIPLE_PROJECT_APPROVAL_ACTIONS = 616

    /** Действие недопустимо, т.к. проект резолюции утвержден.*/
    const val INVALID_ACTION_FOR_APPROVED_RESOLUTION_PROJECT = 617

    /** Действие недопустимо, т.к. проект резолюции передан другому сотруднику.*/
    const val RESOLUTION_PROJECT_AUTHOR_MISMATCH = 618

    /** Действие недопустимо, т.к. оно противоречит состоянию подборки.*/
    const val WRONG_COMPILATION_STATE = 619

    /** Не указан ИД подборки*/
    const val MISSING_COMPILATION_ID = 620

    /** Отчеты по резолюциям*/

    /** Непредвиденная ошибка при сохранении отчета.*/
    const val REPORT_SAVE_ERROR = 625

    /** Не указан получатель отчета (\b receiverUid) при его создании.*/
    const val REPORT_RECEIVER_EMPTY = 626

    /** Не допускается направлять отчет самому себе.*/
    const val SELF_REPORT_RECEIVER = 627

    /** Архивирование документов (отработка резолюций для информирования)*/

    /** Непредвиденная ошибка при архивировании инстанции по входящей резолюции для информирования.*/
    const val DOC_ARCHIVE_ERROR = 630

    /** Личный контроль*/

    /** Непредвиденная ошибка при изменении статуса личного контроля.*/
    const val personal_control_error = 640

    /** Невозможно изменить статус личного контроля - нет исходящих резолюций.*/
    const val PERSONAL_CONTROL_NO_RESOLUTIONS_OUT = 641

    /** Согласование*/

    /** Операция требует указание кода причины.*/
    const val RESULT_CODE_MISSING = 701

    /** Операция требует указание текста причины.*/
    const val RESULT_TEXT_MISSING = 702

    /** Действие по согласованию утратило актуальность.*/
    const val OUTDATED_APPROVAL_STATION_ACTION = 710

    /** Маршрут согласования был изменен другими участниками на сервисе обмена.*/
    const val APPROVAL_STATION_CAS_ROUTE_VERSION_CHANGED = 720

    /** Отсроченное выполнение запроса*/

    /** Отложенный результат еще не готов (используется для внутренних нужд сервиса).*/
    const val DEFERRED_REQUEST_RUNNING = 1001

    /** Отложенный результат отсутствует в кэше (используется для внутренних нужд сервиса).*/
    const val DEFERRED_RESULT_EXPIRED = 1002
}

/// Ошибка. Может содержать ссылку на бизнес-объект, с которым она связана.*/
data class Error(
    /// Тип (код) ошибки, по которому можно установить тип объекта, вызвавшего ошибку.*/
    @SerializedName("type")
    @Expose
    var type: Int? = null, //Value of ErrorType class!!!
    /// Текст ошибки. Оно должно быть на русском языке и пригодно для отображения конечному пользователю.*/
    @SerializedName("message")
    @Expose
    var message: String? = null,
    /// Стек вызовов вместе с оригинальным сообщением об ошибке. Не предназначено для отображения конечному пользователю.*/
    @SerializedName("details")
    @Expose
    var details: String? = null,
    /// Целочисленный ИД объекта, вызвавшего ошибку. За тип объекта отвечает тип ошибки.*/
    @SerializedName("itemId")
    @Expose
    var itemId: Long? = null, //Long
    /// УИД объекта, вызвавшего ошибку. За тип объекта отвечает тип ошибки.*/
    @SerializedName("itemUid")
    @Expose
    var itemUid: String? = null, //Guid
    /// Имя объекта, вызвавшего ошибку. Передается только если нет ИД или УИД. За тип объекта отвечает тип ошибки.*/
    @SerializedName("itemName")
    @Expose
    var ItemName: String? = null
)

open class IErrors {
    /// Коллекция ошибок. Если все прошло успешно, то коллекция = null.*/
    @SerializedName("errors")
    @Expose
    open var errors: List<Error>? = null
}


/** Типовые сообщения об ошибках.*/
object ErrorMessages {

    const val AuthenticationError = "Ошибка входа в систему"
    const val AccessDenied = "Ошибка доступа."
    const val NoSourceObject = "Не задан объект копирования."
    const val ContentNotFound =
        "В запросе присутствует ссылка на отсутствующие бинарные данные, которые идут следом за JSON телом запроса."
    const val FileNotFound = "Файл не найден по указанному идентификатору."
    const val FileRequired = "Файл является обязательным для выполнения действия."
    const val EmptyRequest = "Пустой запрос."
    const val PdfCompressionFailed = "Ошибка при сжатии PDF."

    //    #region Файлы*/
    const val FileUploadConfigurationError =
        "Параметры загрузки файлов не настроены или настроены неверно."
    const val ConcurrentFileUploadIsNotSupported =
        "Одновременная загрузка разных частей файла не поддерживается."

    //    #region ЭП*/
    const val SignatureWriteDenied = "У пользователя нет прав на добавление ЭП."
    const val SignatureMissing = "Есть право на создание ЭП, но ЭП не была передана на сервер."
    const val DataIntegrityError = "Нарушена целостность данных."
    const val SignatureVerificationError = "ЭП не действительна."

    //    #region Операции*/
    const val OperationIsRunning = "Операция еще выполняется. Повторите запрос позже."
    const val ActionTypeMissing = "Для действия не указан его тип."
    const val ErrorInChildObjects = "Возникла ошибка в дочерних объектах."
    const val DataVersionConflict =
        "Информация о документе устарела. Обновите данные и повторите действие."
    const val NoAction = "Не задан тип операции."
    const val ParentActionMismatch =
        "Тип операции для объекта не соответствует типу операции для родительского объекта."
    const val NoObjectId = "Не задан числовой ИД объекта, с которым выполняется действие."
    const val NoObjectUid = "Не задан УИД объекта, с которым выполняется действие."

    const val NoDocument = "Не задан идентификатор документа."
    const val NoDocType = "Не задан тип документа."
    const val NoIncomingStation = "Не задан идентификатор входящей инстанции."

    const val IncomingStationNotFound = "Входящая инстанция не найдена."
    const val InvalidRouteState = "Маршрут не допускает запрошенного действия."

    const val ConsiderationRouteNotFound = "Маршрут рассмотрения не найден."

    //    #region Резолюции и отчеты по резолюциям*/
    const val CanNotChangeControlState = "Признак контроля может менять только главный исполнитель."
    const val StationGroupsMissing = "Не заполнены группы исполнителей резолюции."
    const val BadStationGroupId =
        "Код группы резолюции не указан или не является положительным числом."
    const val ExecutivesMissing = "Не заполнен список исполнителей в группе исполнителей."
    const val ExecutiveMismatch =
        "Несоответствие идентификаторов исполнителей в инстанциях с одинаковым ИД."
    const val SelfExecutive = "Не допускается направлять резолюции самому себе."
    const val ProjectGroupActionsMismatch = "Несовместимые действия над группами проекта резолюции."
    const val MultipleProjectApprovalActions =
        "Утверждение нескольких групп проекта резолюции не допускается."
    const val InvalidActionForApprovedResolutionProject =
        "Действие недопустимо, т.к. проект резолюции утвержден."
    const val ResolutionProjectAuthorMismatch =
        "Действие недопустимо, т.к. проект резолюции передан другому сотруднику."
    const val WrongCompilationState =
        "Действие недопустимо, т.к. оно противоречит состоянию подборки."
    const val MissingCompilationId = "Не указан ИД подборки."

    const val ReportReceiverEmpty = "Не указан получатель отчета при его создании."
    const val SelfReportReceiver = "Не допускается направлять отчет самому себе."

    //    #region Согласование*/
    const val ResultCodeMissing = "Операция требует указание кода причины."
    const val ResultTextMissing = "Операция требует указание текста причины."
    const val OutdatedApprovalStationAction =
        "Действие по согласованию/подписанию утратило актуальность."
}