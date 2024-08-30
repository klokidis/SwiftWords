package com.example.swiftwords.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Int,
    val initializeProfile: Boolean,
    val nickname: String, //to-do
    val character: Boolean,//0 for male 1 for female //to-do
    val currentLevel: Int,
    val streak: Int, //to-do
    val highScore: Int,
    val dailyDate: String, //to-do
    val lives: Int, //to-do
    val lifeDate: String, //to-do
    val setOfLetters: String, //to-do
    val color : Int,
    val starterLevel: Int,
    val endingLevel: Int,
    val checked: Boolean
)