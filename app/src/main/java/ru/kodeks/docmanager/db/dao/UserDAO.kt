package ru.kodeks.docmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kodeks.docmanager.model.data.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    suspend fun getUser(): User?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
}