package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kodeks.docmanager.model.data.GlobalObject

@Dao
interface GlobalObjectsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(globalObject: GlobalObject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(globalObject: List<GlobalObject>)

    @Query("SELECT COUNT(1) FROM global_objects")
    fun count(): Int
}