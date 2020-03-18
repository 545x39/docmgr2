package ru.kodeks.docmanager.model.data.actions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.model.data.FileUploadInfo

/** Подписание документа.*/

/** <h3>Ошибки</h3>
Помимо кодов ошибок, унаследованных от "SignedStationAction" и "IFileUpload", при выполнении данного действия
может возникать ряд логических ошибок:
- 72: API.ErrorType::FileNotFound - Подписываемый файл не найден на сервере.
- 73: API.ErrorType::FileRequired - Отсутствует файл, подписанный квалифицированной ЭП.
- 710: API.ErrorType API.ErrorType::OutdatedApprovalStationAction - Действие по согласованию/подписанию утратило актуальность.*/
class SignDocumentAction : SignedStationAction<SignDocumentAction>(), IFileUpload {
    /// Подписанный документ.
    @SerializedName("file")
    @Expose
    override var file: FileUploadInfo? = null
}

/** Тип действия по согласованию документа, используется в <see cref="ApprovalStationAction"/>.*/
object ApprovalActionTypes {
    /** Неизвестно. Для RejectDocuments данное значение приравнено к Reject.*/
    const val UNKNOWN = 0

    /** Согласовать.*/
    const val APPROVE = 1

    /** Вернуть на доработку.*/
    const val REJECT = 2

    /** Перенаправить.*/
    const val FORWARD = 3

    /** Удалить подмаршрут*/
    const val DELETE_FORWARDING = 4
}

/** Действие с документом в рамках согласования, см. <see cref="IoModel.ApproveDocumentsRequest"/>:
- Согласование документа с опциональным добавлением вложения.
- Возврат документа на доработку с опциональным добавлением вложения.
- Перенаправление документа на подмаршрут.
- Удаление документа с подмаршрута.*/

/** Ошибки
Ошибки унаследованы от SignedStationAction<TAction> и "IFileUpload", а также:
- 701: API.ErrorType::ResultCodeMissing - Операция требует указание кода причины.
- 702: link API.ErrorType API.ErrorType::ResultTextMissing - Операция требует указание текста причины.
- 710: API.ErrorType::OutdatedApprovalStationAction - Действие по согласованию утратило актуальность.*/
class ApprovalStationAction : SignedStationAction<ApprovalStationAction>(), IFilesUpload {

    /** Тип операции согласования. См. ApprovalActionTypes.*/
    @SerializedName("actionType")
    @Expose
    var actionType: Int? = null

    /** Тип операции согласования. См. ApprovalActionTypes.*/
    @SerializedName("attachmentVersion")
    @Expose
    var attachmentVersion: Int = 0

    /** Сопроводительный текст, результат операции по согласованию.*/
    @SerializedName("resultText")
    @Expose
    var resultText: String? = null

    /** Код результата согласования (используется только при согласовании) - ИД элемента из словаря Classifiers::CRESULT.
    Для операции "Согласовать" элементы словаря нужно фильтровать по значениям resultCode = ApprovalResults::Approved (2)
    или ApprovalResults::ApprovedWithRemarks (3).*/
    @SerializedName("resultCode")
    @Expose
    var resultCode: Int? = null

    /** Кто подписал (можно не передавать - по умолчанию проставляется текущий пользователь).*/
    @SerializedName("signerUid")
    @Expose
    var signerUid: String? = null

    /** Контрольный срок для подмаршрута (используется только при перенаправлении документа).
    В остальных случаях нужно не передавать или передавать null.*/
    @SerializedName("controlDate")
    @Expose
    var controlDate: String? = null

    /** Исполнители для перенаправления (используется только при перенаправлении документа).*/
    @SerializedName("executiveUids")
    var executiveUids: List<String>? = null

    /** Сопутствующие вложения, которые будут прикреплены к инстанции согласования.*/
    @SerializedName("files")
    @Expose
    override var files: List<FileUploadInfo>? = null
}

/** Подписание второстепенного вложения.*/

/** Ошибки
Ошибки унаследованы от SignedStationAction".*/
//class TempSignAttachmentAction : SignedStationAction {
class TempSignAttachmentAction<TAction> :
    SignedStationAction<TAction>() where TAction : BaseAction<TAction> {

    /** УИД документа вложения, который соответствует полю DS_TextStore.Doc_UID. При подписании основного
    вложения данное поле должно быть не указано или его значение должно совпасть с docUid.*/
    @SerializedName("textStoreDocUid")
    @Expose
    var textStoreDocUid: String? = null

    // Данные для поля DS_TextStore.DigitalSignature

    /** Рег. номер, генерируемый при создании штампа регистрации (не используется при подписании документа).*/
    @SerializedName("regNum")
    @Expose
    var regNum: String? = null

    /** Рег. дата, генерируемая при создании штампа регистрации (не используется при подписании документа).*/
    @SerializedName("regDate")
    @Expose
    var regDate: String? = null//DateTime

    /** Дата и время подписания с точностью до минуты (секунды можно не передавать).*/
    @SerializedName("signDate")
    @Expose
    var signDate: String? = null

    /** Полное ФИО подписавшего.*/
    @SerializedName("signerName")
    @Expose
    var signerName: String? = null

    /** Должность подписавшего.*/
    @SerializedName("signerPosition")
    @Expose
    var signerPosition: String? = null

    /** Тип подписи: 0 - квалифицированная, 1 - простая.*/
    @SerializedName("signatureType")
    @Expose
    var signatureType: Int? = null

    /** Номер сертификата для квалифицированной подписи (для простой можно не передавать).*/
    @SerializedName("certificateNumber")
    @Expose
    var certificateNumber: String? = null
}
