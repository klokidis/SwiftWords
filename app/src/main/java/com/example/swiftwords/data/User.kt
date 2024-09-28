package com.example.swiftwords.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Int,
    val initializeProfile: Boolean,
    val nickname: String,
    val character: Boolean,//0 for male 1 for female
    val currentLevel: Int,
    val streak: Int,
    val highScore: Int,
    val dailyDate: String,
    val lives: Int, //to-do
    val lifeDate: String, //to-do
    val setOfLetters: String,
    val color: Int,
    val starterLevel: Int,
    val endingLevel: Int,
    val checked: Boolean,
    val levelTime: Long
)