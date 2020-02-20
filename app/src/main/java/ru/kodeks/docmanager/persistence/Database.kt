package ru.kodeks.docmanager.persistence

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kodeks.docmanager.model.data.DocNote
import ru.kodeks.docmanager.model.data.InitData
import ru.kodeks.docmanager.model.data.Setting
import ru.kodeks.docmanager.persistence.dao.DocNoteDAO
import ru.kodeks.docmanager.persistence.dao.InitDao
import ru.kodeks.docmanager.persistence.dao.SettingsDAO
import ru.kodeks.docmanager.persistence.typeconverters.UserRightsTypeConverter
import ru.kodeks.docmanager.util.DocManagerApp
import java.io.File


@androidx.room.Database(entities = [InitData::class, DocNote::class, Setting::class], version = 1)
//@TypeConverters(UserRightsTypeConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun docNoteDao(): DocNoteDAO
    abstract fun settingsDao(): SettingsDAO
    abstract fun initDao(): InitDao

    companion object {
        val dbName =
            "${DocManagerApp.instance.dbDirectory}${File.separator}${DocManagerApp.instance.user.login}"
        var INSTANCE = Room.databaseBuilder(DocManagerApp.instance, Database::class.java, dbName)
            .fallbackToDestructiveMigration().build()
    }
}
