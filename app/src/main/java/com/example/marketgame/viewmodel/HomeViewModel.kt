package com.example.marketgame.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketgame.data.model.DailyMission
import com.example.marketgame.data.remote.AuthRepository
import com.example.marketgame.data.remote.GameDataRepository
import com.example.marketgame.data.remote.GameSummaryResponse
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    // ── Data dari database ────────────────────────────────────────────────

    var userName by mutableStateOf("Petualang SDGs")
        private set
    var userEmail by mutableStateOf("")
        private set
    var userInitials by mutableStateOf("P")
        private set
    var isLoggedIn by mutableStateOf(false)
        private set

    var missions by mutableStateOf<List<DailyMission>>(emptyList())
        private set
    var gameSummary by mutableStateOf<GameSummaryResponse?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    // ── Modal state (1 halaman utama) ─────────────────────────────────────

    var showSettingsModal by mutableStateOf(false)
        private set
    var showGameMenuModal by mutableStateOf(false)
        private set
    var showMissionsModal by mutableStateOf(false)
        private set

    val completedMissionsToday: Int
        get() = missions.count { it.isCompleted }

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            isLoggedIn = authRepository.isLoggedIn()
            if (isLoggedIn) {
                authRepository.getProfile()
                    .onSuccess { user ->
                        userName = user.name
                        userEmail = user.email
                        userInitials = user.name
                            .split(" ")
                            .mapNotNull { it.firstOrNull() }
                            .take(2)
                            .joinToString("")
                            .uppercase()
                    }
                    .onFailure { e ->
                        errorMessage = e.message ?: "Gagal memuat profil."
                    }
            }

            GameDataRepository.getMyMissions()
                .onSuccess { loaded -> missions = loaded.map { it.toDailyMission() } }
                .onFailure { e -> errorMessage = e.message ?: "Gagal memuat misi harian." }

            GameDataRepository.getGameSummary()
                .onSuccess { gameSummary = it }
                .onFailure { gameSummary = null }

            isLoading = false
        }
    }

    fun refresh() {
        loadHomeData()
        errorMessage = null
    }

    fun openSettings() {
        showSettingsModal = true
    }

    fun closeSettings() {
        showSettingsModal = false
    }

    fun openGameMenu() {
        showGameMenuModal = true
    }

    fun closeGameMenu() {
        showGameMenuModal = false
    }

    fun openMissions() {
        showMissionsModal = true
    }

    fun closeMissions() {
        showMissionsModal = false
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            isLoggedIn = false
            userName = "Petualang SDGs"
            userEmail = ""
            userInitials = "P"
            missions = emptyList()
            gameSummary = null
            showSettingsModal = false
            onDone()
        }
    }

    // ── Mapping misi backend → model tampilan ─────────────────────────────

    private fun com.example.marketgame.data.remote.MissionResponse.toDailyMission() = DailyMission(
        id = id,
        name = name,
        description = description ?: "",
        progressValue = progress_value,
        targetValue = target_value,
        progressPercent = (progress_percent ?: 0.0).toFloat() / 100f,
        status = status ?: "in_progress",
        voucherName = voucher?.name
    )
}