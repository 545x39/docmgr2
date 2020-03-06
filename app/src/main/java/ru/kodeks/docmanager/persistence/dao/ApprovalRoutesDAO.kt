package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import ru.kodeks.docmanager.model.data.ApprovalRoute

@Dao
interface ApprovalRoutesDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insert(approvalRoute: ApprovalRoute)

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(approvalRoutes: List<ApprovalRoute>)
}