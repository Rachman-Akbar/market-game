package com.example.marketgame.data.model

data class MatchCard(
    val id: Int,
    val text: String,
    val pairId: Int,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)
