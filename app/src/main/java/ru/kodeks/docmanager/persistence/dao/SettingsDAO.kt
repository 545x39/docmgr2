package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.kodeks.docmanager.model.data.Setting

@Dao
interface SettingsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(setting: Setting)
}