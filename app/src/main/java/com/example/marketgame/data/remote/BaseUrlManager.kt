package com.example.marketgame.data.remote

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Manages the dynamic base URL for the REST API.
 *
 * Supports three strategies (in priority order):
 * 1. User-configured URL via Settings UI (persisted in DataStore)
 * 2. Automatic default: emulator -> 10.0.2.2, physical device -> 127.0.0.1
 *    (relies on `adb reverse tcp:8000 tcp:8000` so device-localhost maps to the PC)
 *
 * Usage:
 *   val baseUrl = BaseUrlManager.getInstance(context).getBaseUrl()
 */
class BaseUrlManager private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: BaseUrlManager? = null

        fun getInstance(context: Context): BaseUrlManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BaseUrlManager(context.applicationContext).also { INSTANCE = it }
            }
        }

        // Build-time defaults -- change these per environment
        private const val DEFAULT_EMULATOR_URL = "http://10.0.2.2:8000/api/v1/"
        // Physical device: works automatically with `adb reverse tcp:8000 tcp:8000`
        // (USB or wireless adb). No LAN IP needed.
        private const val DEFAULT_DEVICE_URL = "http://127.0.0.1:8000/api/v1/"
        // Standalone fallback (no adb): set to your PC's LAN IP.
        private const val DEFAULT_LOCAL_DEVICE_URL = "http://192.168.100.129:8000/api/v1/"
        private const val DEFAULT_PRODUCTION_URL = "https://your-production-domain.com/api/v1/"

        private val KEY_CUSTOM_BASE_URL = stringPreferencesKey("custom_base_url")
        private val KEY_ENVIRONMENT = stringPreferencesKey("environment")
    }

    // DataStore for coroutine-based persistence
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "api_config"
    )

    // SharedPreferences as a synchronous fallback
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("api_config_prefs", Context.MODE_PRIVATE)
    }

    // ── Preset environment URLs ──────────────────────────────────────────

    enum class Environment(val baseUrl: String) {
        EMULATOR(DEFAULT_EMULATOR_URL),
        LOCAL_DEVICE(DEFAULT_LOCAL_DEVICE_URL),
        PRODUCTION(DEFAULT_PRODUCTION_URL)
    }

    // ── Read current URL (reactive Flow) ─────────────────────────────────

    val baseUrlFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_CUSTOM_BASE_URL] ?: getDefaultUrl()
    }

    // ── Read current URL (blocking, for OkHttp Interceptor) ──────────────

    fun getBaseUrl(): String {
        val custom = prefs.getString(KEY_CUSTOM_BASE_URL.name, null)
        return custom ?: getDefaultUrl()
    }

    // ── Write new URL ─────────────────────────────────────────────────────

    suspend fun setBaseUrl(url: String) {
        val normalized = normalizeUrl(url)
        context.dataStore.edit { preferences ->
            preferences[KEY_CUSTOM_BASE_URL] = normalized
        }
        prefs.edit().putString(KEY_CUSTOM_BASE_URL.name, normalized).apply()
    }

    suspend fun setEnvironment(env: Environment) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_CUSTOM_BASE_URL)
            preferences[KEY_ENVIRONMENT] = env.name
        }
        prefs.edit()
            .remove(KEY_CUSTOM_BASE_URL.name)
            .putString(KEY_ENVIRONMENT.name, env.name)
            .apply()
    }

    // ── Quick preset helpers ──────────────────────────────────────────────

    suspend fun useEmulator() = setEnvironment(Environment.EMULATOR)
    suspend fun useLocalDevice(host: String, port: Int = 8000) {
        setBaseUrl("http://$host:$port/api/v1/")
    }
    suspend fun useProduction() = setEnvironment(Environment.PRODUCTION)

    // ── Internal ──────────────────────────────────────────────────────────

    private fun getDefaultUrl(): String {
        val envName = prefs.getString(KEY_ENVIRONMENT.name, null)
        if (envName != null) {
            try {
                return Environment.valueOf(envName).baseUrl
            } catch (_: Exception) { /* fall through */ }
        }
        return if (isEmulator()) DEFAULT_EMULATOR_URL else DEFAULT_DEVICE_URL
    }

    private fun isEmulator(): Boolean {
        val fingerprint = Build.FINGERPRINT
        return fingerprint.startsWith("generic") ||
            fingerprint.contains("emulator") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("sdk_gphone") ||
            Build.PRODUCT.contains("sdk_gphone") ||
            Build.BRAND.startsWith("generic")
    }

    private fun normalizeUrl(url: String): String {
        var result = url.trim()
        if (!result.startsWith("http://") && !result.startsWith("https://")) {
            result = "http://$result"
        }
        if (!result.endsWith("/")) {
            result = "$result/"
        }
        // Ensure it ends with api/v1/ if it looks like a bare domain
        if (!result.contains("/api/")) {
            result = "${result}api/v1/"
        }
        return result
    }
}
