package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** ИД документа (УИД + тип).*/
class DocId {
    /** Уникальный идентификатор документа*/
    @SerializedName("u")
    @Expose
    var uid: String? = null//Guid

    /** Идентификатор глобального типа документа. Справочник \link Classifiers Classifiers::DOCTYPE\endlink.*/
    @SerializedName("t")
    @Expose
    var docType: String? = null
}

/** Общий контейнер для всех типов документов.*/
class Document : ObjectBase() {

    /** Уникальный идентификатор документа */
    @SerializedName("uid")
    @Expose
    var uid: String? = null//Guid

    /** Идентификатор глобального типа документа. Справочник \link Classifiers Classifiers::DOCTYPE\endlink.*/
    @SerializedName("docType")
    @Expose
    var docType: Int? = null

    /** Вид документа, может быть пустым. Справочник зависит от типа документа:
    - Входящие - \link Classifiers Classifiers::IncomingDocTypes\endlink
    - Исходящие - \link Classifiers Classifiers::OutgoingDocTypes\endlink
    - ОРД - \link Classifiers Classifiers::DECREE_TYPE\endlink
    - Внутренние - \link Classifiers Classifiers::DIRECTIVETYPE\endlink
    - Обращения - \link Classifiers Classifiers::VIDOBR\endlink
    - Договоры - \link Classifiers Classifiers::ContractKind\endlink*/
    @SerializedName("categoryId")
    @Expose
    var categoryId: Int? = null

    /** Автор документа*/
    @SerializedName("author")
    @Expose
    var authorUid: String? = null //Guid


    /** Наименование, краткое содержание документа*/
    @SerializedName("name")
    @Expose
    var name: String? = null

    /** Регистрационный номер документа*/
    @SerializedName("regNum")
    @Expose
    var regNum: String? = null

    /** Числовое значение регистрационного номера*/
    @SerializedName("regNo")
    @Expose
    var regNo: Int? = null

    /** Дата регистрации документа*/
    @SerializedName("regDate")
    @Expose
    var regiDate: String? = null //DateTime

    /** Примечание документа*/
    @SerializedName("remark")
    @Expose
    var remark: String? = null

    /** Данные штрих кода*/
    @SerializedName("barcode")
    @Expose
    var barcode: String? = null

    /** Номер документа по канцелярии*/
    @SerializedName("docNum")
    @Expose
    var docNum: String? = null

    /** Дата документа*/
    @SerializedName("docDate")
    @Expose
    var docDate: String? = null// DateTime

    /** Тип сотрудника, передавшего документ, см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("deliveryType")
    @Expose
    var deliveryType: Int? = null //Byte

    /** Идентификатор сотрудника, передавшего документ*/
    @SerializedName("deliveryUid")
    @Expose
    var deliveryUid: String? = null //Guid

    /** Сотрудник, передавший документ */
    @SerializedName("deliverySubject")
    @Expose
    var deliverySubject: String? = null

    /** Тип субъекта, подготовившего документ, см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("workerType")
    @Expose
    var workerType: Int? = null

    /** Идентификатор субъекта, подготовившего документ (Кто готовил документ)*/
    @SerializedName("workerUid")
    @Expose
    var workerUid: String? = null //Guid

    /** Имя субъекта, подготовившего документ*/
    @SerializedName("workerName")
    @Expose
    var workerName: String? = null

    /** Тип адресата, см. @see cref="CorrespondentTypes"/>*/
    @SerializedName("recipientType")
    @Expose
    var recipientType: Int? = null //Byte

    /** Идентификатор адресата*/
    @SerializedName("recipientUid")
    @Expose
    var recipientUid: String? = null //Guid

    /** Имя адресата*/
    @SerializedName("recipientSubject")
    @Expose
    var recipientSubject: String? = null

    /** Тип корреспондента, см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("corrType")
    @Expose
    var corrType: Int? = null //Byte

    /** Идентификатор корреспондента*/
    @SerializedName("corrUid")
    @Expose
    var corrUid: String? = null //Guid

    /** Имя корреспондента*/
    @SerializedName("corrName")
    @Expose
    var corrName: String? = null

    /** Тип лица, подписавшего документ, см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("signerType")
    @Expose
    var signerType: String? = null //Byte

    /** Идентификатор лица, подписавшего документ*/
    @SerializedName("signerUid")
    @Expose
    var signerUid: String? = null//Guid

    /** Имя лица, подписавшего документ*/
    @SerializedName("signerName")
    @Expose
    var signerName: String? = null

    //<editor-fold desc="Маршрут рассмотрения" defaultstate="collapsed">

    /** Статус маршрута рассмотрения, см. <see cref="ConsiderationRouteStates"/>.*/
    @SerializedName("routeState")
    @Expose
    var routeState: Int? = null//Byte

