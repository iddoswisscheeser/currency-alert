package com.student.currencyalert.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.currencyalert.data.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ExchangeRateRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
    
    init {
        loadRates()
    }
    
    fun refresh() {
        loadRates()
    }
    
    private fun loadRates() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getExchangeRates().fold(
                onSuccess = { rate ->
                    _uiState.value = _uiState.value.copy(
                        rates = mapOf("KRW" to rate),
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        rates = null,
                        error = error.message,
                        isLoading = false
                    )
                }
            )
        }
    }
}

data class UiState(
    val rates: Map<String, Double>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
