package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Маска для выбора типов документов при поиске*/
sealed class DocTypeSearchMask {
    /** Все типы*/
    val ALL = 0

    /** Входящие*/
    val INCOMING = 1

    /** Исходящие*/
    val OUTGOING = 2

    /** ОРД*/
    val NORMATIVE_AND_ORGANIZATIONAL = 4

    /** Внутренние*/
    val DIRECTIVES = 32
}


/** Имя поискового параметра*/
sealed class SearchParameterName {

    /** Поиск по текстовому содержимому документа.*/
    val TEXT = 1

    /** Поиск по рег. номеру.*/
    val REG_NUM = 2

    /** Поиск по диапазону рег. дат.*/
    val REG_DATE = 3

    /** Организация/исполнитель. С клиента нужно в это поле передавать УИД элемента из ГК или строчку из поискового поля.*/
    val USER = 4
}

/** Поисковый параметр, используется в поисковом запросе <see cref="FindDocsRequest"/>.*/
class SearchParameter(
    /** Имя поискового параметра, см. <see cref="SearchParameterName"/>.*/
        @SerializedName("name")
        @Expose
        var name: SearchParameterName? = null,
    /** Значение (или первое значение диапазона).*/
        @SerializedName("value1")
        @Expose
        var value1: String? = null,
    /** Последнее значение диапазона.*/
        @SerializedName("value2")
        @Expose
        var value2: String? = null,
    /** Требуется ли искать точное соответствие (актуально только для строковых типов).*/
        @SerializedName("isExactMatch")
        @Expose
        var isExactMatch: Boolean? = null
)