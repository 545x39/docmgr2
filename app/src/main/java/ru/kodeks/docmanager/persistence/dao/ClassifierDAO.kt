package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.kodeks.docmanager.model.data.ClassifierItem

@Dao
interface ClassifierDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(classifier: ClassifierItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(classifier: ClassifierItem)
}