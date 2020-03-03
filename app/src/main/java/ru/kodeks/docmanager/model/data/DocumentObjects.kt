package ru.kodeks.docmanager.model.data

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ru.kodeks.docmanager.persistence.typeconverters.IntListToStringTypeConverter

/** Связь документа с виджетом. Передается на клиент в составе документа, см. <see cref="Document"/>.*/
@Entity(tableName = "document_widget_links", primaryKeys = ["widget_id", "doc_uid"])
@TypeConverters(IntListToStringTypeConverter::class)
class DocumentWidgetLink(
    /** ИД виджета. Привязка выполняется по коду из-за потенциальной необходимости внедрять замещения.*/
    @SerializedName("wid")
    @Expose
    @ColumnInfo(name = "widget_id")
    var wid: Int = 0,
    /**  UID документа*/
    @SerializedName("doc_uid")
    @Expose
    @ColumnInfo(name = "doc_uid")
    var docUid: String = "",
    /**  Инстанции документа, относящиеся к текущему виджету.
    Первой в списке идет инстанция, к которой применимы действия по умолчанию в списке документов.
    \b Внимание: У виджета не обязательно есть инстанции (например, их нет в оперативном архиве).*/
    @SerializedName("psid")
    @Expose
    @ColumnInfo(name = "station_ids")
    var psid: List<Int>? = null,
    /**  Тип документа*/
    @SerializedName("doc_type")
    @Expose
    @ColumnInfo(name = "doc_type")
    var docType: Int? = null
) : ObjectBase()

/** Связь объекта с документом.
Родителем может быть:
- документ <see cref="Document"/>
- маршрут согласования <see cref="ApprovalRoute"/>
- инстанция рассмотрения <see cref="ConsiderationStation"/>*/
@Entity(tableName = "document_links", primaryKeys = ["linked_doc_uid", "doc_uid"])
class DocumentLink(
    @ColumnInfo(name = "doc_uid")
    var doc_uid: String = "",
    /**  УИД cвязанного документа*/
    @SerializedName("uid")
    @Expose
    @ColumnInfo(name = "linked_doc_uid")
    var uid: String = "",
    /**  Тип связанного документа*/
    @SerializedName("docType")
    @Expose
    @ColumnInfo(name = "linked_doc_type")
    var docType: Int? = null,
    /**  Тип связи. Значение из справочника \link Classifiers Classifiers::LINK_TYPES\endlink. Заполняется только для связей Документ-Документ.*/
    @SerializedName("type")
    @Expose
    var type: Int? = null,
    /**  Регистрационный номер документа*/
    @SerializedName("regNum")
    @Expose
    @ColumnInfo(name = "linked_doc_reg_num")
    var regNum: String? = null,
    /**  Дата регистрации документа*/
    @SerializedName("regDate")
    @Expose
    @ColumnInfo(name = "linked_doc_reg_name")
    var regDate: String? = null,
    /**  Наименование, краткое содержание документа*/
    @SerializedName("name")
    @Expose
    var name: String? = null,
    /**  Метаинформация по вложениям, без содержимого. Главное вложение всегда первое в списке.*/
    @SerializedName("attachments")
    @Expose
    @Ignore
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