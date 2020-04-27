package ru.kodeks.docmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kodeks.docmanager.model.data.Document

@Dao
interface DocumentsDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insert(document: Document)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(documents: List<Document>)

    @Query("SELECT * FROM documents")
    suspend fun getDocuments(): List<Document>

}