package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kodeks.docmanager.model.data.DocumentWidgetLink

@Dao
interface DocumentWidgetLinksDAO {
    @Insert(onConflict = REPLACE)
    fun insert(link: DocumentWidgetLink)

    @Insert(onConflict = REPLACE)
    fun insertAll(links: List<DocumentWidgetLink>)

    @Query("SELECT COUNT(1) FROM document_widget_links")
    fun count(): Int
}