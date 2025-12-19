package com.student.currencyalert.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.currencyalert.data.api.FrankfurterService
import com.student.currencyalert.data.database.entity.ExchangeRateEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val frankfurterService: FrankfurterService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState
    
    init {
        loadHistory(7)
    }
    
    fun loadHistory(days: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val endDate = Date()
                val startDate = Date(endDate.time - (days * 24 * 60 * 60 * 1000L))
                
                val dateRange = "${dateFormat.format(startDate)}..${dateFormat.format(endDate)}"
                
                val response = frankfurterService.getHistoricalRates(dateRange, "CAD", "KRW")
                
                if (response.isSuccessful) {
                    val data = response.body()?.rates?.map { (date, rates) ->
                        ExchangeRateEntity(
                            currencyPair = "CAD-KRW",
                            rate = rates["KRW"] ?: 0.0,
                            timestamp = dateFormat.parse(date)?.time ?: 0L
                        )
                    } ?: emptyList()
                    
                    _uiState.value = _uiState.value.copy(
                        historyData = data.sortedBy { it.timestamp },
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "API Error: ${response.code()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}

data class HistoryUiState(
    val historyData: List<ExchangeRateEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
