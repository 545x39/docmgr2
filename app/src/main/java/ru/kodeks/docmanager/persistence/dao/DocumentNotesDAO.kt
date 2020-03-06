package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kodeks.docmanager.model.data.DocNote

@Dao
interface DocNoteDAO{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: DocNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<DocNote>)

//    @Query("SELECT * FROM document_notes")
//    suspend fun selectAll(): LiveData<List<DocNote>>

    @Query("SELECT COUNT(1) FROM document_notes")
    suspend fun getCount(): Int

}