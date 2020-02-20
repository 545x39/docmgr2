package ru.kodeks.docmanager.model.data.actions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.model.data.FileUploadInfo

/** Подписание документа, см. <see cref="IoModel.SignDocumentsRequest"/>.*/

/** <h3>Ошибки</h3>
Помимо кодов ошибок, унаследованных от <see cref="SignedStationAction"/> и <see cref="IFileUpload"/>, при выполнении данного действия может возникать ряд логических ошибок:
- \b 72 \link API.ErrorType API.ErrorType::FileNotFound\endlink - Подписываемый файл не найден на сервере.
- \b 73 \link API.ErrorType API.ErrorType::FileRequired\endlink - Отсутствует файл, подписанный квалифицированной ЭП.
- \b 710 \link API.ErrorType API.ErrorType::OutdatedApprovalStationAction\endlink - Действие по согласованию/подписанию утратило актуальность.*/
class SignDocumentAction : SignedStationAction<SignDocumentAction>(), IFileUpload {
    /// Подписанный документ.
    @SerializedName("file")
    @Expose
    override var file: FileUploadInfo? = null
}

/** Тип действия по согласованию документа, используется в <see cref="ApprovalStationAction"/>.*/
class ApprovalActionTypes {
    /** Неизвестно. Для RejectDocuments данное значение приравнено к Reject.*/
    val UNKNOWN = 0

    /** Согласовать.*/
    val APPROVE = 1

    /** Вернуть на доработку.*/
    val REJECT = 2

    /** Перенаправить.*/
    val FORWARD = 3

    /** Удалить подмаршрут*/
    val DELETE_FORWARDING = 4
}

/** Действие с документом в рамках согласования, см. <see cref="IoModel.ApproveDocumentsRequest"/>:
- Согласование документа с опциональным добавлением вложения.
- Возврат документа на доработку с опциональным добавлением вложения.
- Перенаправление документа на подмаршрут.
- Удаление документа с подмаршрута.*/

/** <h3>Ошибки</h3>
Ошибки унаследованы от \ref SignedStationAction<TAction> и <see cref="IFileUpload"/>, а также:
- \b 701 \link API.ErrorType API.ErrorType::ResultCodeMissing\endlink - Операция требует указание кода причины.
- \b 702 \link API.ErrorType API.ErrorType::ResultTextMissing\endlink - Операция требует указание текста причины.
- \b 710 \link API.ErrorType API.ErrorType::OutdatedApprovalStationAction\endlink - Действие по согласованию утратило актуальность.*/
class ApprovalStationAction : SignedStationAction<ApprovalStationAction>(), IFilesUpload {

    /** Тип операции согласования. См. <see cref="ApprovalActionTypes"/>.*/
    @SerializedName("actionType")
    @Expose
    var actionType: Int? = null

    /** Тип операции согласования. См. <see cref="ApprovalActionTypes"/>.*/
    @SerializedName("attachmentVersion")
    @Expose
    var attachmentVersion: Int = 0

    /** Сопроводительный текст, результат операции по согласованию.*/
    @SerializedName("resultText")
    @Expose
    var resultText: String? = null

    /** Код результата согласования (используется только при согласовании) - ИД элемента из словаря \link Classifiers Classifiers::CRESULT\endlink.
    Для операции "Согласовать" элементы словаря нужно фильтровать по значениям \b resultCode = \link ApprovalResults
    ApprovalResults::Approved\endlink (2) или \link ApprovalResults ApprovalResults::ApprovedWithRemarks\endlink (3).*/
    @SerializedName("resultCode")
    @Expose
    var resultCode: Int? = null

    /** Кто подписал (можно не передавать - по умолчанию проставляется текущий пользователь).*/
    @SerializedName("signerUid")
    @Expose
    var SignerUid: String? = null//Guid

    /** Контрольный срок для подмаршрута (используется только при перенаправлении документа).
    В остальных случаях нужно не передавать или передавать \e null.*/
    @SerializedName("controlDate")
    @Expose
    var controlDate: String? = null

    /** Исполнители для перенаправления (используется только при перенаправлении документа).*/
    @SerializedName("executiveUids")
    var executiveUids: List<String>? = null//Guid[]

    /** Сопутствующие вложения, которые будут прикреплены к инстанции согласования.*/
    @SerializedName("files")
    @Expose
    override var files: List<FileUploadInfo>? = null
}

/** Подписание второстепенного вложения.*/

/** <h3>Ошибки</h3>
Ошибки унаследованы от <see cref="SignedStationAction"/>.*/
//class TempSignAttachmentAction : SignedStationAction {
class TempSignAttachmentAction<TAction> : SignedStationAction<TAction>() where TAction : BaseAction<TAction> {
//        #region Fields

    /** УИД документа вложения, который соответствует полю DS_TextStore.Doc_UID. При подписании основного
    вложения данное поле должно быть не указано или его значение должно совпасть с \ref docUid.*/
    @SerializedName("textStoreDocUid")
    @Expose
    var textStoreDocUid: String? = null//Guid

    // Данные для поля DS_TextStore.DigitalSignature

    /** Рег. номер, генерируемый при создании штампа регистрации (не используется при подписании документа).*/
    @SerializedName("regNum")
    @Expose
    var regNum: String? = null

    /** Рег. дата, генерируемая при создании штампа регистрации (не используется при подписании документа).*/
    @SerializedName("regDate")
    @Expose
    var regDate:String? = null//DateTime

    /** Дата и время подписания с точностью до минуты (секунды можно не передавать).*/
    @SerializedName("signDate")
    @Expose
    var signDate: String? = null//DateTime

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
    var signatureType: Int? = null//Byte

    /** Номер сертификата для квалифицированной подписи (для простой можно не передавать).*/
    @SerializedName("certificateNumber")
    @Expose
    var certificateNumber: String? = null
}
