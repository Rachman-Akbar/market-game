package com.example.marketgame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketgame.data.model.UserProfile
import com.example.marketgame.data.remote.AuthRepository
import com.example.marketgame.data.remote.GameDataRepository
import com.example.marketgame.data.remote.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    private val _profile = MutableStateFlow(
        UserProfile(
            name = "Petualang SDGs",
            email = "",
            avatar = null,
            level = 1,
            xp = 0,
            coins = 0,
            completedQuest = 0,
            gamesPlayed = 0
        )
    )
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(authRepository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        if (authRepository.isLoggedIn()) {
            loadProfile()
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            authRepository.getProfile()
                .onSuccess { user ->
                    _profile.value = user.toUserProfile()
                    _isLoggedIn.value = true
                }
                .onFailure { e ->
                    _errorMessage.value = e.message ?: "Gagal memuat profil"
                }

            GameDataRepository.getMyMissions()
                .onSuccess { missions ->
                    _profile.value = _profile.value.copy(
                        completedQuest = missions.count {
                            it.status == "completed" || it.status == "rewarded"
                        }
                    )
                }
                .onFailure { e ->
                    _errorMessage.value = e.message ?: "Gagal memuat misi"
                }

            GameDataRepository.getGameSummary()
                .onSuccess { summary ->
                    _profile.value = _profile.value.copy(
                        gamesPlayed = summary.games_played,
                        coins = summary.coins_earned,
                        xp = summary.correct_answers,
                        level = (summary.games_played / 5) + 1
                    )
                }
                .onFailure { e ->
                    _errorMessage.value = e.message ?: "Gagal memuat ringkasan permainan"
                }

            _isLoading.value = false
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            authRepository.login(email, password)
                .onSuccess { user ->
                    _profile.value = user.toUserProfile()
                    _isLoggedIn.value = true
                    onSuccess()
                }
                .onFailure { e ->
                    _errorMessage.value = e.message ?: "Login gagal"
                }
            _isLoading.value = false
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
            _profile.value = UserProfile(
                name = "Guest", email = "", avatar = null, level = 1,
                xp = 0, coins = 0, completedQuest = 0, gamesPlayed = 0
            )
            onDone()
        }
    }

    // ── Mapping ──────────────────────────────────────────────────────────

    private fun UserResponse.toUserProfile() = UserProfile(
        name = name,
        email = email,
        avatar = avatar,
        level = 1,
        xp = 0,
        coins = 0,
        completedQuest = 0,
        gamesPlayed = 0
    )
}