package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Перечисление, описывающее способы подписания документов ЭП.*/
sealed class SignedDataType {

    /** Подписание вложений на вкладке "Вложения" (штамп располагается на первой старнице)*/
    val ATTACHMENT_SIGN = 1

    /** Подписание при согласовании (штамп располагается на листе согласования)*/
    val APPROVAL_SIGN = 2

    /** Подписание при регистрации (штамп располагается на первой странице в виде штампа регистрации)*/
    val REGISTRATION_SIGN = 3

    /** Подписание резолюций*/
    val RESOLUTION_SIGN = 4

    /** Подписание отчетов по резолюциям*/
    val RESOLUTION_REPORT_SIGN = 5

    /** Подписание визы согласования*/
    val APPROVAL_VISA_SIGN = 6
}


/**  Объект, хранящий ЭП инстанции. Ошибки валидации конкретных ЭП приходят в коллекции \b errors.
<h3>Ошибки</h3>
- \b 303 \link API.ErrorType API.ErrorType::DataIntegrityError \endlink - Обнаружено нарушение целостности данных при проверке ЭП (вероятней всего, данные были изменены с момента формирования ЭП).
- \b 304 \link API.ErrorType API.ErrorType::SignatureVerificationError \endlink - Ошибка при криптографической проверке подлинности ЭП.*/

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
