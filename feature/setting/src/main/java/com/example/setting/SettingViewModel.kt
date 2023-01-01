package com.example.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datastore.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingDataStore: SettingDataStore
) : ViewModel() {

    data class SettingUiState(
        val limit: Int? = null,
    )

    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState

    init {
        viewModelScope.launch {
            settingDataStore.getRequestLimit().collect {
                _uiState.value = _uiState.value.copy(limit = it)
            }
        }
    }

    fun onLimitSelected(limit: Int) {
        viewModelScope.launch {
            runCatching {
                settingDataStore.saveRequestLimit(limit)
            }.onSuccess {
                _uiState.value = _uiState.value.copy(limit = limit)
            }.onFailure { }
        }
    }
}