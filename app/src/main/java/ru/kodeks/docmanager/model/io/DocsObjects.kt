package ru.kodeks.docmanager.model.io

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.const.JsonNames.APPROVAL_ACTIONS
import ru.kodeks.docmanager.const.JsonNames.DOCS
import ru.kodeks.docmanager.const.JsonNames.DOCS_COUNT
import ru.kodeks.docmanager.const.JsonNames.DOC_NOTE_ACTIONS
import ru.kodeks.docmanager.const.JsonNames.GROUPED_STATIONS
import ru.kodeks.docmanager.const.JsonNames.ORGANIZATIONS
import ru.kodeks.docmanager.const.JsonNames.ORGANIZATIONS_COUNT
import ru.kodeks.docmanager.const.JsonNames.REJECTED_DOCS
import ru.kodeks.docmanager.const.JsonNames.REPORT_STATIONS
import ru.kodeks.docmanager.const.JsonNames.SIGNED_DOCS
import ru.kodeks.docmanager.model.data.Document
import ru.kodeks.docmanager.model.data.Organization
import ru.kodeks.docmanager.model.data.StationSignature
import ru.kodeks.docmanager.model.data.actions.*

/**  BASE TYPES  */

/** Интерфейс запроса на операции с документом.*/
interface IDocumentActionsRequest<TAction> where TAction : BaseDocumentAction<TAction> {
    /** Список операций с документом.*/
    var documentActions: List<DocNoteAction>?
}


/** Интерфейс ответа на операции с документом.*/
interface IDocumentActionsResponse<TAction> where TAction : BaseDocumentAction<TAction> {

    /** Список операций с документом.*/
    var documentActions: List<DocNoteAction>?
}

/** <see cref="API.IDocs"/>. Ответ на запрос списка документов.*/

open class DocsListResponseBase<T> : ResponseBase<T>() where T : DocsListResponseBase<T> {

    /** Количество возвращаемых организаций.*/
    @SerializedName(ORGANIZATIONS_COUNT)
    @Expose
    var organizationsCount: Int? = null

    /** Список организаций, использующихся в возвращаемых документах.*/
    @SerializedName(ORGANIZATIONS)
    @Expose
    var organizationsList: List<Organization>? = null

    /** Количество возвращаемых документов.*/
    @SerializedName(DOCS_COUNT)
    @Expose
    var docsCount: Int? = null

    /** Список документов вместе со всем их содержимым, включая вложения (только метаинформация), инстанции, пометки и связанные документы.*/
    @SerializedName(DOCS)
    @Expose
    var documents: List<Document>? = null
}

/**    Get documents  */

/**<see cref="API.IDocs"/>. Запрос на получение списка документов.
\b Ответ: <see cref="GetDocsResponse"/>.
h3>Пример</h3>
pre><code>
 *    {
 *      "user": {
 *        "login": "gromov",
 *        "password": "11111",
 *        "device": "iOS",
 *        "version": "1.2.0"
 *      },
 *      "docUids": [
 *        "670ffab8-a675-4e29-a23a-b92c3ed8a045"
 *      ],
 *      "fillWidgetLinks": false
 *    }
</code></pre>*/
class GetDocsRequest : RequestBase() {

    /** Список УИДов документов, которые нужно вернуть.*/
    @SerializedName("docUids")
    @Expose
    var docUids: List<String>? = null//>Guid[]

    /** Заполнять ли связи с виджетами.*/
    @SerializedName("fillWidgetLinks")
    @Expose
    var fillWidgetLinks: Boolean? = null
}


/** <see cref="API.IDocs"/>. Ответ на запрос списка документов.*
\b Запрос: <see cref="GetDocsRequest"/>.*/
class GetDocsResponse : DocsListResponseBase<GetDocsResponse>()

/**   ЭП  */

/**  GetSignatureStamp   */

/** <see cref="API.IDocs"/>. Запрос на получение штампа согласования для использования в ЭП.
\b Ответ: набор байт картинки штампа.
<h3>Пример</h3>
<pre><code>
 *    {
 *      "user": {
 *        "login": "gromov",
 *        "password": "11111",
 *        "device": "iOS",
 *        "version": "1.2.0"
 *      },
 *      "signatureType": 1
 *    }
</code></pre>*/
class GetSignatureStampRequest : RequestBase() {

    /** Тип ЭП (1 - простая, 2 - квалицифированная).*/
    @SerializedName("signatureType")
    @Expose
    var signatureType: Int? = null//Byte
}

/** GetSignatures   */

/** <see cref="API.IDocs"/>. Запрос на получение ЭП по списку инстанций в документе.
\b Ответ: <see cref="GetSignaturesResponse"/>.
<h3>Пример</h3>
<pre><code>
 *    {
 *      "user": {
 *        "login": "gromov",
 *        "password": "11111",
 *        "device": "iOS",
 *        "version": "1.2.0"
 *      },
 *      "docUid": "F0C8AC8A-B286-4617-8293-7DED2D9F38DD",
 *      "considerationStationIds": [
 *        152502
 *      ]
 *    }
</code></pre>*/
class GetSignaturesRequest : RequestBase() {

    /** УИД родительского документа.*/

    @SerializedName("docUid")
    @Expose
    var docUid: String? = null//Guid

    /**<b>Временно оставлено для сохранения обратной совместимости!</b> Список ИД инстанций <b>резолюций и отчетов по резолюциям</b> в пределах одного документа, по которым нужно вернуть ЭП.*/
    @SerializedName("stationIds")
    @Expose
    var stationIds: List<Int>? = null

    /** Список ИД инстанций <b>маршрута рассмотрения</b> (резолюций и отчетов по резолюциям) в пределах одного документа, по которым нужно вернуть ЭП.*/
    @SerializedName("considerationStationIds")
    @Expose
    var considerationStationIds: List<Int>? = null

    /** Список ИД инстанций <b>маршрута согласования</b> в пределах одного документа, по которым нужно вернуть ЭП.*/
    @SerializedName("approvalStationIds")
    @Expose
    var approvalStationIds: List<Int>? = null
}

/** <see cref="API.IDocs"/>. Ответ на запрос ЭП. Ошибки валидации приходят в коллекции \b errors у объектов <see cref="DataModel.StationSignature"/>.
\b Запрос: <see cref="GetSignaturesRequest"/>.*/
class GetSignaturesResponse : ResponseBase<GetSignaturesResponse>() {

