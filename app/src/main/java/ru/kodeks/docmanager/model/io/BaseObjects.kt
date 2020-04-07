package ru.kodeks.docmanager.model.io

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.const.JsonNames.BLOB_LENGTHS
import ru.kodeks.docmanager.const.JsonNames.DELAY
import ru.kodeks.docmanager.const.JsonNames.PREVIEW
import ru.kodeks.docmanager.const.JsonNames.REQUEST_KEY
import ru.kodeks.docmanager.const.JsonNames.SERVER
import ru.kodeks.docmanager.const.JsonNames.USER
import ru.kodeks.docmanager.model.data.IErrors
import ru.kodeks.docmanager.model.data.User

/** Базовый класс для всех запросов к сервису.
Для передачи бинарных данных на сервер нужно использовать заголовок \ref ServiceMobile.API.HttpHeaders.In.BinaryContentOffset,
а сами данные писать в поток сразу за JSON строкой непрерывным массивом байт один за другим. Длины блобов задаются полем \ref blobLengths.*/
open class RequestBase(

    /**Сведения о пользователе и его устройстве.*/
        @SerializedName(USER)
        @Expose
        var user: User? = null,

    /** Флаг отсроченного возврата результатов. Если установлен, то ответ содержит только ключ запроса \ref ResponseBase<T>.requestKey,
        по которому нужно спрашивать результат с помощью хандлера **GetDeferredResponse.ashx**, см. подробнее на главной странице справки.*/
        @SerializedName("runDeferred")
        @Expose
        internal var runDeferred: Boolean = true,

    /** Размеры бинарных блобов, следующих после тела JSON запроса. Бобы считываются только если указан заголовок \ref ServiceMobile.API.HttpHeaders.In.BinaryContentOffset. */
        @SerializedName(BLOB_LENGTHS)
        @Expose
        internal var blobLengths: IntArray? = null,

    /** Режим предпросмотра запроса. Если \b true, то запрос не отрабатывает, а просто сохраняется в дампах (нужно для отладки новых фич). */
        @SerializedName(PREVIEW)
        @Expose
        internal var preview: Boolean? = null,

    /** Искусственная задержка ответа от сервера для эмуляции долгой обработки (нужно для разработки).*/
        @SerializedName(DELAY)
        @Expose
        internal var delay: Int? = null
)

open class ResponseBase<T> : IErrors() where T : ResponseBase<T> {
    /** Версия сервера. Возвращается в каждом ответе от сервера.*/
    @SerializedName(SERVER)
    @Expose
    var serverVersion: String? = null
    /**  Информация о пользователе, выполнившем операцию.
    Заполняется базовым ServiceBase.HandleRequest().*/
    @SerializedName(USER)
    @Expose
    var user: User? = null
//    /**  Главная коллекция ошибок. Если все прошло успешно, то коллекция = null.
//    \b Внимание: При выполнении групповых операций сюда попадают ошибки, фатальные для всех операций,
//    а также <see cref="API.ErrorType"/>.\b ErrorInChildObjects, если возникла ошибка при обработке одной из дочерних операций
//    (в этом случае нужно проанализировать все дочерние операции).*/
//    @SerializedName("errors")
//    @Expose
//    override var errors: List<Error>? = null
    /** Ключ, по которому нужно спрашивать результат выполнения отсроченного запроса.
    \b Внимание: это поле заполняется только при ответе на отсроченный запрос (<see cref="RequestBase.runDeferred"/> = true).
    Этот ответ с ключом не содержит самого ответа.*/
    @SerializedName(REQUEST_KEY)
    @Expose
    var requestKey: String? = null
}

/** Пустой ответ, содержащий только ИД пользователя, версию сервера и ошибки.
Этот вид ответа используется для возврата ответов с ошибками для веб методов, которые должны при нормальной работе возвращать бинарные данные.
При возврате ошибок бинарными веб методами в ответ всегда добавляется заголовок <see cref="HttpHeaders.Out"/>.\b HasErrors.*/
class DefaultResponse : ResponseBase<DefaultResponse>()
