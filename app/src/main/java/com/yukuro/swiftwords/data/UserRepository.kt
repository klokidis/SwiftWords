package com.yukuro.swiftwords.data

import com.yukuro.swiftwords.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserStream(): Flow<User?>

    suspend fun insertUser(user: User)

    suspend fun updateUser(user: User)

}
