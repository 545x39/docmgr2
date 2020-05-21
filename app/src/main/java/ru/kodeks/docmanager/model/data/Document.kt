package ru.kodeks.docmanager.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.db.typeconverter.IntListToStringTypeConverter

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
@Entity(tableName = "documents", primaryKeys = ["uid"])
@TypeConverters(IntListToStringTypeConverter::class)
class Document(
    /** Уникальный идентификатор документа */
    @SerializedName("uid")
    @Expose
    var uid: String = "",
    /** Идентификатор глобального типа документа. Справочник \link Classifiers Classifiers::DOCTYPE\endlink.*/
    @SerializedName("docType")
    @ColumnInfo(name = "doc_type")
    @Expose
    var docType: Int? = null,
    /** Вид документа, может быть пустым. Справочник зависит от типа документа:*/
    @SerializedName("categoryId")
    @Expose
    @ColumnInfo(name = "category_id")
    var categoryId: Int? = null,
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
    @ColumnInfo(name = "reg_num")
    var regNum: String? = null,
    /** Числовое значение регистрационного номера*/
    @SerializedName("regNo")
    @Expose
    @ColumnInfo(name = "reg_no")
    var regNo: Int? = null,
    /** Дата регистрации документа*/
    @SerializedName("regDate")
    @Expose
    @ColumnInfo(name = "reg_date")
    var regDate: String? = null,
    /** Примечание документа*/
    @SerializedName("remark")
    @Expose
    var remark: String? = null,
    /** Данные штрих кода*/
    @SerializedName("barcode")
    @Expose
    var barcode: String? = null,
    /** Номер документа по канцелярии*/
    @SerializedName("docNum")
    @Expose
    @ColumnInfo(name = "doc_num")
    var docNum: String? = null,
    /** Дата документа*/
    @SerializedName("docDate")
    @Expose
    @ColumnInfo(name = "doc_date")
    var docDate: String? = null,
    /** Тип сотрудника, передавшего документ, см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("deliveryType")
    @Expose
    @ColumnInfo(name = "delivery_type")
    var deliveryType: Int? = null,
    /** Идентификатор сотрудника, передавшего документ*/
    @SerializedName("deliveryUid")
    @Expose
    @ColumnInfo(name = "delivery_uid")
    var deliveryUid: String? = null,
    /** Сотрудник, передавший документ */
    @SerializedName("deliverySubject")
    @Expose
    var deliverySubject: String? = null,
    /** Тип субъекта, подготовившего документ, см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("workerType")
    @Expose
    @ColumnInfo(name = "worker_type")
    var workerType: Int? = null,
    /** Идентификатор субъекта, подготовившего документ (Кто готовил документ)*/
    @SerializedName("workerUid")
    @Expose
    @ColumnInfo(name = "worker_uid")
    var workerUid: String? = null,
    /** Имя субъекта, подготовившего документ*/
    @SerializedName("workerName")
    @Expose
    @ColumnInfo(name = "worker_name")
    var workerName: String? = null,
    /** Тип адресата, см. @see cref="CorrespondentTypes"/>*/
    @SerializedName("recipientType")
    @Expose
    @ColumnInfo(name = "recipient_type")
    var recipientType: Int? = null,
    /** Идентификатор адресата*/
    @SerializedName("recipientUid")
    @Expose
    @ColumnInfo(name = "recipient_uid")
    var recipientUid: String? = null,
    /** Имя адресата*/
    @SerializedName("recipientSubject")
    @Expose
    @ColumnInfo(name = "recipient_subject")
    var recipientSubject: String? = null,
    /** Тип корреспондента, см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("corrType")
    @Expose
    @ColumnInfo(name = "correspondent_type")
    var corrType: Int? = null,
    /** Идентификатор корреспондента*/
    @SerializedName("corrUid")
    @Expose
    @ColumnInfo(name = "correspondent_uid")
    var corrUid: String? = null,
    /** Имя корреспондента*/
    @SerializedName("corrName")
    @Expose
    @ColumnInfo(name = "correspondent_name")
    var corrName: String? = null,
    /** Тип лица, подписавшего документ, см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("signerType")
    @Expose
    @ColumnInfo(name = "signer_type")
    var signerType: String? = null,
    /** Идентификатор лица, подписавшего документ*/
    @SerializedName("signerUid")
    @Expose
    @ColumnInfo(name = "signer_uid")
    var signerUid: String? = null,
    /** Имя лица, подписавшего документ*/
    @SerializedName("signerName")
    @Expose
    @ColumnInfo(name = "signer_name")
    var signerName: String? = null,
    //<editor-fold desc="Маршрут рассмотрения" defaultstate="collapsed">
    /** Статус маршрута рассмотрения, см. <see cref="ConsiderationRouteStates"/>.*/
    @SerializedName("routeState")
    @Expose
    @ColumnInfo(name = "route_state")
    var routeState: Int? = null,
    /** Контрольный срок по документу*/
    @SerializedName("controlDate")
    @Expose
    @ColumnInfo(name = "control_date")
    var ControlDate: String? = null,
    /** Контрольная длительность, в минутах*/
    @SerializedName("controlDuration")
    @Expose
    @ColumnInfo(name = "control_duration")
    var controlDuration: Int? = null,
    /** Дата инициализации*/
    @SerializedName("startDate")
    @Expose
    @ColumnInfo(name = "start_date")
    var startDate: String? = null,
    /** Дата снятия с контроля*/
    @SerializedName("closingDate")
    @Expose
    @ColumnInfo(name = "closing_date")
    var closingDate: String? = null,
    /** Срочность.*/
    @SerializedName("urgencyType")
    @Expose
    @ColumnInfo(name = "urgency_type")
    var urgencyType: Int? = null,
    /** Признак контроля*/
    @SerializedName("isControlled")
    @Expose
    @ColumnInfo(name = "is_controlled")
    var isControlled: Boolean = false,
    /** Контролер исполнения документа*/
    @SerializedName("controllerUid")
    @Expose
    @ColumnInfo(name = "controller_uid")
    var controllerUid: String? = null,
    /** Признак "Для информирования"*/
    @SerializedName("isInformational")
    @Expose
    @ColumnInfo(name = "is_informational")
    var isInformational: Boolean = false,
    /** Признак "Учитывать только рабочие дни"*/
    @SerializedName("workDaysOnly")
    @Expose
    @ColumnInfo(name = "work_days_only")
    var workDaysOnly: Boolean = false,
    /** Идентификатор документа, закрывшего маршрут*/
    @SerializedName("closingDocUid")
    @Expose
    @ColumnInfo(name = "closing_doc_uid")
    var closingDocUid: String? = null,
    //</editor-fold>
    //<editor-fold desc="Поля обращения" defaultstate="collapsed">
    /** Идентификатор заявителя*/
    @SerializedName("applicantUid")
    @Expose
    @ColumnInfo(name = "applicant_uid")
    var applicantUid: String? = null,
    /** Созаявители. С Сервера не присылаются.*/
    @SerializedName("applicants")
    @Expose
    @Ignore
    var applicants: List<String>? = null,
    /** Форма обращения. Справочник \link Classifiers Classifiers::FORMOBR\endlink.*/
    @SerializedName("approachForm")
    var approachForm: Int? = null,
    /** Вид обращения - ПОЛЕ УСТАРЕЛО, вместо него данные нужно считывать из \ref categoryId.*/
    @SerializedName("approachType")
    @Expose
    @Deprecated("ПОЛЕ УСТАРЕЛО, вместо него данные нужно считывать из categoryId.")
    @Ignore
    var approachType: Int? = null,
    /**Тематики. Справочник \link Classifiers Classifiers::ApproachThems\endlink - он так называется в БД!*/
    @SerializedName("themes")
    @Expose
    var themes: List<Int>? = null,
    /** Тематика обращения, она же - Президентская тематика. Справочник \link Classifiers Classifiers::AppealThemes\endlink.*/
    @SerializedName("themeId")
    @Expose
    @ColumnInfo(name = "theme_id")
    var themeId: Int? = null,
    /** Тип адресата документа (Куда адресовал заявитель), см. <see cref="CorrespondentTypes"/>*/
    @SerializedName("submittedToType")
    @Expose
    @ColumnInfo(name = "submitted_to_type")
    var ubmittedToTypeRaw: Int? = null,
    /** Организация адресата документа (Куда адресовал заявитель)*/
    @SerializedName("submittedToUid")
    @Expose
    @ColumnInfo(name = "submitted_to_uid")
    var SubmittedToUid: String? = null,
    /** Имя адресата (Куда адресовал заявитель)*/
    @SerializedName("submittedToPerson")
    @Expose
    @ColumnInfo(name = "submitted_to_person")
    var submittedToPerson: String? = null,
    /** Исходящий номер документа (заявитель)*/
    @SerializedName("submittedToDocNum")
    @Expose
    @ColumnInfo(name = "submitted_to_doc_num")
    var submittedToOutDocNum: String? = null,
    /** Исходящая дата документа (заявитель)*/
    @SerializedName("submittedToDate")
    @Expose
    @ColumnInfo(name = "submitted_to_date")
    var SubmittedToOutDate: String? = null,
    /** Тип решения ответа на обращение. Справочник \link Classifiers Classifiers::RTA_Resolution\endlink.*/
    @SerializedName("rtaResolution")
    @Expose
    @ColumnInfo(name = "rta_resolution")
    var rtaResolution: Int? = null,
    /** Объект/Место решения ответа на обращение. Справочник \link Classifiers Classifiers::RTA_Location\endlink.*/
    @SerializedName("rtaLocation")
    @Expose
    @ColumnInfo(name = "rta_location")
    var rtaLocation: Int? = null,
    /** Причина решения ответа на обращение. Справочник \link Classifiers Classifiers::RTA_Reason\endlink.*/
    @SerializedName("rtaReason")
    @Expose
    @ColumnInfo(name = "tra_reason")
    var rtaReason: Int? = null,
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
    @ColumnInfo(name = "approach_flags")
    var approachFlags: Int = 0,
    /** Идентификатор окончательного ответа*/
    @SerializedName("finalAnswerUid")
    @Expose
    @ColumnInfo(name = "final_answer_uid")
    var finalAnswerUid: String? = null,
    //</editor-fold>
    //<editor-fold desc="Поля оперативного поручения" defaultstate="collapsed">
    /** Важность*/
    @SerializedName("importanceType")
    @Expose
    @ColumnInfo(name = "importance_type")
    var importanceType: Int? = null,
    //</editor-fold>
    /** Сиквенс документа*/
    @SerializedName("sequence")
    @Expose
    var sequence: Long = 0,
    //<editor-fold desc="Associated objects" defaultstate="collapsed">
    /** Связи с виджетами*/
    @SerializedName("widgets")
    @Expose
    @Ignore
    var widgetLinks: List<DocumentWidgetLink>? = null,
    /** Связи с документами*/
    @SerializedName("links")
    @Expose
    @Ignore
    var links: List<DocumentLink>? = null,
    //</editor-fold>
    /** Метаинформация по вложениям, без содержимого. Главное вложение всегда первое в списке.*/
    @SerializedName("attachments")
    @Expose
    @Ignore
    var attachments: List<FileAttachment>? = null,
    /** Инстанции рассмотрения (записи из таблицы \b WF4_Stations под маршрутом с типом 1 в таблице \b WF4_Routes)*/
    @SerializedName("considerationStations")
    @Expose
    @Ignore
    var considerationStations: List<ConsiderationStation>? = null,
    /** Маршруты согласования (маршруты с типом 2 в таблице \b WF4_Routes). Инстанции согласования (\b WF4_Stations) лежат внутри этапов согласования (\b WF4_Stages).*/
    @SerializedName("approvalRoutes")
    @Expose
    @Ignore
    var approvalRoutes: List<ApprovalRoute>? = null,
    /** Пометки */
    @SerializedName("notes")
    @Expose
    @Ignore
    var notes: List<DocNote>? = null,
    //<editor-fold desc="" defaultstate="collapsed">
    /** докумен основание*/
    @SerializedName("baseDocInfo")
    @Expose
    @ColumnInfo(name = "base_doc_info")
    var baseDocInfo: String? = null,
    /** заголовок документа*/
    @SerializedName("baseDocRemark")
    @Expose
    @ColumnInfo(name = "base_doc_remark")
    var baseDocRemark: String? = null,
    /** основной исполнитель*/
    @SerializedName("mainExecutiveInfo")
    @Expose
    @ColumnInfo(name = "main_executive_info")
    var sainExecutiveInfo: String? = null
//</editor-fold>
) : ObjectBase() {
    override fun equals(other: Any?): Boolean {
        return when (other is Document) {
            false -> false
            true -> uid == other.uid && isControlled == other.isControlled
                    && isInformational == other.isInformational
//                    && deleted == other.deleted
        }
    }
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