    /** Контрольный срок по документу*/
    @SerializedName("controlDate")
    @Expose
    var ControlDate: String? = null//DateTime

    /** Контрольная длительность, в минутах*/
    @SerializedName("controlDuration")
    @Expose
    var controlDuration: Int? = null

    /** Дата инициализации*/
    @SerializedName("startDate")
    @Expose
    var startDate: String? = null//DateTime

    /** Дата снятия с контроля*/
    @SerializedName("closingDate")
    @Expose
    var closingDate: String? = null//DateTime

    /** Срочность.*/
    @SerializedName("urgencyType")
    @Expose
    var urgencyType: Int? = null

    /** Признак контроля*/
    @SerializedName("isControlled")
    @Expose
    var isControlled: Boolean? = null

    /** Контролер исполнения документа*/
    @SerializedName("controllerUid")
    @Expose
    var controllerUid: String? = null//Guid

    /** Признак "Для информирования"*/
    @SerializedName("isInformational")
    @Expose
    var isInformational: Boolean? = null

    /** Признак "Учитывать только рабочие дни"*/
    @SerializedName("workDaysOnly")
    @Expose
    var workDaysOnly: Boolean? = null

    /** Идентификатор документа, закрывшего маршрут*/
    @SerializedName("closingDocUid")
    @Expose
    var closingDocUid: String? = null//Guid

    //</editor-fold>

    //<editor-fold desc="Поля обращения" defaultstate="collapsed">
    /** Идентификатор заявителя*/
    @SerializedName("applicantUid")
    @Expose
    var applicantUid: String? = null//Guid

    /** Созаявители*/
    @SerializedName("applicants")
    @Expose
    var applicants: Array<String>? = null//Guid[]

    /** Форма обращения. Справочник \link Classifiers Classifiers::FORMOBR\endlink.*/
    @SerializedName("approachForm")
    var approachForm: Int? = null

    /** Вид обращения - ПОЛЕ УСТАРЕЛО, вместо него данные нужно считывать из \ref categoryId.*/
    @SerializedName("approachType")
    @Expose
    @Deprecated("ПОЛЕ УСТАРЕЛО, вместо него данные нужно считывать из categoryId.")
    var approachType: Int? = null

    /**Тематики. Справочник \link Classifiers Classifiers::ApproachThems\endlink - он так называется в БД!*/
    @SerializedName("themes")
    @Expose
    var themes: Array<Int>? = null//int[]

    /** Тематика обращения, она же - Президентская тематика. Справочник \link Classifiers Classifiers::AppealThemes\endlink.*/
    @SerializedName("themeId")
    @Expose
    var themeId: Int? = null

    /** Тип адресата документа (Куда адресовал заявитель), см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("submittedToType")
    @Expose
    var ubmittedToTypeRaw: Int? = null//Byte

    /** Организация адресата документа (Куда адресовал заявитель)*/
    @SerializedName("submittedToUid")
    @Expose
    var SubmittedToUid: String? = null//Guid

    /** Имя адресата (Куда адресовал заявитель)*/
    @SerializedName("submittedToPerson")
    @Expose
    var submittedToPerson: String? = null

    /** Исходящий номер документа (заявитель)*/
    @SerializedName("submittedToDocNum")
    @Expose
    var submittedToOutDocNum: String? = null

    /** Исходящая дата документа (заявитель)*/
    @SerializedName("submittedToDate")
    @Expose
    var SubmittedToOutDate: String? = null//DateTime

    /** Тип решения ответа на обращение. Справочник \link Classifiers Classifiers::RTA_Resolution\endlink.*/
    @SerializedName("rtaResolution")
    @Expose
    var rtaResolution: Int? = null

    /** Объект/Место решения ответа на обращение. Справочник \link Classifiers Classifiers::RTA_Location\endlink.*/
    @SerializedName("rtaLocation")
    @Expose
    var rtaLocation: Int? = null

    /** Причина решения ответа на обращение. Справочник \link Classifiers Classifiers::RTA_Reason\endlink.*/
    @SerializedName("rtaReason")
    @Expose
    var rtaReason: Int? = null

    /**Различные флаги обращения (битовая маска).
    <h3>Значение битов</h3>
    - 1 - IsCollective - Признак коллективного обращения
    - 2 - IsPresident - Обращения президента
    - 4 - IsReviewLocal - Рассмотренно совместно с органами местного самоуправления
    - 8 - IsReviewFedOrg - Рассмотренно совместно с территориальными органами федеральных органов исполнительной власти
    - 16 - IsPunished - Виновные в нарушении прав граждан наказаны
    - 32 - IsLawsuits - Судебные иски по жалобам о нарушении прав авторов при рассмотрении обращений
    - 64 - IsMeasuresTaken - Меры приняты (Результативность по рассмотренным и направленным по компетенции обращениям за отчётный период)
    - 128 - IsReported - Доложено руководителю высшего исполнительного органа государственной власти субъекта Российской Федерации
    - 256 - IsCollegial - Рассмотренно коллегиально*/
    @SerializedName("approachFlags")
    @Expose
    var approachFlags: Int? = null

