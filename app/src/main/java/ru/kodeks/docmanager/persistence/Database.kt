package ru.kodeks.docmanager.persistence

import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kodeks.docmanager.model.data.*
import ru.kodeks.docmanager.persistence.dao.*
import ru.kodeks.docmanager.util.DocManagerApp
import java.io.File


@androidx.room.Database(
    entities = [InitData::class,
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
        DocNote::class], version = 1,
    //Чтоб не орал что не знает куда экспортировать
    exportSchema = false
)
//@TypeConverters(UserRightsTypeConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun docNoteDao(): DocNoteDAO
    abstract fun settingsDao(): SettingsDAO
    abstract fun initDao(): InitDAO
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

    companion object {
        private val dbName =
            "${DocManagerApp.instance.dbDirectory}${File.separator}${DocManagerApp.instance.user.login}"
        var INSTANCE = Room.databaseBuilder(DocManagerApp.instance, Database::class.java, dbName)
            .fallbackToDestructiveMigration().build()
    }
}