    /** Список ЭП, ИД инстанций и ошибки серверной проверки подлинности ЭП (если есть).*/
    @SerializedName("stationSignatures")
    @Expose
    var stationSignatures: List<StationSignature>? = null

    /** Текущий сиквенс документа. Нужен для сравнения клиентской и серверной версий документа,
    чтобы избежать фактора рассинхронизации данных при проверке подлинности ЭП.
    Если на вход не передана ни одна инстанция, то поле не заполняется (и не возвращается).*/
    @SerializedName("docSequence")
    @Expose
    var docSequence: Long? = null
}

/**    ПОМЕТКИ ДОКУМЕНТА  */

/** <see cref="API.IDocs"/>. Запрос на изменение пометок в документах. За один раз может быть отправлено несколько действий по нескольким документам.
УИД каждой операции - это УИД пометки (операции не трекаются на повтор!). По каждой пометке возвращается свой ответ.
\b Ответ: <see cref="ChangeDocNotesResponse"/>.
<h3>Пример</h3>
<pre><code>
 *    {
 *      "user": {
 *        "login": "gromov",
 *        "password": "11111",
 *        "device": "iOS",
 *        "version": "1.2.0"
 *      },
 *      "docNoteActions": [
 *        {
 *          "docUid": "239a3e50-919d-45f2-9e9d-32dfb3492454",
 *          "changedNotes": [
 *            {
 *              "uid": "2fdaf991-5f2e-4649-b5d4-48672710e3cb",
 *              "note": "Пометка с мобильного",
 *              "common": false,
 *              "verb": 1
 *            }
 *          ]
 *        }
 *      ]
 *    }
</code></pre>*/
class ChangeDocNotesRequest : RequestBase(), IDocumentActionsRequest<DocNoteAction> {

    /** Список действий с пометками документов.*/
    @SerializedName(DOC_NOTE_ACTIONS)
    @Expose
    var docNoteActions: List<DocNoteAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref docNoteActions.*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ с результатами изменения пометок вместе с обновленной полной версией всех участвующих в операции документов.
\b Запрос: <see cref="ChangeDocNotesRequest"/>.*/

class ChangeDocNotesResponse : ResponseBase<ChangeDocNotesResponse>(),
    IDocumentActionsResponse<DocNoteAction> {

    /** Калька запроса, в которой заполнен результат выполнения каждой операции над пометкой.*/
    @SerializedName(DOC_NOTE_ACTIONS)
    @Expose
    var docNoteActions: List<DocNoteAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref docNoteActions.*/
    override var documentActions: List<DocNoteAction>? = null
}

/**    МАРШРУТ СОГЛАСОВАНИЯ */

/**  SignDocuments */

/** <see cref="API.IDocs"/>. Запрос на подписание документов в рамках процесса согласования (кнопка "Подписать" из виджета "На подпись").
\b Ответ: <see cref="SignDocumentsResponse"/>.
<h3>Пример 1 (новый вариант, отправка блобов отдельными запросами UploadFile.ashx)</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "gromov",
 *		    "password" : "11111",
 *		    "device" : "iOS",
 *		    "version" : "1.2.0"
 *	    },
 *	    "signedDocs" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "incomingStationId" : 160125,
 *	    	    "file" : {
 *		    	    "blobUid" : "a2125ea0-73fa-4e32-b42d-dc6cf061668a"
 *	    	    },
 *			    "signature" : "<?xml version=\"1.0\\" encoding=\"UTF-8\\"?><Root><DataForSign><Document><UID>d2544ea0-73fa-4e32-b42d-dc6cf060657e<\/UID><RegNumber>2013-1883\/14-0-0<\/RegNumber><RegDate>01.09.2014 00:00<\/RegDate><Recipient>Комитет по информатизации и связи<\/Recipient><Correspondent>Администрация Санкт-Петербурга<\/Correspondent><Annotation>dfgsdfg<\/Annotation><\/Document><Signer><SigningDate>01.09.2014 12:22<\/SigningDate><SignerUID>5cb91458-be0b-4cf5-94d1-28efc47b2f35<\/SignerUID><SignerName>Тяжева Ирина Ивановна<\/SignerName><SignerPosition>Главный специалист<\/SignerPosition><SignatureType>1<\/SignatureType><SignerLogin>tyazheva<\/SignerLogin><CertificateNumber><\/CertificateNumber><\/Signer><ApprovalVisaData><StationID>152632<\/StationID><AuthorUID>626becc9-d59f-465c-9eaa-369179d5f527<\/AuthorUID><AuthorName>Чамара Денис Петрович, Начальник управления<\/AuthorName><ControlDate>30.07.2014 23:59<\/ControlDate><Result\/><AttachmentUID\/><\/ApprovalVisaData><\/DataForSign><\/Root>"
 *		    }
 *	    ]
 *    }
</code></pre>
<h3>Пример 2 (старый вариант, отправка блобов в конце тела запроса)</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "gromov",
 *		    "password" : "11111",
 *		    "device" : "iOS",
 *		    "version" : "1.2.0"
 *	    },
 *	    "signedDocs" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "incomingStationId" : 160125,
 *	    	    "file" : {
 *		    	    "blobNo" : 1
 *	    	    },
 *			    "signature" : "<?xml version=\"1.0\\" encoding=\"UTF-8\\"?><Root><DataForSign><Document><UID>d2544ea0-73fa-4e32-b42d-dc6cf060657e<\/UID><RegNumber>2013-1883\/14-0-0<\/RegNumber><RegDate>01.09.2014 00:00<\/RegDate><Recipient>Комитет по информатизации и связи<\/Recipient><Correspondent>Администрация Санкт-Петербурга<\/Correspondent><Annotation>dfgsdfg<\/Annotation><\/Document><Signer><SigningDate>01.09.2014 12:22<\/SigningDate><SignerUID>5cb91458-be0b-4cf5-94d1-28efc47b2f35<\/SignerUID><SignerName>Тяжева Ирина Ивановна<\/SignerName><SignerPosition>Главный специалист<\/SignerPosition><SignatureType>1<\/SignatureType><SignerLogin>tyazheva<\/SignerLogin><CertificateNumber><\/CertificateNumber><\/Signer><ApprovalVisaData><StationID>152632<\/StationID><AuthorUID>626becc9-d59f-465c-9eaa-369179d5f527<\/AuthorUID><AuthorName>Чамара Денис Петрович, Начальник управления<\/AuthorName><ControlDate>30.07.2014 23:59<\/ControlDate><Result\/><AttachmentUID\/><\/ApprovalVisaData><\/DataForSign><\/Root>"
 *		    }
 *	    ],
 *	    "blobLengths" : [
 *		    120000
 *	    ]
 *    }<i>[бинарное содержимое документа]</i>
</code></pre>*/
class SignDocumentsRequest : RequestBase(), IDocumentActionsRequest<SignDocumentAction> {

