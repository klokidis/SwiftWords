package com.example.swiftwords.data

import androidx.room.Entity

@Entity(tableName = "user")
data class User(
    val nickname: String,
    val character: Boolean,//0 for male 1 for female
    val currentLevel: Int,
    val streak: Int,
    val highScore: Int,
    val dailyDate: String,
    val lives: Int,
    val lifeDate: String
)