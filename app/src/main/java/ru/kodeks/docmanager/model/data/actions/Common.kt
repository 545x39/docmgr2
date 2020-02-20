package ru.kodeks.docmanager.model.data.actions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.model.data.ObjectBase
import ru.kodeks.docmanager.model.data.FileUploadInfo


/** Базовое действие (BaseAction)*/

/** Базовое действие по записи информации в СЭДД. Данный класс является базовым для всех корневых действий по записи чего-либо в СЭДД,
на уровне которых должна контролироваться неповторяемость (результат повторного действия будет браться из кэша).*/

/** <h3>Ошибки</h3>
Помимо общих кодов, связанных с парсингом запроса, авторизацией и т.п., при выполнении действия может возникать ряд специфических ошибок:
- \b 500 \link API.ErrorType API.ErrorType::OperationIsRunning \endlink - Операция над объектом все еще выполняется.
- \b 501 \link API.ErrorType API.ErrorType::OperationProcessingError \endlink - Ошибка общего характера при обработке операции (см. детали в теле исключения).*/

/** Среди действий могут встречаться такие ошибки общего характера (коды 5XX):
- \b 502 \link API.ErrorType API.ErrorType::DataVersionConflict\endlink - Конфликт версий клиентских и серверных данных.
- \b 503 \link API.ErrorType API.ErrorType::InputDataError\endlink - Логическая ошибка во входных данных, обнаруженная ядром.
- \b 504 \link API.ErrorType API.ErrorType::ActionTypeMissing\endlink - Для действия не указан его тип.
- \b 510 \link API.ErrorType API.ErrorType::ErrorInChildObjects\endlink - Какая-то ошибка возникла при анализе одного из дочерних объектов составной операции.
- \b 511 \link API.ErrorType API.ErrorType::NoAction\endlink - Не задан тип операции для объекта.
- \b 512 \link API.ErrorType API.ErrorType::ParentActionMismatch\endlink - Тип операции для объекта не соответствует типу операции для родительского объекта.
- \b 520 \link API.ErrorType API.ErrorType::NoObjectId\endlink - Не задан числовой ИД объекта, с которым выполняется действие.
- \b 521 \link API.ErrorType API.ErrorType::NoObjectUid\endlink - Не задан УИД объекта, с которым выполняется действие.*/
abstract class BaseAction<TAction> : ObjectBase() where TAction : BaseAction<TAction> {

    /** УИД операции, по которой ведется отслеживание повторных вызовов (если он пустой, то отслеживание не ведется).
    \b Внимание: нельзя брать в качестве УИДа операции УИД изменяемого объекта СЭДД, т.к. потом с этим объектом ничего нельзя будет сделать!
    Данный механизм придуман для защиты от неконтролируемого клиентом повторного вызова операции, когда на клиента
    не пришел результат предыдущей попытки выполнения операции из-за проблем с сетью и т.п.*/
    @SerializedName("opUid")
    @Expose
    var opUid: String? = null//Guid
}

/** Типы действий с объектами*/

/** Действие общего характера над объектом.*/
class Verb {

    /** Пустое значение (ошибка)*/
    val UNKNOWN = 0

    /** Обновление*/
    val UPDATE = 1

    /** Добавление*/
    val ADD = 2

    /** Удаление*/
    val DELETE = 3
}

/** Базовое действие с документом*/

/** Базовое действие с документом.*/

/** <h3>Ошибки</h3>*
Помимо кодов ошибок, отнаследованных от \ref BaseAction<TAction>, при выполнении данного действия может возникать ряд логических ошибок:
- \b 522 \link API.ErrorType API.ErrorType::NoDocument \endlink - Не задан УИД документа.
- \b 523 \link API.ErrorType API.ErrorType::NoDocType \endlink - Не задан тип документа.*/
abstract class BaseDocumentAction<TAction> : BaseAction<TAction>() where TAction : BaseAction<TAction> {

    /** Идентификатор родительского документа.*/
    @SerializedName("docUid")
    @Expose
    var docUid: String? = null//Guid

    /** Тип документа.*/
    @SerializedName("docType")
    @Expose
    var docType: Int? = null

    /** Содержание документа.*/
    @SerializedName("docName")
    @Expose
    var docName: String? = null

    /** Признак нового документа.*/
    @SerializedName("docIsNew")
    @Expose
    var docIsNew: Boolean? = null

    /** Требует ЭП*/
    @SerializedName("isDigitalSignRequired")
    @Expose
    var isDigitalSignRequired = true
}


/** Передача данных на сервер*/

/** Интерфейс, который реализуют действия, допускающие передачу одиночного вложения с клиента.
Для передачи множественных вложений используется <see cref="IFilesUpload"/>.*/

/** <h3>Ошибки</h3>
При выполнении действия, реализующего этот интерфейс, могут возникать следующие ошибки:
- \b 7 \link API.ErrorType API.ErrorType::ContentNotFound\endlink - В запросе присутствует
ссылка на отсутствующие бинарные данные, которые должны идти следом за JSON телом запроса.*/
interface IFileUpload {

    /** Файл, передаваемый с клиента на сервер.*/
    var  file: FileUploadInfo?
}

/** Интерфейс, который реализуют действия, допускающие передачу сразу нескольких вложений с клиента.
Для передачи множественных вложений используется <see cref="IFileUpload"/>.*/

/** <h3>Ошибки</h3>
При выполнении действия, реализующего этот интерфейс, могут возникать следующие ошибки:
- \b 7 \link API.ErrorType API.ErrorType::ContentNotFound\endlink - В запросе присутствует
ссылка на отсутствующие бинарные данные, которые должны идти следом за JSON телом запроса.*/

interface IFilesUpload {

    /** Файлы, передаваемые с клиента на сервер.*/
    var files: List<FileUploadInfo>?
}