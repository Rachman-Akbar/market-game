package com.example.marketgame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketgame.data.remote.GameDataRepository
import com.example.marketgame.data.remote.MyVoucherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VouchersViewModel(application: Application) : AndroidViewModel(application) {

    private val _vouchers = MutableStateFlow<List<MyVoucherResponse>>(emptyList())
    val vouchers: StateFlow<List<MyVoucherResponse>> = _vouchers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _claimId = MutableStateFlow<Int?>(null)
    val claimId: StateFlow<Int?> = _claimId.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadVouchers()
    }

    fun loadVouchers() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            GameDataRepository.getMyVouchers()
                .onSuccess { list ->
                    _vouchers.value = list
                }
                .onFailure { e ->
                    _errorMessage.value = e.message ?: "Gagal memuat voucher"
                }
            _isLoading.value = false
        }
    }

    fun claim(voucherId: Int) {
        viewModelScope.launch {
            _isSubmitting.value = true
            _claimId.value = voucherId
            _errorMessage.value = null
            _message.value = null
            GameDataRepository.claimVoucher(voucherId)
                .onSuccess {
                    _message.value = "Voucher berhasil diklaim."
                    loadVouchers()
                }
                .onFailure { e ->
                    _errorMessage.value = e.message ?: "Gagal mengklaim voucher"
                }
            _claimId.value = null
            _isSubmitting.value = false
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }
}