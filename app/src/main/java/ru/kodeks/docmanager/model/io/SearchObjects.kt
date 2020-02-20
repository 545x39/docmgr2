package ru.kodeks.docmanager.model.io

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.model.data.FoundDocument
import ru.kodeks.docmanager.model.data.SearchParameter

/** <see cref="API.ISearch"/>. Запрос на поиск документов.
\b Ответ: <see cref="FindDocsResponse"/>.
<h3>Пример</h3>
<pre><code>
 *    {
 *      "user": {
 *        "login": "skovpen",
 *        "password": "11111",
 *        "device": "iOS",
 *        "version": "1.2.0"
 *      },
 *      "docType": 0,
 *      "searchParams": [
 *        {
 *          "name": "User",
 *          "value1": "da957c11-e260-4a3c-b7f0-a09ebb0ebde7"
 *        },
 *        {
 *          "name": "Text",
 *          "value1": "тест"
 *        },
 *        {
 *          "name": "RegNum",
 *          "value1": "123"
 *        },
 *        {
 *          "name": "RegDate",
 *          "value1": "2015-08-04T12:00:00",
 *          "value2": "2015-08-05T12:00:00"
 *        }
 *      ]
 *    }
</code></pre>*/
class FindDocsRequest : RequestBase() {

    /** Маска идентификаторов глобального типа документа, см. <see cref="DataModel.DocTypeSearchMask"/>.*/
    @SerializedName("docType")
    @Expose
    var documentType: Int? = null

    /** Ограничение на количество результатов. Если поле не передано с клиента или передано не положительное значение,
    то берется значение по умолчанию 100.
    В ядре стоит другое ограничение, которое не превысить - 2000 результатов, поэтому более высокие значения будут игнорироваться.*/
    @SerializedName("maxResults")
    @Expose
    var maxResults: Int? = null

    /** Поисковые параметры.*/
    @SerializedName("searchParams")
    @Expose
    var searchParams: List<SearchParameter>? = null
}

/** <see cref="API.ISearch"/>. Ответ на поиск документов. <b>Внимание:</b> заполняются только УИД, тип/вид документа, рег. номер, рег. дата и содержание.
\b Запрос: <see cref="FindDocsRequest"/>.*/
class FindDocsResponse : ResponseBase<FindDocsResponse>() {

    /** Количество найденных документов (может превосходить кол-во возвращенных документов).*/
    @SerializedName("foundDocsCount")
    @Expose
    var foundDocsCount: Int? = null

    /** Количество возвращаемых документов.*/
    @SerializedName("docsCount")
    @Expose
    var docsCount: Int? = null

    /** Список найденных документов с краткой информацией по ним.*/
    @SerializedName("docs")
    @Expose
    var documents: List<FoundDocument>? = null
}