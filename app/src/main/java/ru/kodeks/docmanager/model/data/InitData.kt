package ru.kodeks.docmanager.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.kodeks.docmanager.persistence.typeconverters.UserRightsTypeConverter

@Entity(tableName = "init_table")
@TypeConverters(UserRightsTypeConverter::class)
data class InitData(
    @ColumnInfo(name = "login")
    var login: String = "",
    @ColumnInfo(name = "password")
    var password: String = "",
    @PrimaryKey
    @ColumnInfo(name = "user_uid")
    var userUid: String = "",
    @ColumnInfo(name = "sequence")
    var sequence: Int = 0,
    @ColumnInfo(name = "global_sequence")
    var globalSequence: Int = 0,
    @ColumnInfo(name = "settings_sequence")
    var settingsSequence: Int = 0,
    @ColumnInfo(name = "user_rights")
    var userRights: List<Int> = listOf(),
    @ColumnInfo(name = "server_version")
    var serverVersion: String = ""
)