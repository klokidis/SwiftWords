package com.yukuro.swiftwords.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yukuro.swiftwords.model.User

//This class has one method that returns the existing instance of the RoomDatabase if the database doesn't exist.
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class InventoryDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null // this  keeps a reference to the database,
        // when one has been created. This helps maintain a single instance of the database opened at a given time

        fun getDatabase(context: Context): InventoryDatabase {
            return Instance ?: synchronized(this) {//a synchronized block means that only one thread of execution at a time can enter this block of code, which makes sure the database only gets initialized once.
                Room.databaseBuilder(context, InventoryDatabase::class.java, "user_database")
                    .fallbackToDestructiveMigration()
                    .createFromAsset("database/user_database.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}