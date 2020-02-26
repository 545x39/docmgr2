package ru.kodeks.docmanager.persistence

import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kodeks.docmanager.model.data.*
import ru.kodeks.docmanager.persistence.dao.*
import ru.kodeks.docmanager.util.DocManagerApp
import java.io.File


@androidx.room.Database(
    entities = [InitData::class,
        DocNote::class,
        Setting::class,
        ClassifierItem::class,
        GlobalObject::class,
        Organization::class,
        OrgAddress::class], version = 1,
    //Чтоб не орал что не знает куда экспортировать
    exportSchema = false
)
//@TypeConverters(UserRightsTypeConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun docNoteDao(): DocNoteDAO
    abstract fun settingsDao(): SettingsDAO
    abstract fun initDao(): InitDao
    abstract fun classifiersDao(): ClassifiersDAO
    abstract fun globalObjectDao(): GlobalObjectsDAO
    abstract fun organizationsDao(): OrganizationsDAO
    abstract fun organizationAddressDao(): OrganizationAddressesDAO

    companion object {
        private val dbName =
            "${DocManagerApp.instance.dbDirectory}${File.separator}${DocManagerApp.instance.user.login}"
        var INSTANCE = Room.databaseBuilder(DocManagerApp.instance, Database::class.java, dbName)
            .fallbackToDestructiveMigration().build()
    }
}