    /** Идентификатор окончательного ответа*/
    @SerializedName("finalAnswerUid")
    @Expose
    var finalAnswerUid: String? = null//Guid

    //</editor-fold>

    //<editor-fold desc="Поля оперативного поручения" defaultstate="collapsed">
    /** Важность*/
    @SerializedName("importanceType")
    @Expose
    var importanceType: Int? = null

    //</editor-fold>

    /** Сиквенс документа*/
    @SerializedName("sequence")
    @Expose
    var sequence: Long? = null//Long

    //<editor-fold desc="Associated objects" defaultstate="collapsed">

    /** Связи с виджетами*/
    @SerializedName("widgets")
    @Expose
    var widgetLinks: List<DocumentWidgetLink>? = null

    /** Связи с документами*/
    @SerializedName("links")
    @Expose
    var links: List<DocumentLink>? = null

    //</editor-fold>

    /** Метаинформация по вложениям, без содержимого. Главное вложение всегда первое в списке.*/
    @SerializedName("attachments")
    @Expose
    var attachments: List<FileAttachment>? = null

    /** Инстанции рассмотрения (записи из таблицы \b WF4_Stations под маршрутом с типом 1 в таблице \b WF4_Routes)*/
    @SerializedName("considerationStations")
    @Expose
    var considerationStations: List<ConsiderationStation>? = null

    /** Маршруты согласования (маршруты с типом 2 в таблице \b WF4_Routes). Инстанции согласования (\b WF4_Stations) лежат внутри этапов согласования (\b WF4_Stages).*/
    @SerializedName("approvalRoutes")
    @Expose
    var approvalRoutes:List<ApprovalRoute> ? = null

    /** Пометки */
    @SerializedName("notes")
    @Expose
    var notes: List<DocNote>? = null

    //<editor-fold desc="" defaultstate="collapsed">

    /** докумен основание*/
    @SerializedName("baseDocInfo")
    @Expose
    var baseDocInfo:String? = null

    /** заголовок документа*/
    @SerializedName("baseDocRemark")
    @Expose
    var baseDocRemark: String? = null

    /** основной исполнитель*/
    @SerializedName("mainExecutiveInfo")
    @Expose
    var sainExecutiveInfo: String? = null
//</editor-fold>
}

//<editor-fold desc="Found document" defaultstate="collapsed">
/** Контейнер для информации по найденным документам.*/
class FoundDocument : ObjectBase() {

    /** Уникальный идентификатор документа*/
    @SerializedName("uid")
    @Expose
    var uid: String? = null//Guid

    /** Идентификатор глобального типа документа. Справочник \link Classifiers Classifiers::DOCTYPE\endlink.*/
    @SerializedName("docType")
    @Expose
    var docType: Int? = null

    /** Название вида документа, используется только в результатах поиска!*/
    @SerializedName("categoryName")
    @Expose
    var categoryName: String? = null

    /** Автор документа (кто подготовил)*/
    @SerializedName("authorName")
    @Expose
    var authorName: String? = null

    /** Наименование, краткое содержание документа*/
    @SerializedName("name")
    @Expose
    var name: String? = null

    /** Регистрационный номер документа*/
    @SerializedName("regNum")
    @Expose
    var regNum: String? = null

    /** Дата регистрации документа*/
    @SerializedName("regDate")
    @Expose
    var regDate: String? = null//DateTime

    /** Номер документа по канцелярии*/
    @SerializedName("docNum")
    @Expose
    var docNum: String? = null

    /** Признак контроля (заполняется только у входящих)*/
    @SerializedName("isControlled")
    @Expose
    var isControlled: Boolean? = null

    /** Дата документа*/
    @SerializedName("docDate")
    @Expose
    var docDate: String? = null//DateTime

    /** Имя корреспондента (Откуда для входящих и Куда для исходящих)*/
    @SerializedName("corrName")
    @Expose
    var corrName: String? = null

    /** Имя лица, подписавшего документ*/
    @SerializedName("signerName")
    @Expose
    var signerName: String? = null

    /** Сиквенс документа (пока не заполняется, зарезервировано на будущее)*/
    @SerializedName("sequence")
    @Expose
    var sequence: Long? = null

    //</editor-fold>
}
