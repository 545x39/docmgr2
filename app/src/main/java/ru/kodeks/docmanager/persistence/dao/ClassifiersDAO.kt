package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kodeks.docmanager.model.data.ClassifierItem

@Dao
interface ClassifiersDAO {
    @Insert(onConflict = REPLACE)
    fun insert(classifier: ClassifierItem)

    @Insert(onConflict = REPLACE)
    fun insertAll(classifiers: ArrayList<ClassifierItem>)

    @Query("SELECT COUNT(1) FROM classifiers")
    fun count(): Int
}