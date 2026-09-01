package com.example.marketgame.data.model

import androidx.annotation.DrawableRes

data class TrashItem(
    val name: String,
    val correctBinColor: String,
    val category: String,
    @DrawableRes val imageRes: Int
)
