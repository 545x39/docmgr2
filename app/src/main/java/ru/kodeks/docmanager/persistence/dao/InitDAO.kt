package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.kodeks.docmanager.model.data.InitData

@Dao
interface InitDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(initTable: InitData)
}