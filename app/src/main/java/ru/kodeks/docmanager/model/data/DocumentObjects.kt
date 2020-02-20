package ru.kodeks.docmanager.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Связь документа с виджетом. Передается на клиент в составе документа, см. <see cref="Document"/>.*/
class DocumentWidgetLink(
    /** ИД виджета. Привязка выполняется по коду из-за потенциальной необходимости внедрять замещения.*/
    @SerializedName("wid")
    @Expose
    var wid: Int? = null,
    /**  Инстанции документа, относящиеся к текущему виджету.
    Первой в списке идет инстанция, к которой применимы действия по умолчанию в списке документов.
    \b Внимание: У виджета не обязательно есть инстанции (например, их нет в оперативном архиве).*/
    @SerializedName("psid")
    @Expose
    var psid: Array<Int>? = null,
    /**  UID документа*/
    var docUid: String? = null,
    /**  Тип документа*/
    var docType: Int? = null
) : ObjectBase()

/** Связь объекта с документом.
Родителем может быть:
- документ <see cref="Document"/>
- маршрут согласования <see cref="ApprovalRoute"/>
- инстанция рассмотрения <see cref="ConsiderationStation"/>*/

class DocumentLink(
    /**  УИД cвязанного документа*/
    @SerializedName("uid")
    @Expose
    var uid: String? = null,
    /**  Тип связанного документа*/
    @SerializedName("docType")
    @Expose
    var docType: Int? = null,
    /**  Тип связи. Значение из справочника \link Classifiers Classifiers::LINK_TYPES\endlink. Заполняется только для связей Документ-Документ.*/
    @SerializedName("type")
    @Expose
    var type: Int? = null,
    /**  Регистрационный номер документа*/
    @SerializedName("regNum")
    @Expose
    var regNum: String? = null,
    /**  Дата регистрации документа*/
    @SerializedName("regDate")
    @Expose
    var regDate: String? = null,
    /**  Наименование, краткое содержание документа*/
    @SerializedName("name")
    @Expose
    var name: String? = null,
    /**  Метаинформация по вложениям, без содержимого. Главное вложение всегда первое в списке.*/
    @SerializedName("attachments")
    @Expose
    var attachments: List<FileAttachment>? = null
) : ObjectBase()

/** Пометка к документу.*/
@Entity(tableName = "document_notes")
data class DocNote(
    /** Идентификатор пометки, уникальный в пределах документа. Удаление происходит именно по числовому ИД и УИД документа.*/
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    var id: Int = 0,
    /** Уникальный идентификатор пометки. Пометки создаются и обновляются по этому УИД.*/
    @SerializedName("uid")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "uid")
    var uid: String = "",
    /** Текст пометки*/
    @SerializedName("note")
    @Expose
    @ColumnInfo(name = "note")
    var note: String = "",
    /** Является ли пометка общей*/
    @SerializedName("common")
    @Expose
    @ColumnInfo(name = "is_common")
    var common: Boolean = false,
    /** Идентификатор создателя пометки*/
    @SerializedName("authorUid")
    @Expose
    @ColumnInfo(name = "author_uid")
    var authorUid: String? = null,
    /** Идентификатор оператора*/
    @SerializedName("operatorUid")
    @Expose
    @ColumnInfo(name = "operator_uid")
    var operatorUid: String? = null,
    /** Дата создания пометки*/
    @SerializedName("created")
    @Expose
    @ColumnInfo(name = "created")
    var created: String? = null,
    /** Дата изменения пометки*/
    @SerializedName("modified")
    @Expose
    @ColumnInfo(name = "modified")
    var modified: String? = null,
    /** Операция, см. <see cref="Actions.Verb"/>. Нулевое значение недопустимо.*/
    @SerializedName("verb")
    @Expose
    @Ignore
    var verb: Int? = null,
    //TODO Разобраться зачем оно
    @Transient
    @ColumnInfo(name = "sync_status")
    var syncStatus: Int? = null
) : ObjectBase()