    /** Список подписанных документов с сопроводительной метаинформацией.*/
    @SerializedName(SIGNED_DOCS)
    @Expose
    var signedDocs: List<SignDocumentAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref signedDocs.*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ на запрос на подписание документов.
Если операция все еще выполняется на сервере, то будет возвращена ошибка \link API.ErrorType API.ErrorType::OperationIsRunning\endlink.
\b Запрос: <see cref="SignDocumentsRequest"/>.
\b Примечание: Если вместе с ранее переданным УИДом операции будет передан другой объект <see cref="DataModel.Actions.SignDocumentAction"/>, то он не будет проанализирован,
а результат обработки операции будет содержать ту копию <see cref="DataModel.Actions.SignDocumentAction"/>, которая была передана при первой попытке обработать запрос.*/
class SignDocumentsResponse : DocsListResponseBase<SignDocumentsResponse>(),
    IDocumentActionsResponse<SignDocumentAction> {
    /** Список результатов подписания документов.*/
    @SerializedName(SIGNED_DOCS)
    @Expose
    var signedDocs: List<SignDocumentAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref signedDocs.*/
    override var documentActions: List<DocNoteAction>? = null
}

/**    ApproveDocuments  */

/** <see cref="API.IDocs"/>. Запрос на выполнение серии операций по согласованию документов (согласовать, вернуть на доработку).
b Ответ: <see cref="ApproveDocumentsResponse"/>.
<h3>Пример согласования</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "gromov",
 *		    "password" : "11111",
 *		    "device" : "iOS",
 *		    "version" : "1.2.0"
 *	    },
 *	    "approvalActions" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "incomingStationId" : 160125,
 *			    "actionType" : 1,
 *			    "resultText" : "Все хорошо - все замечания устранены.",
 *			    "resultCode" : 3,
 *			    "signature" : "<?xml version=\"1.0\\" encoding=\"UTF-8\\"?><Root><DataForSign><Document><UID>d2544ea0-73fa-4e32-b42d-dc6cf060657e<\/UID><RegNumber>2013-1883\/14-0-0<\/RegNumber><RegDate>01.09.2014 00:00<\/RegDate><Recipient>Комитет по информатизации и связи<\/Recipient><Correspondent>Администрация Санкт-Петербурга<\/Correspondent><Annotation>dfgsdfg<\/Annotation><\/Document><Signer><SigningDate>01.09.2014 12:22<\/SigningDate><SignerUID>5cb91458-be0b-4cf5-94d1-28efc47b2f35<\/SignerUID><SignerName>Тяжева Ирина Ивановна<\/SignerName><SignerPosition>Главный специалист<\/SignerPosition><SignatureType>1<\/SignatureType><SignerLogin>tyazheva<\/SignerLogin><CertificateNumber><\/CertificateNumber><\/Signer><ApprovalVisaData><StationID>152632<\/StationID><AuthorUID>626becc9-d59f-465c-9eaa-369179d5f527<\/AuthorUID><AuthorName>Чамара Денис Петрович, Начальник управления<\/AuthorName><ControlDate>30.07.2014 23:59<\/ControlDate><Result>Все хорошо - все замечания устранены.<\/Result><AttachmentUID\/><\/ApprovalVisaData><\/DataForSign><\/Root>"
 *		    }
 *	    ]
 *    }
</code></pre>
<h3>Пример удаления подмаршрута</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "gromov",
 *		    "password" : "11111",
 *		    "device" : "iOS",
 *		    "version" : "1.2.0"
 *	    },
 *	    "approvalActions" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "incomingStationId" : 160125,
 *			    "actionType" : 4
 *		    }
 *	    ]
 *    }
</code></pre>*/
class ApproveDocumentsRequest : RequestBase(), IDocumentActionsRequest<ApprovalStationAction> {

    /**писок документов для возврата на доработку.*/
    @SerializedName(APPROVAL_ACTIONS)
    @Expose
    var approvalActions: List<ApprovalStationAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref approvalActions.*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ на запрос на согласование документов.
Если операция все еще выполняется на сервере, то будет возвращена ошибка \link API.ErrorType API.ErrorType::OperationIsRunning\endlink.
\b Запрос: <see cref="ApproveDocumentsRequest"/>.
\b Примечание: Если вместе с ранее переданным УИДом операции будет передан другой объект <see cref="DataModel.Actions.ApprovalStationAction"/>,
то он не будет проанализирован, а результат обработки операции будет содержать ту копию <see cref="DataModel.Actions.ApprovalStationAction"/>,
которая была передана при первой попытке обработать запрос.*/
class ApproveDocumentsResponse : DocsListResponseBase<ApproveDocumentsResponse>(),
    IDocumentActionsResponse<ApprovalStationAction> {

    /** Список результатов возврата документов на доработку.*/
    @SerializedName(APPROVAL_ACTIONS)
    @Expose
    var approvalActions: List<ApprovalStationAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref approvalActions.*/
    override var documentActions: List<DocNoteAction>? = null
}
/**    #region RejectDocuments  */

/** <see cref="API.IDocs"/>. Устаревший запрос на возврат документов на доработку, вместо него нужно использовать <see cref="ApproveDocumentsRequest"/>.
Удалить когда не останется клиентов Android 1.2.0.
\b Ответ: <see cref="RejectDocumentsResponse"/>.
<h3>Пример</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "gromov",
 *		    "password" : "11111",
 *		    "device" : "iOS",
 *		    "version" : "1.2.0"
 *	    },
 *	    "rejectedDocs" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "incomingStationId" : 160125,
 *			    "resultText" : "Все переделать!",
 *			    "signature" : "<?xml version=\"1.0\\" encoding=\"UTF-8\\"?><Root><DataForSign><Document><UID>d2544ea0-73fa-4e32-b42d-dc6cf060657e<\/UID><RegNumber>2013-1883\/14-0-0<\/RegNumber><RegDate>01.09.2014 00:00<\/RegDate><Recipient>Комитет по информатизации и связи<\/Recipient><Correspondent>Администрация Санкт-Петербурга<\/Correspondent><Annotation>dfgsdfg<\/Annotation><\/Document><Signer><SigningDate>01.09.2014 12:22<\/SigningDate><SignerUID>5cb91458-be0b-4cf5-94d1-28efc47b2f35<\/SignerUID><SignerName>Тяжева Ирина Ивановна<\/SignerName><SignerPosition>Главный специалист<\/SignerPosition><SignatureType>1<\/SignatureType><SignerLogin>tyazheva<\/SignerLogin><CertificateNumber><\/CertificateNumber><\/Signer><ApprovalVisaData><StationID>152632<\/StationID><AuthorUID>626becc9-d59f-465c-9eaa-369179d5f527<\/AuthorUID><AuthorName>Чамара Денис Петрович, Начальник управления<\/AuthorName><ControlDate>30.07.2014 23:59<\/ControlDate><Result>Все переделать!<\/Result><AttachmentUID\/><\/ApprovalVisaData><\/DataForSign><\/Root>"
 *		    }
 *	    ]
 *    }
</code></pre>*/
class RejectDocumentsRequest : RequestBase(), IDocumentActionsRequest<ApprovalStationAction> {

