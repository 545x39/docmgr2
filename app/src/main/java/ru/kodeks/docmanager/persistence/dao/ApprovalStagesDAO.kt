package ru.kodeks.docmanager.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import ru.kodeks.docmanager.model.data.ApprovalStage

@Dao
interface ApprovalStagesDAO {

    @Insert(onConflict = REPLACE)
    fun insert(approvalStage: ApprovalStage)

    @Insert(onConflict = REPLACE)
    fun insertAll(approvalStages: List<ApprovalStage>)
}