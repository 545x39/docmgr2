package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.kodeks.docmanager.model.data.InitData

@Dao
interface InitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(initTable: InitData)
}