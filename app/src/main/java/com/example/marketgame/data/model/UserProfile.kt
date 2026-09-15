package com.example.marketgame.data.model

data class UserProfile(
    val name: String,
    val email: String,
    val avatar: String?,
    val level: Int,
    val xp: Int,
    val coins: Int,
    val completedQuest: Int,
    val gamesPlayed: Int
)