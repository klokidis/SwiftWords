package com.yukuro.swiftwords

import android.app.Application
import com.yukuro.swiftwords.data.AppContainer
import com.yukuro.swiftwords.data.AppDataContainer

class InventoryApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
