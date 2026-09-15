package com.example.marketgame.data.model

/**
 * Misi harian yang disajikan dari tabel `missions` di backend
 * (endpoint engagement/missions/me).
 */
data class DailyMission(
    val id: Int,
    val name: String,
    val description: String,
    val progressValue: Int,
    val targetValue: Int,
    val progressPercent: Float,
    val status: String,
    val voucherName: String? = null
) {
    val isCompleted: Boolean
        get() = status == "completed" || status == "rewarded"
}