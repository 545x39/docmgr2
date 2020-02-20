package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Корневой класс для хранения информации по рабочим столам, виджетам и их метаинформации (категории и типы).*/
class Workbench(
        /** Категории виджетов. */
        @SerializedName("widgetCategories")
        @Expose
        var widgetCategories: List<WidgetCategory>? = null,
        /** Типы виджетов (содержат названия виджетов для отображения конечному пользователю).*/
        @SerializedName("widgetTypes")
        @Expose
        var widgetTypes: List<WidgetType>? = null,
        /** Рабочие столы вместе с их виджетами и шорткатами (наполнение зависит от необходимости возвращать на клиента мета данные по виджетам и рабочим столам).*/
        @SerializedName("desktops")
        @Expose
        var desktops: List<Desktop>? = null
) : ObjectBase()

/** Категория виджета. Например, "Кнопка", "Исходящяя резолюция", "Предписания дела".*/
class WidgetCategory(
        /// Код категории виджета.
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        /** Наименование категории для отображения пользователю.*/
        @SerializedName("name")
        @Expose
        var name: String? = null,
        /** Системное имя категории виджета.*/
        @SerializedName("sysName")
        @Expose
        var sysName: String? = null
) : ObjectBase()

/** Тип виджета/шортката (по сути название на рабочем столе). Например, "На согласование", "Согласованные", "Атрибутный поиск".*/
class WidgetType(
        /** Код типа виджета.*/
        @SerializedName("id")
        var id: Int? = null,
        /** Наименование типа для отображения пользователю.*/
        @SerializedName("name")
        var name: String? = null,
        /** Системное имя типа виджета.*/
        @SerializedName("sysName")
        var sysName: String? = null
) : ObjectBase()

/** Рабочий стол. Содержит полную метаинформацию о рабочем столе и список дочерних виджетов и шорткатов. \b Внимание: Если в методе синхронизации
 * не запрошена метаинформация по рабочему месту (<see cref="IoModel.DataFilter.WorkbenchMeta"/>), то передается только <see cref="id"/> стола.*/
class Desktop(
        /** Идентификатор рабочего стола*/
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        /** Тип рабочего стола*/
        @SerializedName("type")
        @Expose
        var type: Int? = null,
        /** Имя рабочего стола*/
        @SerializedName("title")
        @Expose
        var title: String? = null,
        /** Описание рабочего стола*/
        @SerializedName("description")
        @Expose
        var description: String? = null,
        /** Keyname цветовой схемы рабочего стола*/
        @SerializedName("color")
        @Expose
        var colorSchema: String? = null,
        /** Имя файла иконки*/
        @SerializedName("icon")
        @Expose
        var icon: String? = null,
        /** Имя файла фоновой картинки*/
        @SerializedName("bgPic")
        @Expose
        var bgPic: String? = null,
        /** Порядок расположения рабочего стола*/
        @SerializedName("order")
        @Expose
        var order: Int? = null,
        /** Определяет, можно ли изменять набор и расположение виджетов*/
        @SerializedName("fixed")
        @Expose
        var fixed: Boolean? = null,
        /** Определяет, можно ли удалять*/
        @SerializedName("removable")
        @Expose
        var removable: Boolean? = null,
        /** Виджеты*/
        @SerializedName("widgets")
        @Expose
        var widgets: List<Widget>? = null,
        /** Шорткаты*/
        @SerializedName("shortcuts")
        @Expose
        var shortcuts: List<Shortcut>? = null
) : ObjectBase()

