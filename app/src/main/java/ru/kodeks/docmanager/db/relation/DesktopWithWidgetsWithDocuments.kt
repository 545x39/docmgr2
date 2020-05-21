package ru.kodeks.docmanager.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.kodeks.docmanager.model.data.Desktop
import ru.kodeks.docmanager.model.data.Document
import ru.kodeks.docmanager.model.data.DocumentWidgetLink
import ru.kodeks.docmanager.model.data.Widget

data class DesktopWithWidgets(
    @Embedded
    var desktop: Desktop,
    /** Виджеты*/
    @Relation(parentColumn = "id", entityColumn = "desktop_id", entity = Widget::class)
    val widgets: List<WidgetWithDocuments>
)

data class WidgetWithDocuments(
    @Embedded
    val widget: Widget,
    @Relation(
        parentColumn = "id",
        entityColumn = "uid",
        entity = Document::class,
//        projection = ["somefields"], //Для выборки только нужных полей, а не всех
        associateBy = Junction(
            DocumentWidgetLink::class,
            parentColumn = "widget_id", entityColumn = "doc_uid"
        )
    )
    val documents: List<Document>
)