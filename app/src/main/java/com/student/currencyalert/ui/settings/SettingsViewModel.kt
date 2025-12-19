package com.student.currencyalert.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.student.currencyalert.utils.PreferencesHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                startTime = preferencesHelper.getStartTime(),
                endTime = preferencesHelper.getEndTime(),
                notificationsEnabled = preferencesHelper.getNotificationsEnabled()
            )
        }
    }
    
    fun showStartTimePicker() {
        // Time picker logic would go here
    }
    
    fun showEndTimePicker() {
        // Time picker logic would go here
    }
    
    fun toggleNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }
    
    fun saveSettings() {
        viewModelScope.launch {
            val state = _uiState.value
            preferencesHelper.saveStartTime(state.startTime)
            preferencesHelper.saveEndTime(state.endTime)
            preferencesHelper.saveNotificationsEnabled(state.notificationsEnabled)
        }
    }
}

data class SettingsUiState(
    val startTime: String = "09:00",
    val endTime: String = "17:00",
    val notificationsEnabled: Boolean = true
)
