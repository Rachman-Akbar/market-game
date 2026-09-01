package com.example.marketgame.data.remote

import android.content.Context
import android.content.SharedPreferences

/**
 * Handles all authentication-related API calls and token persistence.
 *
 * Flow:
 *   1. Call login() or register()
 *   2. On success, token is saved to SharedPreferences
 *   3. ApiClient's authInterceptor automatically attaches the token to subsequent requests
 *   4. Call logout() to clear the token
 */
class AuthRepository(context: Context) {

    private val api = ApiClient.getInstance(context)
    private val prefs: SharedPreferences =
        context.getSharedPreferences("api_config_prefs", Context.MODE_PRIVATE)

    // ── Login ────────────────────────────────────────────────────────────

    suspend fun login(email: String, password: String): Result<UserResponse> {
        return try {
            val result = api.login(LoginRequest(email, password))
            val authData = result.data
            val user = authData?.user
            if (authData?.token != null) {
                saveToken(authData.token)
                saveUserId(user?.id)
            }
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(RuntimeException(result.message ?: "Login gagal. Periksa email dan kata sandi."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Register ─────────────────────────────────────────────────────────

    suspend fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Result<UserResponse> {
        return try {
            val result = api.register(
                RegisterRequest(name, email, password, passwordConfirmation)
            )
            val authData = result.data
            val user = authData?.user
            if (authData?.token != null) {
                saveToken(authData.token)
                saveUserId(user?.id)
            }
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(RuntimeException(result.message ?: "Registrasi gagal."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Get current user ─────────────────────────────────────────────────

    suspend fun getProfile(): Result<UserResponse> {
        return try {
            val result = api.getMe()
            val user = result.data
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(RuntimeException(result.message ?: "Gagal memuat profil."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Logout ───────────────────────────────────────────────────────────

    suspend fun logout() {
        try {
            api.logout()
        } catch (_: Exception) {
            // Proceed with local logout even if server call fails
        }
        clearToken()
    }

    // ── Token helpers ────────────────────────────────────────────────────

    fun isLoggedIn(): Boolean = getToken() != null

    fun getToken(): String? = prefs.getString("auth_token", null)

    private fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    private fun saveUserId(userId: Int?) {
        if (userId != null) {
            prefs.edit().putInt("user_id", userId).apply()
        }
    }

    private fun clearToken() {
        prefs.edit()
            .remove("auth_token")
            .remove("user_id")
            .apply()
    }
}
