package com.yukuro.swiftwords.data

import com.yukuro.swiftwords.model.User
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {

    override fun getUserStream(): Flow<User?> = userDao.getUser()

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun updateUser(user: User) = userDao.update(user)

}