    /** Список документов для возврата на доработку.*/
    @SerializedName(REJECTED_DOCS)
    @Expose
    var rejectedDosc: List<ApprovalStationAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref rejectedDocs.*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ на запрос на возврат документов на доработку.
Если операция все еще выполняется на сервере, то будет возвращена ошибка \link API.ErrorType API.ErrorType::OperationIsRunning\endlink.
\b Запрос: <see cref="RejectDocumentsRequest"/>.
\b Примечание: Если вместе с ранее переданным УИДом операции будет передан другой объект <see cref="DataModel.Actions.ApprovalStationAction"/>,
то он не будет проанализирован, а результат обработки операции будет содержать ту копию <see cref="DataModel.Actions.ApprovalStationAction"/>,
которая была передана при первой попытке обработать запрос.*/
class RejectDocumentsResponse : DocsListResponseBase<RejectDocumentsResponse>(),
    IDocumentActionsResponse<ApprovalStationAction> {

    /** Список результатов возврата документов на доработку.*/
    @SerializedName(REJECTED_DOCS)
    @Expose
    var rejectedDocs: List<ApprovalStationAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref rejectedDocs.*/
    override var documentActions: List<DocNoteAction>? = null
}

/**   МАРШРУТ РАССМОТРЕНИЯ  */

/**    Резолюции */

/** <see cref="API.IDocs"/>. Запрос на изменение резолюций (возможные действия см. в <see cref="DataModel.Actions.GroupedStationVerb"/>).
\b Ответ: <see cref="ChangeResolutionsResponse"/>.
<h3>Пример создания резолюции из одного исполнителя</h3>
<pre><code>
 *   	{
 *   		"user" : {
 *   			"login" : "chamara",
 *   			"password" : "11111",
 *   			"device" : "Android",
 *   			"version" : "1.1.6"
 *   		},
 *   		"groupedStations" : [{
 *   				"opUid" : "b9a8d1f9-b0be-4385-b35e-ecbbc7a01518",
 *   				"docUid" : "33839223-09df-45b6-9327-32bad14bba79",
 *   				"docType" : 16,
 *   				"incomingStationId" : 160204,
 *   				"signerUid" : "626becc9-d59f-465c-9eaa-369179d5f527",
 *   				"isControlled" : 0,
 *   				"isProject" : 0,
 *   				"isPersonalControl" : 0,
 *   				"importance" : 0,
 *   				"verb" : 2,
 *   				"signature" : "<?xml version=\"1.0\\" encoding=\"UTF-8\\"?><Root><DataForSign><Document><UID>33839223-09df-45b6-9327-32bad14bba79<\/UID><RegNumber>2013-1886\/14-0-0<\/RegNumber><RegDate>01.09.2014 00:00<\/RegDate><Recipient>Комитет по информатизации и связи<\/Recipient><Correspondent> Комитет по образованию<\/Correspondent><Annotation>тест групп<\/Annotation><\/Document><Signer><SigningDate>01.09.2014 18:22<\/SigningDate><SignerUID>626becc9-d59f-465c-9eaa-369179d5f527<\/SignerUID><SignerName>Платонов Вячеслав Владимирович<\/SignerName><SignerPosition>генеральный директор<\/SignerPosition><SignatureType>2<\/SignatureType><SignerLogin>chamara<\/SignerLogin><CertificateNumber>25e13681000100002ccf<\/CertificateNumber><CertificateExpirationDate>18.12.2014<\/CertificateExpirationDate><\/Signer><ResolutionData><ResolutionGroupsData><ResolutionGroupData><GroupNumber>1<\/GroupNumber><ResolutionText>TEST<\/ResolutionText><ExecutivesData><ExecutiveData><ExecutiveUID>24dcab3f-d9e2-49e7-a4aa-18739e2406a8<\/ExecutiveUID><ExecutiveName>Азарсков Алексей Вольдемарович, Первый заместитель председателя Комитета<\/ExecutiveName><IsMain>1<\/IsMain><AuthorUID>626becc9-d59f-465c-9eaa-369179d5f527<\/AuthorUID><AuthorName>Чамара Денис Петрович, Начальник управления<\/AuthorName><CreateDate>01.09.2014 18:22<\/CreateDate><\/ExecutiveData><\/ExecutivesData><\/ResolutionGroupData><\/ResolutionGroupsData><\/ResolutionData><\/DataForSign><Signature>MIILmgYJKoZIhvcNAQcCoIILizCCC4cCAQExDDAKBgYqhQMCAgkFADALBgkqhkiG9w0BBwGgggly\nMIIJbjCCCR2gAwIBAgIKJeE2gQABAAAszzAIBgYqhQMCAgMwggF6MRgwFgYFKoUDZAESDTEwOTc3\nNDYxODUxOTUxGjAYBggqhQMDgQMBARIMMDA3ODQxMDE2NjM2MTgwNgYDVQQJDC\/QodC10YDQtdCx\n0YDQtdC90L3QuNC60L7QstGB0LrQsNGPINGD0LsuLCDQtC4xNDEmMCQGCSqGSIb3DQEJARYXdWNA\ncjU0LmNlbnRlci1pbmZvcm0ucnUxCzAJBgNVBAYTAlJVMTUwMwYDVQQIDCw1NCDQndC+0LLQvtGB\n0LjQsdC40YDRgdC60LDRjyDQvtCx0LvQsNGB0YLRjDEiMCAGA1UEBwwZ0LMu0J3QvtCy0L7RgdC4\n0LHQuNGA0YHQujEoMCYGA1UECgwf0KTQk9Cj0J8g0KbQtdC90YLRgNCY0L3RhNC+0YDQvDExMC8G\nA1UECwwo0J3QstGB0KQg0KTQk9Cj0J8g0KbQtdC90YLRgNCY0L3RhNC+0YDQvDEbMBkGA1UEAxMS\nQ2VudGVyLUluZm9ybSBOdnNmMB4XDTEzMTIxODEwMDMwMFoXDTE0MTIxODEwMTMwMFowggI8MRYw\nFAYFKoUDZAMSCzAxMjM5MTg0OTI5MRgwFgYFKoUDZAESDTExMDk4NDcwMjM0NjExGjAYBggqhQMD\ngQMBARIMMDA3ODEzNDg4MzQ4MR4wHAYJKoZIhvcNAQkBFg9zbGF2YUBrb2Rla3MucnUxCzAJBgNV\nBAYTAlJVMS0wKwYDVQQIDCQ3OCDQsy4g0KHQsNC90LrRgi3Qn9C10YLQtdGA0LHRg9GA0LMxKjAo\nBgNVBAcMIdCzLiDQodCw0L3QutGCLdCf0LXRgtC10YDQsdGD0YDQszFGMEQGA1UECgw90J7QntCe\nICLQmtC+0LzQv9GM0Y7RgtC10YAg0JTQtdCy0LXQu9C+0L\/QvNC10L3RgiDQptC10L3RgtGAIjEK\nMAgGA1UECwwBMDFDMEEGA1UEAww60J\/Qu9Cw0YLQvtC90L7QsiDQktGP0YfQtdGB0LvQsNCyINCS\n0LvQsNC00LjQvNC40YDQvtCy0LjRhzEKMAgGA1UECQwBMDE+MDwGCSqGSIb3DQEJAgwvSU5OPTc4\nMTM0ODgzNDgvS1BQPTc4MTMwMTAwMS9PR1JOPTExMDk4NDcwMjM0NjExMDAuBgNVBAwMJ9Cz0LXQ\nvdC10YDQsNC70YzQvdGL0Lkg0LTQuNGA0LXQutGC0L7RgDEyMDAGA1UEKgwp0JLRj9GH0LXRgdC7\n0LDQsiDQktC70LDQtNC40LzQuNGA0L7QstC40YcxGTAXBgNVBAQMENCf0LvQsNGC0L7QvdC+0LIw\nYzAcBgYqhQMCAhMwEgYHKoUDAgIkAAYHKoUDAgIeAQNDAARADQKDSbUDx7tDUN47PyPwBD0DOPWg\n73AZEtbZoOU9Agf8uBkTTm1CXItgfBREiFqRukxupEfwp9yfNergUXdXTKOCBLswggS3MA4GA1Ud\nDwEB\/wQEAwIE8DB4BgNVHSUEcTBvBggrBgEFBQcDAgYIKwYBBQUHAwQGByqFAwICIgYGByqFAwYD\nAQEGCCqFAwYDAQMBBgUqhQMGAwYFKoUDBgcGCCqFAwYDAQIBBggqhQMGAwEEAQYIKoUDBgMBBAIG\nCCqFAwYDAQQDBgcqhQMDBgAMMB0GA1UdDgQWBBTytd0NeasPk7w5mHUfQGna76+zYTCCAZsGA1Ud\nIwSCAZIwggGOgBTZcujttPPnvks0pm7HkImZlQ7k9qGCAWikggFkMIIBYDEYMBYGBSqFA2QBEg0x\nMDk3NzQ2MTg1MTk1MRowGAYIKoUDA4EDAQESDDAwNzg0MTAxNjYzNjEoMCYGA1UECQwf0KjQv9Cw\n0LvQtdGA0L3QsNGPINGD0LsuLCDQtC4yODEiMCAGCSqGSIb3DQEJARYTY2FAY2VudGVyLWluZm9y\nbS5ydTELMAkGA1UEBhMCUlUxLTArBgNVBAgMJDc4INCTLiDQodCw0L3QutGCLdCf0LXRgtC10YDQ\nsdGD0YDQszEqMCgGA1UEBwwh0JMuINCh0LDQvdC60YIt0J\/QtdGC0LXRgNCx0YPRgNCzMSgwJgYD\nVQQKDB\/QpNCT0KPQnyDQptC10L3RgtGA0JjQvdGE0L7RgNC8MTAwLgYDVQQLDCfQo9C00L7RgdGC\n0L7QstC10YDRj9GO0YnQuNC5INGG0LXQvdGC0YAxFjAUBgNVBAMTDUNlbnRlci1JbmZvcm2CChXV\npf4AAAAAAIkwfwYDVR0fBHgwdjA8oDqgOIY2aHR0cDovL3I1NC5jZW50ZXItaW5mb3JtLnJ1L3Vj\nL2NlbnRyaW5mb3JtX252c2ZfdjMuY3JsMDagNKAyhjBodHRwOi8vY2EuY2k1NC5ydS9jYXVzZXIv\
 *nY2VudHJpbmZvcm1fbnZzZl92My5jcmwwgYMGCCsGAQUFBwEBBHcwdTAtBggrBgEFBQcwAYYhaHR0\ncDovL29jc3AuY2k1NC5ydS9vY3NwL29jc3Auc3JmMEQGCCsGAQUFBzAChjhodHRwOi8vcjU0LmNl\nbnRlci1pbmZvcm0ucnUvdWMvY2VudGVyLWluZm9ybV9udnNmKDEpLmNlcjArBgNVHRAEJDAigA8y\nMDEzMTIxODEwMDMwMFqBDzIwMTQxMjE4MTAwMzAwWjAdBgNVHSAEFjAUMAgGBiqFA2RxATAIBgYq\nhQNkcQIwNgYFKoUDZG8ELQwrItCa0YDQuNC\/0YLQvtCf0YDQviBDU1AiICjQstC10YDRgdC40Y8g\nMy42KTCB4QYFKoUDZHAEgdcwgdQMKyLQmtGA0LjQv9GC0L7Qn9GA0L4gQ1NQIiAo0LLQtdGA0YHQ\nuNGPIDMuNikMUyLQo9C00L7RgdGC0L7QstC10YDRj9GO0YnQuNC5INGG0LXQvdGC0YAgItCa0YDQ\nuNC\/0YLQvtCf0YDQviDQo9CmIiDQstC10YDRgdC40LggMS41DCfQodCkLzEyMS0xODU5INC+0YIg\nMTcg0LjRjtC90Y8gMjAxMiDQsy4MJ9Ch0KQvMTI4LTE4MjIg0L7RgiAwMSDQuNGO0L3RjyAyMDEy\nINCzLjAIBgYqhQMCAgMDQQAyXD3RTUWsYPCwLmSu1bmkaGAIETCthaFLVNjfVfRQlliCTnHY+tpJ\nb2XS76fONYVMoEt9+\/YT4VN\/YRdntM9zMYIB7zCCAesCAQEwggGKMIIBejEYMBYGBSqFA2QBEg0x\nMDk3NzQ2MTg1MTk1MRowGAYIKoUDA4EDAQESDDAwNzg0MTAxNjYzNjE4MDYGA1UECQwv0KHQtdGA\n0LXQsdGA0LXQvdC90LjQutC+0LLRgdC60LDRjyDRg9C7Liwg0LQuMTQxJjAkBgkqhkiG9w0BCQEW\nF3VjQHI1NC5jZW50ZXItaW5mb3JtLnJ1MQswCQYDVQQGEwJSVTE1MDMGA1UECAwsNTQg0J3QvtCy\n0L7RgdC40LHQuNGA0YHQutCw0Y8g0L7QsdC70LDRgdGC0YwxIjAgBgNVBAcMGdCzLtCd0L7QstC+\n0YHQuNCx0LjRgNGB0LoxKDAmBgNVBAoMH9Ck0JPQo9CfINCm0LXQvdGC0YDQmNC90YTQvtGA0Lwx\nMTAvBgNVBAsMKNCd0LLRgdCkINCk0JPQo9CfINCm0LXQvdGC0YDQmNC90YTQvtGA0LwxGzAZBgNV\nBAMTEkNlbnRlci1JbmZvcm0gTnZzZgIKJeE2gQABAAAszzAKBgYqhQMCAgkFADAKBgYqhQMCAhMF\nAARA8UUD9Z5rm\/62CZoKk1RX4s+6iWSe2HcOwb8yrvuxNhPk1Ta52ZfDS0DA5WETILkDFwio\/kMd\nEBIYyuk6Q87j\/w==<\/Signature><\/Root>",
 *				"groups" : [{
 *   						"id" : 1,
 *   						"resolution" : "TEST",
 *   						"controlDate" : "2014-09-09T23:59",
 *   						"isInformational" : 0,
 *   						"isAnswerRequired" : false,
 *   						"verb" : 2,
 *   						"executives" : [{
 *   								"uid" : "24dcab3f-d9e2-49e7-a4aa-18739e2406a8",
 *   								"stationId" : 0,
 *   								"isFixed" : 0,
 *   								"createDate" : "2014-09-01T18:22",
 *   								"isMain" : 1,
 *   								"verb" : 2
 *   							}
 *   						]
 *   					}
 *   				]
 *   			}
 *   		]
 *   	}
</code></pre>
<h3>Пример подписания подборки</h3>
<pre><code>
 *   	{
 *   		"user" : {
 *   			"login" : "sskag_poltavchenko",
 *   			"password" : "12345",
 *   			"device" : "Android",
 *   			"version" : "1.2.6"
 *   		},
 *   		"groupedStations" : [{
 *   				"opUid" : "b9a8d1f9-b0be-4385-b35e-ecbbc7a01518",
 *   				"docUid" : "33839223-09df-45b6-9327-32bad14bba79",
 *   				"docType" : 16,
 *   				"incomingStationId" : 160204,
 *   				"signerUid" : "626becc9-d59f-465c-9eaa-369179d5f527",
 *   				"isControlled" : 0,
 *   				"isProject" : 0,
 *   				"isPersonalControl" : 0,
 *   				"importance" : 0,
 *   				"verb" : 1,
 *   				"compilationId" : 2138,
 *   				"files": [{
 *   						"blobNo" : 1,
 *   						"name" : "Резолюция руководителя.pdf"
 *   				}],
 *   				"groups" : [{
 *   						"id" : 1,
 *   						"resolution" : "TEST",
 *   						"controlDate" : "2014-09-09T23:59",
 *   						"isInformational" : 0,
 *   						"isAnswerRequired" : false,
 *   						"verb" : 4,
 *   						"executives" : [{
 *   								"uid" : "24dcab3f-d9e2-49e7-a4aa-18739e2406a8",
 *   								"stationId" : 0,
 *   								"isFixed" : 0,
 *   								"createDate" : "2014-09-01T18:22",
 *   								"isMain" : 1,
 *   								"verb" : 1
 *   							}
 *   						]
 *   					}
 *   				]
 *   			}
 *   		]
 *   	}
</code></pre>*/
class ChangeResolutionsRequest : RequestBase(), IDocumentActionsRequest<GroupedStationAction> {

