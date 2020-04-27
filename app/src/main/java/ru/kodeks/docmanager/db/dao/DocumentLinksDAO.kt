package ru.kodeks.docmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import ru.kodeks.docmanager.model.data.DocumentLink

@Dao
interface DocumentLinksDAO {
    @Insert(onConflict = REPLACE)
    suspend fun insert(link: DocumentLink)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(links: List<DocumentLink>)
}