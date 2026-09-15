package com.example.marketgame.data.remote

import android.content.Context
import android.content.SharedPreferences

/**
 * Handles all authentication-related API calls and token persistence.
 *
 * Flow:
 *   1. Call login() or register()
 *   2. On success, token is saved to an ENCRYPTED SharedPreferences
 *   3. ApiClient's authInterceptor automatically attaches the token to subsequent requests
 *   4. Call logout() to clear the token
 *
 * Storage note: token & user id are stored via [EncryptedSharedPreferences] so
 * they are not readable in plain text from the device filesystem.
 */
class AuthRepository(private val context: Context) {

    private val api = ApiClient.getInstance(context)
    private val prefs: SharedPreferences = SecurePrefs.open(context)

    // ── Login ────────────────────────────────────────────────────────────

    suspend fun login(email: String, password: String): Result<UserResponse> {
        return try {
            handleAuthResponse(api.login(LoginRequest(email, password)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ── Firebase / Google login ──────────────────────────────────────────

    suspend fun firebaseLogin(
        firebaseIdToken: String,
        deviceName: String = "marketplace-android"
    ): Result<UserResponse> {
        return try {
            handleAuthResponse(
                api.firebaseLogin(
                    authorization = "Bearer $firebaseIdToken",
                    body = FirebaseLoginRequest(role = "buyer", device_name = deviceName)
                )
            )
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
            handleAuthResponse(
                api.register(RegisterRequest(name, email, password, passwordConfirmation))
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Backend selalu mengembalikan `api_token`/`access_token` plus `user`.
     * Kalau salah satu hilang, anggap gagal — supaya tidak menyisakan state
     * "setengah login" (ada token tanpa user, atau user tanpa token).
     */
    private fun handleAuthResponse(authData: AuthPayloadResponse): Result<UserResponse> {
        val user = authData.user
        val token = authData.api_token ?: authData.access_token
        if (token.isNullOrBlank()) {
            return Result.failure(RuntimeException("Backend tidak mengembalikan token Sanctum."))
        }
        saveToken(token)
        if (user == null) {
            return Result.failure(RuntimeException("Backend tidak mengembalikan data user."))
        }
        saveUserId(user.id)
        return Result.success(user)
    }

    // ── Get current user ─────────────────────────────────────────────────

    suspend fun getProfile(): Result<UserResponse> {
        return try {
            val user = api.getMe().user
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(RuntimeException("Gagal memuat profil."))
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

        // Rapikan sesi Firebase/Google yang tersimpan di perangkat, supaya
        // akun Google lama tidak "nyangkut" saat user login ulang (sama seperti
        // frontend yang memanggil signOut pada saat logout).
        if (FirebaseGoogleAuth.isConfigured()) {
            runCatching { FirebaseGoogleAuth.signOut(context) }
        }
    }

    // ── Token helpers ────────────────────────────────────────────────────

    fun isLoggedIn(): Boolean = getToken() != null

    fun getToken(): String? = prefs.getString("auth_token", null)

    private fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    private fun saveUserId(userId: String?) {
        if (userId != null) {
            prefs.edit().putString("user_id", userId).apply()
        }
    }

    private fun clearToken() {
        prefs.edit()
            .remove("auth_token")
            .remove("user_id")
            .apply()
    }
}
