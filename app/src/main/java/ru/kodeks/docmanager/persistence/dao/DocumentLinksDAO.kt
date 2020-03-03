package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import ru.kodeks.docmanager.model.data.DocumentLink

@Dao
interface DocumentLinksDAO {
    @Insert(onConflict = REPLACE)
    fun insert(link: DocumentLink)

    @Insert(onConflict = REPLACE)
    fun insertAll(links: List<DocumentLink>)
}