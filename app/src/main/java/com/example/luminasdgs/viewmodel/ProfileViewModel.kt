package com.example.luminasdgs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.luminasdgs.data.model.UserProfile
import com.example.luminasdgs.data.remote.AuthRepository
import com.example.luminasdgs.data.remote.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Example ViewModel showing how to integrate the Retrofit networking layer.
 *
 * Before (dummy data):
 *   val profile = UserProfile(name = "Pejuang SDGs", level = 5, ...)
 *
 * After (API-connected):
 *   profile is loaded from the server via AuthRepository.getProfile()
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    private val _profile = MutableStateFlow(
        UserProfile(
            name = "Pejuang SDGs",
            level = 1,
            xp = 0,
            coins = 0,
            completedQuest = 0,
            gamesPlayed = 0,
            treeLevel = 1
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

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
            _profile.value = UserProfile(
                name = "Guest", level = 1, xp = 0,
                coins = 0, completedQuest = 0, gamesPlayed = 0, treeLevel = 1
            )
        }
    }

    // ── Mapping ──────────────────────────────────────────────────────────

    private fun UserResponse.toUserProfile() = UserProfile(
        name = name,
        level = 1,
        xp = 0,
        coins = 0,
        completedQuest = 0,
        gamesPlayed = 0,
        treeLevel = 1
    )
}
