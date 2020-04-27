package ru.kodeks.docmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kodeks.docmanager.model.data.GlobalObject

@Dao
interface GlobalObjectsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(globalObject: GlobalObject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(globalObject: List<GlobalObject>)

    @Query("SELECT COUNT(1) FROM global_objects")
    suspend fun count(): Int
}