package ru.kodeks.docmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.kodeks.docmanager.model.data.DocumentWidgetLink

@Dao
interface DocumentWidgetLinksDAO {
    @Insert(onConflict = REPLACE)
    suspend fun insert(link: DocumentWidgetLink)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(links: List<DocumentWidgetLink>)

    /** Удалить связи документов с виджетами. UID'ы приходят в ответе синка в коллекции unboundDocUids.*/
    @Query("DELETE FROM document_widget_links WHERE doc_uid IN (:links)")
    suspend fun deleteAll(links: List<String>)
}