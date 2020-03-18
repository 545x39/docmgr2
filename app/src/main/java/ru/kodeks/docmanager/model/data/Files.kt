package ru.kodeks.docmanager.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Разрешения на папку.*/
object FolderPermissions {

    /**  Нет доступа*/
    const val NONE = 0

    /**  Чтение содержимого папки*/
    const val READ = 1

    /**  Изменение метаданных папки*/
    const val MODIFY = 2

    /** Удаление папки*/
    const val DELETE = 4

    /** Добавление документов в папку*/
    const val ADD_DOCUMENTS = 8
}

/** Типы папок.*/
object FolderTypes {

    /**  Личная папка*/
    const val PRIVATE_FOLDER = 0

    /** Общая папка*/
    const val SHARED_FOLDER = 1

    /**  Категория подборок*/
    const val COMPILATION_CATEGORY = 2

    /**  Подборка*/
    const val COMPILATION = 3
}

/**  Папка с документами. Бывает общая, личная, категория и подборка (см. тип \b type).*/
open class Folder(
    /**  Идентификатор папки. Корневые папки имеют id = -5, -6.*/
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    /**  УИД папки. Корневые и новые папки не имеют УИД.(Guid)*/
    @SerializedName("uid")
    @Expose
    var uid: String? = null,
    /** Идентификатор родительской папки (заполняется только у неудаленных папок). (parentId)*/
    @SerializedName("pid")
    @Expose
    var parentId: Int? = null,
    /** Признак личной папки.*/
    @SerializedName("private")
    @Expose
    var isPrivate: Boolean? = null,
    /** Тип папки, см. <see cref="FolderTypes"/>.*/
    @SerializedName("type")
    @Expose
    var type: Int? = null,
    /** Наименование папки.*/
    @SerializedName("name")
    @Expose
    var name: String? = null,
    /** Примечание к папке.*/
    @SerializedName("remark")
    @Expose
    var remark: String? = null,
    /** Дата создания папки.(DateTime)*/
    @SerializedName("created")
    @Expose
    var created: String? = null,
    /** Идентификатор владельца папки.(Guid)*/
    @SerializedName("ownerUid")
    @Expose
    var ownerUid: String? = null,
    /** Имя владельца папки.*/
    @SerializedName("ownerName")
    @Expose
    var ownerName: String? = null,
    /** Разрешения на папку. Битовая маска, состоящая из набора прав <see cref="FolderPermissions"/>.*/
    @SerializedName("permissions")
    @Expose
    var permissions: Int? = null,
    /** Дочерние файлы.*/
    @SerializedName("files")
    @Expose
    var files: List<FileDocument>? = null
//    /** Идентификатор родительской папки (заполняется всегда).*/
    //int ParentIdInternal;
) : ObjectBase()

/** Категория/подборка*/

/**  Состояние подборки*/
object CompilationStates {

    /**  Не определено*/
    const val UNDEFINED = 0

    /** Подготовка*/
    const val PREPEARING = 1

    /** Отправлена*/
    const val SENT = 2

    /** Получена*/
    const val RECEIVED = 3

    /** Подписана*/
    const val SIGNED = 4

    /** На доработку*/
    const val REJECTED = 5

    /** Архив*/
    const val ARCHVED = 6
}

/** Категория/подборка документов - особая разновидность папки с документами.*/
class Compilation(
    /**  Цвет категории. Заполняется только у категории.*/
    @SerializedName("color")
    @Expose
    var color: String? = null,
    /** УИД документа-основания. Заполняется только у подборки. Именно этот документ должен открываться как основной документ подборки.
    Проект резолюции по подборке нужно брать из него. (Guid)*/
    @SerializedName("baseDocUid")
    @Expose
    var baseDocUid: String? = null,
    /** Автор подборки/категории. Его нужно использовать при фильтрации по автору материала.(Guid)*/
    @SerializedName("authorUid")
    @Expose
    var authorUid: String? = null,
    /** Состояние подборки, см. <see cref="CompilationStates"/>. Заполняется только у подборки.*/
    @SerializedName("state")
    @Expose
    var state: Int? = null
) : Folder()

