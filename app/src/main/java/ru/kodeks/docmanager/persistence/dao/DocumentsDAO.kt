package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import ru.kodeks.docmanager.model.data.Document

@Dao
interface DocumentsDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insert(document: Document)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(documents: List<Document>)


}