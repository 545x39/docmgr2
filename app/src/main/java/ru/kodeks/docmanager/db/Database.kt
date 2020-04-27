package ru.kodeks.docmanager.db

import androidx.room.RoomDatabase
import ru.kodeks.docmanager.db.dao.*
import ru.kodeks.docmanager.model.data.*


@androidx.room.Database(
    entities = [User::class,
        Setting::class,
        ClassifierItem::class,
        GlobalObject::class,
        Organization::class,
        OrgAddress::class,
        WidgetCategory::class,
        WidgetType::class,
        Desktop::class,
        Widget::class,
        Shortcut::class,
        Document::class,
        ConsiderationStation::class,
        FileAttachment::class,
        DocumentLink::class,
        DocumentWidgetLink::class,
        DocNote::class,
        ApprovalRoute::class,
        ApprovalStage::class,
        ApprovalStation::class], version = 1,
    //Чтоб не орал что не знает куда экспортировать
    exportSchema = false
)
//@TypeConverters(UserRightsTypeConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun docNoteDao(): DocNoteDAO
    abstract fun settingsDao(): SettingsDAO
    abstract fun userDAO(): UserDAO
    abstract fun classifiersDao(): ClassifiersDAO
    abstract fun globalObjectDao(): GlobalObjectsDAO
    abstract fun organizationsDao(): OrganizationsDAO
    abstract fun organizationAddressDao(): OrganizationAddressesDAO
    abstract fun widgetCategoryDao(): WidgetCategoryDAO
    abstract fun widgetTypeDao(): WidgetTypeDAO
    abstract fun desktopDao(): DesktopsDAO
    abstract fun widgetDao(): WidgetsDAO
    abstract fun shortcutDao(): ShortcutsDAO
    abstract fun documentDao(): DocumentsDAO
    abstract fun considerationStationDao(): ConsiderationStationsDAO
    abstract fun documentLinksDao(): DocumentLinksDAO
    abstract fun attachmentsDao(): AttachmentsDAO
    abstract fun documentWidgetLinkDao(): DocumentWidgetLinksDAO
    abstract fun approvalRouteDao(): ApprovalRoutesDAO
    abstract fun approvalStageDao(): ApprovalStagesDAO
    abstract fun approvalStationDao(): ApprovalStationsDAO
}
