package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import ru.kodeks.docmanager.model.data.ConsiderationStation

@Dao
interface ConsiderationStationsDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insert(station: ConsiderationStation)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(stations: List<ConsiderationStation>)
}