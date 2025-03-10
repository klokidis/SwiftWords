package com.yukuro.swiftwords.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yukuro.swiftwords.model.User
import kotlinx.coroutines.flow.Flow

//Single-responsibility principle
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * from user")
    fun getUser(): Flow<User>
}