package com.example.swiftwords.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Int,
    val nickname: String,
    val character: Boolean,//0 for male 1 for female
    val currentLevel: Int,
    val streak: Int,
    val highScore: Int,
    val dailyDate: String,
    val lives: Int,
    val lifeDate: String
)