package com.yukuro.swiftwords.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val userRepository: UserRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val userRepository: UserRepository by lazy {
        OfflineUserRepository(InventoryDatabase.getDatabase(context).userDao())
    }
}