    /** Список резолюций для создания/сохранения/утверждения.*/
    @SerializedName(GROUPED_STATIONS)
    @Expose
    var groupedStations: List<GroupedStationAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref groupedStations.*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ на запрос на изменение резолюций. По всем документам возвращается их актуальная версия,
/// а результирующий список резолюций содержит результаты обработки (отсутствие ошибок означает успех операции).
/// Если операция все еще выполняется на сервере, то будет возвращена ошибка \link API.ErrorType API.ErrorType::OperationIsRunning\endlink.
/// \b Запрос: <see cref="ChangeResolutionsRequest"/>.
/// \b Примечание: Если вместе с ранее переданным УИДом операции будет передан другой объект <see cref="GroupedStationAction"/>, то он не будет проанализирован,
/// а результат обработки операции будет содержать ту копию <see cref="GroupedStationAction"/>, которая была передана при первой попытке обработать запрос.*/
class ChangeResolutionsResponse : DocsListResponseBase<ChangeResolutionsResponse>(),
    IDocumentActionsResponse<GroupedStationAction> {

    /**Список результатов изменения в резолюциях, внедренных в данные, которые пришли на сервер.*/
    @SerializedName(GROUPED_STATIONS)
    @Expose
    var groupedStations: List<GroupedStationAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref groupedStations.*/
    override var documentActions: List<DocNoteAction>? = null
}

/**   ОТЧЕТЫ  */

/** <see cref="API.IDocs"/>. Запрос на изменение отчетов по резолюциям (возможные действия см. в <see cref="DataModel.Actions.ResolutionReportStationVerb"/>).
\b Ответ: <see cref="ChangeResolutionReportsResponse"/>.
<h3>Пример 1: создание отчета</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "tyazheva",
 *		    "password" : "11111",
 *		    "device" : "Android",
 *		    "version" : "1.1.6"
 *	    },
 *	    "reportStations" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "incomingStationId" : 160125,
 *			    "receiverUid" : "b317f587-0b3a-4e35-b3bb-9b70f639bbaa",
 *			    "resolution" : "Принято к сведению, учтено в ответе заявителю",
 *			    "signerUid" : "5cb91458-be0b-4cf5-94d1-28efc47b2f35",
 *			    "createDate" : "2014-09-01T12:22:00",
 *			    "verb" : 2,
 *			    "signature" : "<?xml version=\"1.0\\" encoding=\"UTF-8\\"?><Root><DataForSign><Document><UID>d2544ea0-73fa-4e32-b42d-dc6cf060657e<\/UID><RegNumber>2013-1883\/14-0-0<\/RegNumber><RegDate>01.09.2014 00:00<\/RegDate><Recipient>Комитет по информатизации и связи<\/Recipient><Correspondent>Администрация Санкт-Петербурга<\/Correspondent><Annotation>dfgsdfg<\/Annotation><\/Document><Signer><SigningDate>01.09.2014 12:22<\/SigningDate><SignerUID>5cb91458-be0b-4cf5-94d1-28efc47b2f35<\/SignerUID><SignerName>Тяжева Ирина Ивановна<\/SignerName><SignerPosition>Главный специалист<\/SignerPosition><SignatureType>1<\/SignatureType><SignerLogin>tyazheva<\/SignerLogin><CertificateNumber><\/CertificateNumber><\/Signer><ReportData><ResolutionText>Принято к сведению, учтено в ответе заявителю<\/ResolutionText><ReceiverUID>b317f587-0b3a-4e35-b3bb-9b70f639bbaa<\/ReceiverUID><ReceiverName>Громов Иван Александрович, Председатель Комитета<\/ReceiverName><AuthorUID>5cb91458-be0b-4cf5-94d1-28efc47b2f35<\/AuthorUID><AuthorName>Тяжева Ирина Ивановна, Главный специалист<\/AuthorName><CreateDate>01.09.2014 12:22<\/CreateDate><\/ReportData><\/DataForSign><\/Root>"
 *		    }
 *	    ]
 *    }
</code></pre>
<h3>Пример 2: изменение отчета</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "tyazheva",
 *		    "password" : "11111",
 *		    "device" : "Android",
 *		    "version" : "1.1.6"
 *	    },
 *	    "reportStations" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "id" : 160126,
 *			    "incomingStationId" : 160125,
 *			    "resolution" : "Принято к сведению, учтено в ответе заявителю",
 *			    "verb" : 1,
 *			    "signature" : "<?xml version=\"1.0\\" encoding=\"UTF-8\\"?><Root><DataForSign><Document><UID>d2544ea0-73fa-4e32-b42d-dc6cf060657e<\/UID><RegNumber>2013-1883\/14-0-0<\/RegNumber><RegDate>01.09.2014 00:00<\/RegDate><Recipient>Комитет по информатизации и связи<\/Recipient><Correspondent>Администрация Санкт-Петербурга<\/Correspondent><Annotation>dfgsdfg<\/Annotation><\/Document><Signer><SigningDate>01.09.2014 12:22<\/SigningDate><SignerUID>5cb91458-be0b-4cf5-94d1-28efc47b2f35<\/SignerUID><SignerName>Тяжева Ирина Ивановна<\/SignerName><SignerPosition>Главный специалист<\/SignerPosition><SignatureType>1<\/SignatureType><SignerLogin>tyazheva<\/SignerLogin><CertificateNumber><\/CertificateNumber><\/Signer><ReportData><ResolutionText>Принято к сведению, учтено в ответе заявителю<\/ResolutionText><ReceiverUID>b317f587-0b3a-4e35-b3bb-9b70f639bbaa<\/ReceiverUID><ReceiverName>Громов Иван Александрович, Председатель Комитета<\/ReceiverName><AuthorUID>5cb91458-be0b-4cf5-94d1-28efc47b2f35<\/AuthorUID><AuthorName>Тяжева Ирина Ивановна, Главный специалист<\/AuthorName><CreateDate>01.09.2014 12:22<\/CreateDate><\/ReportData><\/DataForSign><\/Root>"
 *		    }
 *	    ]
 *    }
</code></pre>
<h3>Пример 3: принятие отчета с закрытием входящей инстанции</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "tyazheva",
 *		    "password" : "11111",
 *		    "device" : "Android",
 *		    "version" : "1.1.6"
 *	    },
 *	    "reportStations" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "id" : 160126,
 *			    "incomingStationId" : 160125,
 *			    "verb" : 3
 *		    }
 *	    ]
 *    }
</code></pre>
<h3>Пример 4: отклонение отчета</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "tyazheva",
 *		    "password" : "11111",
 *		    "device" : "Android",
 *		    "version" : "1.1.6"
 *	    },
 *	    "reportStations" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "id" : 160126,
 *			    "incomingStationId" : 160125,
 *			    "rejectReason" : "Переделать параграф 3",
 *			    "verb" : 5
 *		    }
 *	    ]
 *    }
</code></pre>*/
class ChangeResolutionReportsRequest : RequestBase(),
    IDocumentActionsRequest<ResolutionReportStationAction> {

    /**Список отчетов для изменения.*/
    @SerializedName(REPORT_STATIONS)
    @Expose
    var reports: List<ResolutionReportStationAction>? = null

    /**Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref reportStations.*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ на запрос на изменение отчетов. По всем документам возвращается их актуальная версия,
а результирующий список резолюций содержит результаты обработки (отсутствие ошибок означает успех операции).
Если операция все еще выполняется на сервере, то будет возвращена ошибка \link API.ErrorType API.ErrorType::OperationIsRunning\endlink.
\b Запрос: <see cref="ChangeResolutionReportsRequest"/>.
\b Примечание: Если вместе с ранее переданным УИДом операции будет передан другой объект <see cref="DataModel.Actions.ResolutionReportStationAction"/>, то он не будет проанализирован,
а результат обработки операции будет содержать ту копию <see cref="DataModel.Actions.ResolutionReportStationAction"/>, которая была передана при первой попытке обработать запрос.*/
class ChangeResolutionReportsResponse : DocsListResponseBase<ChangeResolutionReportsResponse>(),
    IDocumentActionsResponse<ResolutionReportStationAction> {

    /** Список результатов изменения в резолюциях, внедренных в данные, которые пришли на сервер.*/
    @SerializedName("reportStations")
    @Expose
    var reports: List<ResolutionReportStationAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref reportStations.*/
    override var documentActions: List<DocNoteAction>? = null
}

/**   АРХИВИРОВАНИЕ ДОКУМЕНТОВ   */

/** <see cref="API.IDocs"/>. Запрос на ознакомление с входящими резолюциями для информирования (происходит архивирование указанных инстанций).
\b Ответ: <see cref="ArchiveDocumentsResponse"/>.
<h3>Пример</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "chamara",
 *		    "password" : "11111",
 *		    "device" : "Android",
 *		    "version" : "1.2.1"
 *	    },
 *	    "stationsToArchive" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "incomingStationId" : 160125,
 *		    }
 *	    ]
 *    }
</code></pre>*/
class ArchiveDocumentsRequest : RequestBase(), IDocumentActionsRequest<ArchiveDocumentAction> {

    /** Список инстанций для ознакомления.*/
    @SerializedName("stationsToArchive")
    @Expose
    var stationsToArchive: List<ArchiveDocumentAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref stationsToArchive.*/
    override var documentActions: List<DocNoteAction>? = null
}

class SaveErrandRequest : RequestBase(), IDocumentActionsRequest<ArchiveDocumentAction> {

    /** Список инстанций для ознакомления.*/
    @SerializedName("SaveErrand")
    @Expose
    var stationsToArchive: List<ArchiveDocumentAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref stationsToArchive.*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ на запрос на ознакомление с резолюциями для информирования. По всем документам возвращается их актуальная версия,
а результирующий список резолюций содержит результаты обработки (отсутствие ошибок означает успех операции).
Если операция все еще выполняется на сервере, то будет возвращена ошибка \link API.ErrorType API.ErrorType::OperationIsRunning\endlink.
\b Запрос: <see cref="ArchiveDocumentsRequest"/>.
\b Примечание: Если вместе с ранее переданным УИДом операции будет передан другой объект <see cref="DataModel.Actions.ArchiveDocumentAction"/>, то он не будет проанализирован,
а результат обработки операции будет содержать ту копию <see cref="DataModel.Actions.ArchiveDocumentAction"/>, которая была передана при первой попытке обработать запрос.*/
class ArchiveDocumentsResponse : DocsListResponseBase<ArchiveDocumentsResponse>(),
    IDocumentActionsResponse<ArchiveDocumentAction> {

    /** Список результатов изменения в резолюциях, внедренных в данные, которые пришли на сервер.*/
    @SerializedName("stationsToArchive")
    @Expose
    var stationsToArchive: List<ArchiveDocumentAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref stationsToArchive.*/
    override var documentActions: List<DocNoteAction>? = null
}

/**    ЛИЧНЫЙ КОНТРОЛЬ  */

/** <see cref="API.IDocs"/>. Запрос на изменение статусов личного контроля.
\b Ответ: <see cref="PersonalControlChangeResponse"/>.
<h3>Пример снятия документа с личного контроля</h3>
<pre><code>
 *    {
 *	    "user" : {
 *		    "login" : "gromov",
 *		    "password" : "11111",
 *		    "device" : "Android",
 *		    "version" : "1.2.6"
 *	    },
 *	    "docActions" : [{
 *			    "opUid" : "8627d690-aab2-4b9f-b682-6b2266b22f52",
 *			    "docUid" : "d2544ea0-73fa-4e32-b42d-dc6cf060657e",
 *			    "docType" : 16,
 *			    "verb" : 1
 *		    }
 *	    ]
 *    }
</code></pre>*/
class PersonalControlChangeRequest : RequestBase(), IDocumentActionsRequest<PersonalControlAction> {

    /** Список документов для изменения статуса личного контроля.*/
    @SerializedName("docActions")
    @Expose
    var docActions: List<PersonalControlAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref docActions.*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ на запрос на изменение статусов личного контроля. По всем документам возвращается их актуальная версия,
а результирующий список резолюций содержит результаты обработки (отсутствие ошибок означает успех операции).
Если операция все еще выполняется на сервере, то будет возвращена ошибка \link API.ErrorType API.ErrorType::OperationIsRunning\endlink.
\b Запрос: <see cref="PersonalControlChangeRequest"/>.
\b Примечание: Если вместе с ранее переданным УИДом операции будет передан другой объект <see cref="DataModel.Actions.PersonalControlAction"/>, то он не будет проанализирован,
а результат обработки операции будет содержать ту копию <see cref="DataModel.Actions.PersonalControlAction"/>, которая была передана при первой попытке обработать запрос.*/

class PersonalControlChangeResponse : DocsListResponseBase<PersonalControlChangeResponse>(),
    IDocumentActionsResponse<PersonalControlAction> {

    /** Список результатов изменения в резолюциях, внедренных в данные, которые пришли на сервер.*/
    @SerializedName("docActions")
    @Expose
    var docActions: List<PersonalControlAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref docActions.*/
    override var documentActions: List<DocNoteAction>? = null
}

class SaveDocRequest : RequestBase(), IDocumentActionsRequest<ErrandAction> {

    /** Список действий с ОП документом.*/
    @SerializedName("docErrandAction")
    @Expose
    var errandAction: List<ErrandAction>? = null

    /** Реализация интерфейса \b IDocumentActionsRequest<T>, зеркало \ref  .*/
    override var documentActions: List<DocNoteAction>? = null
}

/** <see cref="API.IDocs"/>. Ответ с результатами изменения пометок вместе с обновленной полной версией всех участвующих в операции документов.
\b Запрос: <see cref="ChangeDocNotesRequest"/>.*/
class SaveDocResponse : ResponseBase<SaveDocResponse>(), IDocumentActionsResponse<ErrandAction> {

    /** Калька запроса, в которой заполнен результат выполнения каждой операции над ОП.*/
    @SerializedName("docErrandAction")
    @Expose
    var errandAction: List<ErrandAction>? = null

    /** Реализация интерфейса \b IDocumentActionsResponse<T>, зеркало \ref docNoteActions.*/
    override var documentActions: List<DocNoteAction>? = null
}