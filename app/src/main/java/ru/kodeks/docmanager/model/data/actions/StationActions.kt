package ru.kodeks.docmanager.model.data.actions

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Базовое действие с входящей инстанцией.*/

/**  <h3>Ошибки</h3>
Помимо кодов ошибок, отнаследованных от \ref BaseDocumentAction<TAction>, при выполнении данного действия может возникать ряд логических ошибок:
- \b 530 \link API.ErrorType API.ErrorType::NoIncomingStation \endlink - Не задан идентификатор входящей инстанции.
- \b 550 \link API.ErrorType API.ErrorType::IncomingStationNotFound \endlink - Входящая инстанция не найдена в маршруте рассмотрения.
- \b 560 \link API.ErrorType API.ErrorType::InvalidRouteState \endlink - Маршрут не допускает запрошенного действия.*/
abstract class BaseStationAction<TAction> : BaseDocumentAction<TAction>() where TAction : BaseAction<TAction> {

    /** Идентификатор входящей инстанции.*/
    @SerializedName("incomingStationId")
    @Expose
    var incomingStationId: Int? = null
}

/** Действие с входящей инстанцией, подписанное с помощью ЭП.*/
abstract class SignedStationAction<TAction> : BaseStationAction<TAction>() where TAction : BaseAction<TAction> {

    /** ЭП (XML, содержащий информацию о документе, резолюции, подписавшем и самую подпись).
    Данные из ЭП нужно подписывать в кодировке UTF-8. На стороне сервера в ЭП добавляются ИД инстанций
    отдельным тегом после сохранения резолюции в БД.*/

    /** \b Внимание: Если это поле заполнено, но права пользователя на создание ЭП (код \b 138) отсутствуют, то сервис выдаст ошибку
     \link API.ErrorType API.ErrorType::SignatureWriteDenied \endlink.*/
    @SerializedName("signature")
    @Expose
    var signature: String? = null
}
