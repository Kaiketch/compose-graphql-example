package com.example.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel() {

    data class SettingUiState(
        val limit: Int? = null,
    )

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState

    init {
        viewModelScope.launch {
            val limit = settingRepository.fetchRequestLimit().first()
            _uiState.value = _uiState.value.copy(limit = limit)
        }
    }

    fun onLimitSelected(limit: Int) {
        viewModelScope.launch {
            runCatching {
                settingRepository.editRequestLimit(limit)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(limit = limit)
            }.onFailure { }
        }
    }
}