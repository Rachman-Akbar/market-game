package com.example.marketgame.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketgame.data.remote.AuthRepository
import com.example.marketgame.data.remote.FirebaseGoogleAuth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            authRepository.login(email.trim(), password)
                .onSuccess { onSuccess() }
                .onFailure { e ->
                    errorMessage = e.message ?: "Login gagal. Periksa email dan kata sandi."
                }
            isLoading = false
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            runCatching {
                val idToken = FirebaseGoogleAuth.getFirebaseIdToken(getApplication(), account)
                idToken
            }
                .mapCatching { idToken -> authRepository.firebaseLogin(idToken).getOrThrow() }
                .onSuccess { onSuccess() }
                .onFailure { e ->
                    errorMessage = e.message ?: "Login Google gagal. Coba lagi."
                }
            isLoading = false
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        onSuccess: () -> Unit
    ) {
        if (password.length < 8) {
            errorMessage = "Kata sandi minimal 8 karakter."
            return
        }
        if (password != passwordConfirmation) {
            errorMessage = "Konfirmasi kata sandi tidak cocok."
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            authRepository.register(name.trim(), email.trim(), password, passwordConfirmation)
                .onSuccess { onSuccess() }
                .onFailure { e ->
                    errorMessage = e.message ?: "Registrasi gagal. Coba lagi."
                }
            isLoading = false
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun showGoogleConfigError() {
        errorMessage =
            "Login Google belum dikonfigurasi. Isi GOOGLE_SIGNIN_CLIENT_ID " +
                "(web client ID dari Firebase console) di local.properties / gradle.properties " +
                "atau environment, lalu build ulang."
    }
}