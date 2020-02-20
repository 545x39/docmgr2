package ru.kodeks.docmanager.model.io

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.model.data.Folder
import ru.kodeks.docmanager.model.data.User


/** <see cref="API.IFiles"/>. Запрос на получение папок и метаинформации дочерних файлов.

 *\b Ответ: <see cref="GetFoldersResponse"/>.
 *<h3>Пример</h3>
 *<pre><code>
 *{
 *    "user": {
 *      "login": "skovpen",
 *      "password": "11111",
 *      "device": "iOS",
 *      "version": "1.2.0"
 *     },
 *    "folderId": 1,
 *    "hierarchyLevel": 15
 *}
 *</code></pre>*/
class GetFoldersRequest : RequestBase() {
    /** Идентификатор родительской папки (зарезервировано на будущее).*/
    @SerializedName("folderId")
    @Expose
    var parentFolderId: Int? = null

    /** Сколько уровней папок возвращать. По умолчанию 2 (корневые папки и подпапки первого уровня).*/
//    JsonProperty("hierarchyLevel")
//    int HierarchyLevel = 2

    /** Текущий сиквенс папок и категорий подборок, начиная с которого нужно выдавать измененные объекты.*/
    @SerializedName("sequence")
    @Expose
    var foldersSequence: Long? = null
}


/** <see cref="API.IFiles"/>. Ответ на запрос папок и заголовков файлов.*/
class GetFoldersResponse : ResponseBase<GetFoldersResponse>() {
    /// Папки вместе с заголовками дочерних файлов.
    @SerializedName("folders")
    @Expose
    var folders: List<Folder>? = null
}

/** <see cref="API.IFiles"/>. Запрос на получение содержимого файла.
Если файл является вложением документа, то нужно передавать \b docUid, а если нет,
то поле docUid надо игнорировать, либо передавать <i>null</i>, либо писать туда <i>"00000000-0000-0000-0000-000000000000"</i>.
Если вложение не найдено, то возвращается код \b 72 \link API.ErrorType API.ErrorType::FileNotFound \endlink (Файл не найден по указанному идентификатору).

\b Ответ: бинарный.

<h3>Пример запроса вложения документа</h3>
 *<pre><code>
 *{
 *  "user": {
 *"     login": "gromov",
 *      "password": "11111",
 *      "device": "iOS",
 *      "version": "1.2.0"
 *  },
 *   "textStoreDocUid": "65B4CF29-69BF-435C-89C2-55305306929B",
 *   "docUid": "1cfeb98b-c7e1-4bad-b65f-09df50161c77",
 *   "appendSearchText": true,
 *   "skipContent": false,
 *   "convertToPdf": true
 *}
</code></pre>*/

class GetFileRequest constructor(

    /** Поля базового класса, обязательные для заполнения */

    user: User? = null,

    runDeferred: Boolean? = null,

    /** УИД документа вложения, который соответствует полю DS_TextStore.Doc_UID.
    /// Для главного вложения документа этот УИД совпадает с УИД документа.*/
    @SerializedName("textStoreDocUid")
    @Expose
    var textStoreDocUid: String? = null,

    /** УИД родительского документа. Нужен исключительно в целях удобства отладки и протоколирования.
    Для вложений, не относящихся к какому-либо документу, это поле не передается.*/
    @SerializedName("docUid")
    @Expose
    var docUid: String? = null,//Guid?

    /** В зависимости от этого флага либо возвращается вложение как есть, либо возвращается вложение с его OCR текстом.
    Если возвращается вложение с текстом, то сначала идет размер вложения и его байты,
    потом размер текста вложения и сам текст в кодировке \e windows-1251.
    Если запрашивается только OCR текст (<see cref="skipContent"/> = \e false), то размер отсутствует и ответ сервера содержит только текст.*/
    @SerializedName("appendSearchText")
    @Expose
    var appendSearchText: Boolean? = null,

    /** В зависимости от этого флага возвращается либо не возвращается тело вложения.
    /// Использование этого флага позволяет запросить только OCR текст файла.*/
    @SerializedName("skipContent")
    @Expose
    var skipContent: Boolean? = null,

    /** Нужно ли конвертировать DOCX вложение в PDF для упрощения просмотра. По умолчанию <i>false</i>.
    Если этот флаг <i>true</i>, то при успешной конвертации файла возвращается HTTP заголовок \ref ServiceMobile.API.HttpHeaders.Out.FileExtension.*/
    @SerializedName("convertToPdf")
    @Expose
    var convertToPdf: Boolean? = null,

    /** Нужно ли сжимать картинки в PDF для уменьшения нагрузки на канал. По умолчанию <i>false</i>.*/
    @SerializedName("compressPdf")
    @Expose
    var compressPdf: Boolean? = null,

    /** Нужно ли преобразовывать картинки в PDF в ЧБ для уменьшения нагрузки на канал. По умолчанию <i>false</i>.*/
    @SerializedName("forceBwPdf")
    @Expose
    var forceBwPdf: Boolean? = null

) : RequestBase()
//{
//    init {
//        super.user = user
//        super.runDeferred = runDeferred
//    }
//}

/** <see cref="API.IFiles"/>. Запрос на получение статуса загрузки файла с заданными UID'ами.
Для начала загрузки файла на сервер можно также использовать этот метод, но это <i>не обязательно</i> - так сделано для возможности
унификации загрузки в коде клиента. Реально данные начнут сохраняться на сервере только при получении первого байта от клиента.
\b Ответ: <see cref="CheckUploadStatusResponse"/>.
<h3>Пример</h3>
<pre><code>
{
"user": {
"login": "skovpen",
"password": "11111",
"device": "iOS",
"version": "1.2.0"
},
"fileUids": ["65B4CF29-69BF-435C-89C2-55305306929B"]
}
</code></pre>*/
class CheckUploadStatusRequest : RequestBase() {

    /** Идентификатор загружаемых файлов.*/
    @SerializedName("fileUids")
    @Expose
    var fileUids: List<String>? = null//Guid[]
}

/** <see cref="API.IFiles"/>. Ответ на запрос статуса загрузки файлов на сервер.
Сервер не знает, полностью ли загружен файл - это клиент определяет самостоятельно сравнивая длину файла и количество полученных байт.
\b Запрос: <see cref="CheckUploadStatusRequest"/>.*/
class CheckUploadStatusResponse : ResponseBase<CheckUploadStatusResponse>() {
    /** Количество полученных сервером байт по каждому УИДу. Если файла нет или он пустой, то возвращается 0.*/
    @SerializedName("bytesRead")
    @Expose
    var bytesRead: Map<String, Long>? = null//Dictionary<Guid, long>
}
