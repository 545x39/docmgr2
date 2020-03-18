package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Перечисление, описывающее способы подписания документов ЭП.*/
object SignedDataType {

    /** Подписание вложений на вкладке "Вложения" (штамп располагается на первой старнице)*/
    const val ATTACHMENT_SIGN = 1

    /** Подписание при согласовании (штамп располагается на листе согласования)*/
    const val APPROVAL_SIGN = 2

    /** Подписание при регистрации (штамп располагается на первой странице в виде штампа регистрации)*/
    const val REGISTRATION_SIGN = 3

    /** Подписание резолюций*/
    const val RESOLUTION_SIGN = 4

    /** Подписание отчетов по резолюциям*/
    const val RESOLUTION_REPORT_SIGN = 5

    /** Подписание визы согласования*/
    const val APPROVAL_VISA_SIGN = 6
}


/**  Объект, хранящий ЭП инстанции. Ошибки валидации конкретных ЭП приходят в коллекции \b errors.
Ошибки
- 303: API.ErrorType::DataIntegrityError - Обнаружено нарушение целостности данных при проверке ЭП (вероятней всего, данные были изменены с момента формирования ЭП).
- 304: API.ErrorType::SignatureVerificationError - Ошибка при криптографической проверке подлинности ЭП.*/
/** Если нет ни одной ошибки, то ЭП считается действительной.*/

data class StationSignature(
    /** Идентификатор инстанции.*/
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    /** ЭП (XML данные в виде строки). Значение текстового поля из \b WF4_Stations.DigitalSignature.*/
    @SerializedName("signature")
    @Expose
    val signature: String? = null
) : ObjectBase()
