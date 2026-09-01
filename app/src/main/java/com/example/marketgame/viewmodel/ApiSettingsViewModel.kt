package com.example.marketgame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketgame.data.remote.ApiClient
import com.example.marketgame.data.remote.BaseUrlManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the API Settings screen.
 * Allows the user to switch between Emulator / Custom IP / Production.
 */
class ApiSettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val baseUrlManager = BaseUrlManager.getInstance(application)

    private val _currentUrl = MutableStateFlow(baseUrlManager.getBaseUrl())
    val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()

    private val _selectedPreset = MutableStateFlow("EMULATOR")
    val selectedPreset: StateFlow<String> = _selectedPreset.asStateFlow()

    private val _testResult = MutableStateFlow<String?>(null)
    val testResult: StateFlow<String?> = _testResult.asStateFlow()

    private val _isTesting = MutableStateFlow(false)
    val isTesting: StateFlow<Boolean> = _isTesting.asStateFlow()

    init {
        viewModelScope.launch {
            baseUrlManager.baseUrlFlow.collect { url ->
                _currentUrl.value = url
            }
        }
    }

    fun setCustomUrl(url: String) {
        viewModelScope.launch {
            baseUrlManager.setBaseUrl(url)
            ApiClient.rebuild(getApplication())
            _currentUrl.value = baseUrlManager.getBaseUrl()
        }
    }

    fun setPreset(preset: String) {
        viewModelScope.launch {
            when (preset) {
                "EMULATOR" -> baseUrlManager.useEmulator()
                "PRODUCTION" -> baseUrlManager.useProduction()
            }
            ApiClient.rebuild(getApplication())
            _selectedPreset.value = preset
            _currentUrl.value = baseUrlManager.getBaseUrl()
        }
    }

    fun setLocalDevice(host: String, port: String = "8000") {
        viewModelScope.launch {
            baseUrlManager.useLocalDevice(host, port.toIntOrNull() ?: 8000)
            ApiClient.rebuild(getApplication())
            _selectedPreset.value = "LOCAL_DEVICE"
            _currentUrl.value = baseUrlManager.getBaseUrl()
        }
    }

    fun testConnection() {
        viewModelScope.launch {
            _isTesting.value = true
            _testResult.value = null
            try {
                val api = ApiClient.getInstance(getApplication())
                val response = api.getProducts(page = 1, perPage = 1)
                _testResult.value = "OK - Server terhubung (${response.data.size} products ditemukan)"
            } catch (e: Exception) {
                _testResult.value = "GAGAL - ${e.message}"
            }
            _isTesting.value = false
        }
    }
}