/** Базовый класс для элемента рабочего стола. Обобщает виджеты и шорткаты. \b Внимание: Если в методе синхронизации не запрошена
метаинформация по рабочему месту (<see cref="IoModel.DataFilter.WorkbenchMeta"/>), то передается только <see cref="id"/> и <see cref="type"/> виджета/шортката.*/
open class DesktopItem(
        /** Идентификатор элемента*/
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        /** Идентификатор типа элемента*/
        @SerializedName("type")
        @Expose
        var type: Int? = null,
        /** Категория (базовый тип) элемента*/
        @SerializedName("category")
        @Expose
        var category: Int? = null,
        /** Имя элемента*/
        @SerializedName("title")
        @Expose
        var title: String? = null,
        /** Keyname цветовой схемы элемента*/
        @SerializedName("color")
        @Expose
        var color: String? = null,
        /** Определяет, можно ли удалять этот элемент рабочего стола.
        \b Внимание: этот признак инвертирован по отношению к своему серверному признаку IsRemovable, чтобы уменьшить кол-во передаваемых данных (в большинстве виджетов \e nonRemovable=false).*/
        @SerializedName("nonRemovable")
        @Expose
        var nonRemovable: Boolean? = null
) : ObjectBase()

/** Виджет. Основной дочерний элемент рабочего стола.  \b Внимание: Если в методе синхронизации не запрошена метаинформация по рабочему месту
(\link IoModel.DataFilter DataFilter::WorkbenchMeta\endlink), то передается только <see cref="id"/> и <see cref="type"/> виджета.*/
class Widget(
        /** Дополнительное наименование*/
        @SerializedName("subTitle")
        @Expose
        var subTitle: String? = null,
        /** Описание виджета*/
        @SerializedName("description")
        @Expose
        var description: String? = null,
        /** Является ли виджет архивом. Для архивов никогда не будет возвращаться список. УИДов необновленных документов, относящихся к этому виджету (из-за объема данных,
        который с годами будет становиться все больше и больше).*/
        @SerializedName("isArchive")
        @Expose
        var isArchive: Boolean? = null,
        /** Идентификатор замещаемого сотрудника (для виджетов замещений).*/
        @SerializedName("absentExecutiveUid")
        @Expose
//    var absentExecutiveUid: Guid? = null
        var absentExecutiveUid: String? = null,
        /** <i>Устаревшее поле, следует использовать <see cref="absentExecutiveUid"/></i>. Идентификатор замещаемого сотрудника (для виджетов замещений).*/
        @SerializedName("absentExecutiveUID")
        @Expose
//    var absentExecutiveUidOld: Guid? = null
        var absentExecutiveUidOld: String? = null,
        /** Сиквенс последней синхронизации виджета. Пропущенное или нулевое значение означает, что по виджету требуется полная синхронизация. \b Внимание: Поле используется только в \b запросе на синхронизацию!*/
        @SerializedName("sequence")
        @Expose
        var sequence: Long? = null,
        /** Кол-во документов в виджете. Заполняется только если включен флаг \ref ServiceMobile.API.IoModel.DataFilter<b>.WidgetDocCounters</b>*/
        @SerializedName("docsCount")
        @Expose
        var docsCount: Int? = null,
        /** Нужно ли заполнять в виджете коллекцию \b oldDocUids. \b Внимание: Поле используется только в \b запросе на синхронизацию!*/
        @SerializedName("fillOldDocUids")
        @Expose
        var fillOldDocUids: Boolean? = null,
        /** Список УИДов документов из виджета, не менявшихся после \link DataModel.DbVersion DbVersion::main\endlink
        Заполняется только при условии, что при синхронизации установлен фильтр \link IoModel.DataFilter DataFilter::OldDocUids\endlink.
        Объединяя этот массив со списком свежих документов в виджете (см. <see cref="DocumentWidgetLink"/>) можно получить полное множество документов в данном виджете. */
        @SerializedName("oldDocUids")
        @Expose
        //    var oldDocUids: List<Guid> ? = null
        var oldDocUids: List<String>? = null
) : DesktopItem()

/** Шорткат. Своеобразная кнопка для перехода к функциональности системы. Например, шорткат "Атрибутный поиск".
\b Внимание: Если в методе синхронизации не запрошена метаинформация по рабочему месту
(<see cref="IoModel.DataFilter.WorkbenchMeta"/>), то передается только <see cref="id"/> шортката.*/
class Shortcut(
        /** Имя файла иконки*/
        @SerializedName("icon")
        @Expose
        var icon: String? = null,
        /** Идентификатор виджета - группы шорткатов*/
        @SerializedName("parent")
        @Expose
        var parent: Int? = null
) : DesktopItem()