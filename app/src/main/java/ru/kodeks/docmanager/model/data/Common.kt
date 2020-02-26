package ru.kodeks.docmanager.model.data

import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Базовый класс для всех бизнес-объектов системы. */
open class ObjectBase(
    /** Признак логического удаления. */
    @SerializedName("deleted")
    @Expose
    var deleted: Boolean = false,
    /** Коллекция ошибок. Если все прошло успешно, то коллекция = null.
    В простых случаях ошибка возвращается только в корневой коллекции \ref IoModel.ResponseBase<T>.errors. */
    @SerializedName("errors")
    @Expose
    @Ignore
    var errors: List<Error>? = null
) {
    @Transient
    var _id: Int? = null
}