/** Файлы*/
/** Абстрактный базовый класс файла. Может быть картинкой, аудиофайлом, основным вложением документа (PDF) и т.п.*/
abstract class File(
    /** Уникальный идентификатор документа. Может не передаваться при загрузке файла на сервер (это зависит от конкретной операции).(Guid)*/
    @SerializedName("uid")
    @Expose
    var uid: String = "",
    /** Наименование, краткое содержание файла.*/
    @SerializedName("name")
    @Expose
    var name: String? = null,
    /** Примечание.*/
    @SerializedName("remark")
    @Expose
    var remark: String? = null
) : ObjectBase()

/**  Загружаемый на сервер файл.
Если указан blobNo, то содержимое файла передается на сервер как массив байт после тела запроса, см. ServiceMobile.API.HttpHeaders.In.BinaryContentOffset.
Если указан blobUid, то содержимое файла должно быть предварительно загружено на сервер с помощью хандлера UploadFile.ashx.*/
class FileUploadInfo : File() {
    /** Номер (начинается с 1) содержимого файла в запросе, в котором бинарное содержимое передается как массив байт после тела запроса.*/
    @SerializedName("blobNo")
    @Expose
    var blobNo: Int? = null

    /** УИД файлового вложения, загруженного с помощью хандлера UploadFile.ashx.*/
    @SerializedName("blobUid")
    @Expose
    var blobUid: String? = null
}


/** Загружаемый с сервера файл.*/
open class FileDownloadInfo(
    /** Длина файла без учета длины OCR текста. На ее основе можно определить, нужна ли загрузка этого файла с поддержкой докачки или нет.*/
    @SerializedName("size")
    @Expose
    var size: Int? = null,
    /** Дата модификации.()*/
    @SerializedName("lastAccess")
    @Expose
    @ColumnInfo(name = "last_access")
    var lastAccess: String? = null
) : File()

/** Файловое вложение. Возвращается вместе с документом, маршрутом согласования или другим контейнером.*/
@Entity(tableName = "attachments", primaryKeys = ["uid"])
class FileAttachment(
    @ColumnInfo(name = "doc_uid")
    var docUid: String = "",
    /** Определяет, является ли файл основным вложением документа.*/
    @SerializedName("isPrimary")
    @Expose
    @ColumnInfo(name = "is_primary")
    var isPrimary: Boolean = false,
    /** Определяет, был ли документ фактически сжат перед отправкой.*/
    @SerializedName("isCompressed")
    @Expose
    @ColumnInfo(name = "is_compressed")
    var isCompressed: Boolean = false,
    /** Определяет, был ли документ фактически сжат перед отправкой.*/
    @SerializedName("version")
    @Expose
    var version: Int? = null,
    /** Отсоедененная подпись*/
    @SerializedName("detachedSignature")
    @Expose
    @ColumnInfo(name = "signature")
    var detachedSignature: String? = null,
    /** Строка для полнотекстового поиска.*/
    @Ignore
    var searchText: String? = null,
    @ColumnInfo(name = "is_loaded")
    var isLoaded: Boolean = false
) : FileDownloadInfo()

/** Самостоятельный файл, представленный как полноценный документ в системе.*/
class FileDocument(
    /** Идентификатор глобального типа документа (справочник DOCTYPE).*/
    @SerializedName("docType")
    @Expose
    var docType: Int? = null,
    /** Автор документа.*/
    @SerializedName("author")
    @Expose
    var authorUid: String? = null,
    /** Регистрационный номер документа.*/
    @SerializedName("regNum")
    @Expose
    var regNumber: String? = null,
    /** Дата регистрации документа.*/
    @SerializedName("regDate")
    @Expose
    var regDate: String? = null,
    /** Порядковый номер документа в папке (используется в подборках документов).*/
    @SerializedName("order")
    @Expose
    var order: Int? = null,
    /** Важность документа в подборке (заполняется только у документов из подборки).
    По умолчанию 0 (обычная важность). 1 означает, что документ важный.*/
    @SerializedName("importance")
    @Expose
    var importance: Int? = null
) : FileDownloadInfo()