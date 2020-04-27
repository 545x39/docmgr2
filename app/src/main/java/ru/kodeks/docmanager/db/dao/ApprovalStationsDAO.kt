package ru.kodeks.docmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.kodeks.docmanager.model.data.ApprovalStation

@Dao
interface ApprovalStationsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(approvalStation: ApprovalStation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(approvalStations: List<ApprovalStation>)
}