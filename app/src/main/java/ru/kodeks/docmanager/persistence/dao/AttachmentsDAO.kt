package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.kodeks.docmanager.model.data.FileAttachment

@Dao
interface AttachmentsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(attachment: FileAttachment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(attachments: List<FileAttachment>)
}